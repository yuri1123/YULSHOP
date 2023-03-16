package com.yuri.shoppingsite.Repository;
import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yuri.shoppingsite.constant.Category;
import com.yuri.shoppingsite.constant.ItemSellStatus;
import com.yuri.shoppingsite.domain.Chart.CategoryItemsDto;
import com.yuri.shoppingsite.domain.Chart.QCategoryItemsDto;
import com.yuri.shoppingsite.domain.shop.*;
import com.yuri.shoppingsite.domain.shop.QBestSellerItemDto;
import com.yuri.shoppingsite.domain.shop.QItem;
import com.yuri.shoppingsite.domain.shop.QItemImg;
import com.yuri.shoppingsite.domain.shop.QLatestItemDto;
import com.yuri.shoppingsite.domain.shop.QMainItemDto;
import com.yuri.shoppingsite.domain.shop.QOrderItem;
import com.yuri.shoppingsite.domain.shop.QResultSellingItemDto;
import com.yuri.shoppingsite.domain.user.QMember;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;
import com.yuri.shoppingsite.domain.shop.QResultCategoryItemDto;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

//ItemRepositoryCustom 상속
public class ItemRepositoryCustomImpl implements ItemRepositoryCustom {

    //동적 쿼리 생성하기 위해 JPAQueryFactory 클래스 사용
    private JPAQueryFactory queryFactory;
    //JPAQueryFactory의 생성자로 EntityManager 객체를 넣어줌
    public ItemRepositoryCustomImpl(EntityManager em){

        this.queryFactory = new JPAQueryFactory(em);
    }

    //상품 판매 상태 조건이 전체(null)일 경우는 null을 리턴한다. 결과값이 null이면 where절에서 해당조건은 무시된다.
    //상품 판매 조건이 null이 아니라 판매중 or 품절 상태라면 해당 조건의 상품만 조회된다.
    private BooleanExpression searchSellStatusEq(ItemSellStatus searchSellStatus){
        return searchSellStatus == null? null : QItem.item.itemSellStatus.eq(searchSellStatus);
    }

    //searchDate TYpe의 값에 따라서 dateTime의 값을 이전 시간의 값으로 셋팅 후 해당시간 이후로 등록된 상품을 조회
    //ex) searchDate Type값이 1m인 경우 dateTime의 시간을 한달 전으로 셋팅후 최근한달동안 등록된 상품만 조회
    //하도록 조건값을 반환한다.
    private BooleanExpression regDtsAfter(String searchDateType){
        LocalDateTime dateTime = LocalDateTime.now();

        if(StringUtils.equals("all", searchDateType) || searchDateType == null) {
            return null;
        } else if(StringUtils.equals("1d", searchDateType)){
                dateTime = dateTime.minusDays(1);
        } else if(StringUtils.equals("1w", searchDateType)){
            dateTime = dateTime.minusWeeks(1);
        } else if(StringUtils.equals("1m", searchDateType)){
            dateTime = dateTime.minusMonths(1);
        } else if(StringUtils.equals("6m", searchDateType)){
            dateTime = dateTime.minusMonths(6);
        }
        return QItem.item.regTime.after(dateTime);
    }

    //searchBy의 값에 따라서 상품명에 검색어를 포함하고 있는 상품 또는
    // 상품 생성자의 아이디에 검색어를 포함하고 있는 상품을 조회하도록 조건값을 반환한다.
    private BooleanExpression searchByLike(String searchBy, String searchQuery){
        if(StringUtils.equals("itemNm", searchBy)){
            return QItem.item.itemNm.like("%"+searchQuery+"%");
        } else if(StringUtils.equals("createBy", searchBy)){
            return QItem.item.createdBy.like("%"+searchQuery+"%");
        }
        return null;
    }

    @Override
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {

        List<Item> content = queryFactory
                .selectFrom(QItem.item)
                .where(regDtsAfter(itemSearchDto.getSearchDateType()),
                        searchSellStatusEq(itemSearchDto.getSearchSellStatus()),
                        searchByLike(itemSearchDto.getSearchBy(),
                                itemSearchDto.getSearchQuery()))
                .orderBy(QItem.item.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory.select(Wildcard.count).from(QItem.item)
                .where(regDtsAfter(itemSearchDto.getSearchDateType()),
                        searchSellStatusEq(itemSearchDto.getSearchSellStatus()),
                        searchByLike(itemSearchDto.getSearchBy(), itemSearchDto.getSearchQuery()))
                .fetchOne()
                ;

        return new PageImpl<>(content, pageable, total);
    }

    private BooleanExpression itemNmLike(String searchQuery){
        return StringUtils.isEmpty(searchQuery) ?
                null : QItem.item.itemNm.like("%"+searchQuery+"%");
    }

    @Override
    public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
        QItem item = QItem.item;
        QItemImg itemImg = QItemImg.itemImg;

        List<MainItemDto> content = queryFactory
                .select(
                        new QMainItemDto(
                                item.id,
                                item.category,
                                item.itemNm,
                                item.itemDetail,
                                itemImg.imgUrl,
                                item.price,
                                item.stockNumber,
                                item.itemSellStatus,
                                item.regTime)
                )
                .from(itemImg)
                .join(itemImg.item, item)
                .where(itemImg.repimgYn.eq("Y"))
                .where(item.itemSellStatus.eq(ItemSellStatus.SELL)
                        .or(item.itemSellStatus.eq(ItemSellStatus.SOLD_OUT))
                )
                .where(itemNmLike(itemSearchDto.getSearchQuery()))
                .orderBy(item.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .select(Wildcard.count)
                .from(itemImg)
                .join(itemImg.item, item)
                .where(itemImg.repimgYn.eq("Y"))
                .where(itemNmLike(itemSearchDto.getSearchQuery()))
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<MainItemDto> getALLMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
        QItem item = QItem.item;
        QItemImg itemImg = QItemImg.itemImg;

        List<MainItemDto> content = queryFactory
                .select(
                        new QMainItemDto(
                                item.id,
                                item.category,
                                item.itemNm,
                                item.itemDetail,
                                itemImg.imgUrl,
                                item.price,
                                item.stockNumber,
                                item.itemSellStatus,
                                item.regTime)
                )
                .from(itemImg)
                .join(itemImg.item, item)
                .where(itemImg.repimgYn.eq("Y"))
                .where(itemNmLike(itemSearchDto.getSearchQuery()))
                .orderBy(item.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .select(Wildcard.count)
                .from(itemImg)
                .join(itemImg.item, item)
                .where(itemImg.repimgYn.eq("Y"))
                .where(itemNmLike(itemSearchDto.getSearchQuery()))
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<MainItemDto> getWallDecoItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
        QItem item = QItem.item;
        QItemImg itemImg = QItemImg.itemImg;

        List<MainItemDto> content = queryFactory
                .select(
                        new QMainItemDto(
                                item.id,
                                item.category,
                                item.itemNm,
                                item.itemDetail,
                                itemImg.imgUrl,
                                item.price,
                                item.stockNumber,
                                item.itemSellStatus,
                                item.regTime)
                )
                .from(itemImg)
                .join(itemImg.item, item)
                .where(itemImg.repimgYn.eq("Y"))
                .where(item.category.eq(Category.WALLDECO))
                .where(itemNmLike(itemSearchDto.getSearchQuery()))
                .orderBy(item.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .select(Wildcard.count)
                .from(itemImg)
                .join(itemImg.item, item)
                .where(itemImg.repimgYn.eq("Y"))
                .where(item.itemSellStatus.eq(ItemSellStatus.SELL), item.itemSellStatus.eq(ItemSellStatus.SOLD_OUT))
                .where(itemNmLike(itemSearchDto.getSearchQuery()))
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<MainItemDto> getPenItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
        QItem item = QItem.item;
        QItemImg itemImg = QItemImg.itemImg;

        List<MainItemDto> content = queryFactory
                .select(
                        new QMainItemDto(
                                item.id,
                                item.category,
                                item.itemNm,
                                item.itemDetail,
                                itemImg.imgUrl,
                                item.price,
                                item.stockNumber,
                                item.itemSellStatus,
                                item.regTime)
                )
                .from(itemImg)
                .join(itemImg.item, item)
                .where(itemImg.repimgYn.eq("Y"))
                .where(item.category.eq(Category.PEN))
                .where(item.itemSellStatus.eq(ItemSellStatus.SELL)
                        .or(item.itemSellStatus.eq(ItemSellStatus.SOLD_OUT))
                )
                .where(itemNmLike(itemSearchDto.getSearchQuery()))
                .orderBy(item.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .select(Wildcard.count)
                .from(itemImg)
                .join(itemImg.item, item)
                .where(itemImg.repimgYn.eq("Y"))
                .where(itemNmLike(itemSearchDto.getSearchQuery()))
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<MainItemDto> getLivingItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
        QItem item = QItem.item;
        QItemImg itemImg = QItemImg.itemImg;

        List<MainItemDto> content = queryFactory
                .select(
                        new QMainItemDto(
                                item.id,
                                item.category,
                                item.itemNm,
                                item.itemDetail,
                                itemImg.imgUrl,
                                item.price,
                                item.stockNumber,
                                item.itemSellStatus,
                                item.regTime)
                )
                .from(itemImg)
                .join(itemImg.item, item)
                .where(itemImg.repimgYn.eq("Y"))
                .where(item.category.eq(Category.LIVING))
                .where(item.itemSellStatus.eq(ItemSellStatus.SELL)
                        .or(item.itemSellStatus.eq(ItemSellStatus.SOLD_OUT))
                )
                .where(itemNmLike(itemSearchDto.getSearchQuery()))
                .orderBy(item.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .select(Wildcard.count)
                .from(itemImg)
                .join(itemImg.item, item)
                .where(itemImg.repimgYn.eq("Y"))
                .where(itemNmLike(itemSearchDto.getSearchQuery()))
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<MainItemDto> getCardItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
        QItem item = QItem.item;
        QItemImg itemImg = QItemImg.itemImg;

        List<MainItemDto> content = queryFactory
                .select(
                        new QMainItemDto(
                                item.id,
                                item.category,
                                item.itemNm,
                                item.itemDetail,
                                itemImg.imgUrl,
                                item.price,
                                item.stockNumber,
                                item.itemSellStatus,
                                item.regTime)
                )
                .from(itemImg)
                .join(itemImg.item, item)
                .where(itemImg.repimgYn.eq("Y"))
                .where(item.category.eq(Category.CARD))
                .where(item.itemSellStatus.eq(ItemSellStatus.SELL)
                        .or(item.itemSellStatus.eq(ItemSellStatus.SOLD_OUT))
                )
                .where(itemNmLike(itemSearchDto.getSearchQuery()))
                .orderBy(item.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .select(Wildcard.count)
                .from(itemImg)
                .join(itemImg.item, item)
                .where(itemImg.repimgYn.eq("Y"))
                .where(itemNmLike(itemSearchDto.getSearchQuery()))
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<MainItemDto> getAccItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
        QItem item = QItem.item;
        QItemImg itemImg = QItemImg.itemImg;

        List<MainItemDto> content = queryFactory
                .select(
                        new QMainItemDto(
                                item.id,
                                item.category,
                                item.itemNm,
                                item.itemDetail,
                                itemImg.imgUrl,
                                item.price,
                                item.stockNumber,
                                item.itemSellStatus,
                                item.regTime)
                )
                .from(itemImg)
                .join(itemImg.item, item)
                .where(itemImg.repimgYn.eq("Y"))
                .where(item.category.eq(Category.ACCESSORY))
                .where(item.itemSellStatus.eq(ItemSellStatus.SELL)
                        .or(item.itemSellStatus.eq(ItemSellStatus.SOLD_OUT))
                )
                .where(itemNmLike(itemSearchDto.getSearchQuery()))
                .orderBy(item.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .select(Wildcard.count)
                .from(itemImg)
                .join(itemImg.item, item)
                .where(item.itemSellStatus.eq(ItemSellStatus.SELL), item.itemSellStatus.eq(ItemSellStatus.SOLD_OUT))
                .where(itemImg.repimgYn.eq("Y"))
                .where(itemNmLike(itemSearchDto.getSearchQuery()))
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<MainItemDto> getDiaryItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
        QItem item = QItem.item;
        QItemImg itemImg = QItemImg.itemImg;

        List<MainItemDto> content = queryFactory
                .select(
                        new QMainItemDto(
                                item.id,
                                item.category,
                                item.itemNm,
                                item.itemDetail,
                                itemImg.imgUrl,
                                item.price,
                                item.stockNumber,
                                item.itemSellStatus,
                                item.regTime)
                )
                .from(itemImg)
                .join(itemImg.item, item)
                .where(itemImg.repimgYn.eq("Y"))
                .where(item.category.eq(Category.DIARY))
                .where(item.itemSellStatus.eq(ItemSellStatus.SELL)
                        .or(item.itemSellStatus.eq(ItemSellStatus.SOLD_OUT))
                )
                .where(itemNmLike(itemSearchDto.getSearchQuery()))
                .orderBy(item.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .select(Wildcard.count)
                .from(itemImg)
                .join(itemImg.item, item)
                .where(itemImg.repimgYn.eq("Y"))
                .where(itemNmLike(itemSearchDto.getSearchQuery()))
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<BestSellerItemDto> getBestSellerItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
        QItem item = QItem.item;
        QItemImg itemImg = QItemImg.itemImg;

        List<BestSellerItemDto> content = queryFactory
                .select(
                        new QBestSellerItemDto(
                                item.id,
                                item.itemNm,
                                item.itemDetail,
                                itemImg.imgUrl,
                                item.price,
                                item.orderTotalCount)
                ).from(itemImg)
                .join(itemImg.item, item)
                .where(itemImg.repimgYn.eq("Y"))
                .where(itemNmLike(itemSearchDto.getSearchQuery()))
                .orderBy(item.orderTotalCount.desc())
                .offset(pageable.getOffset())
                .limit(12)
                .fetch();

        long total = queryFactory
                .select(Wildcard.count)
                .from(itemImg)
                .join(itemImg.item, item)
                .where(itemImg.repimgYn.eq("Y"))
                .where(itemNmLike(itemSearchDto.getSearchQuery()))
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<LatestItemDto> getLatestItemDto(ItemSearchDto itemSearchDto, Pageable pageable) {
        QItem item = QItem.item;
        QItemImg itemImg = QItemImg.itemImg;

        List<LatestItemDto> content = queryFactory
                .select(
                        new QLatestItemDto(
                                item.id,
                                item.itemNm,
                                item.itemDetail,
                                itemImg.imgUrl,
                                item.price,
                                item.orderTotalCount)
                ).from(itemImg)
                .join(itemImg.item, item)
                .where(itemImg.repimgYn.eq("Y"))
                .where(itemNmLike(itemSearchDto.getSearchQuery()))
                .orderBy(item.regTime.desc())
                .offset(pageable.getOffset())
                .limit(12)
                .fetch();

        long total = queryFactory
                .select(Wildcard.count)
                .from(itemImg)
                .join(itemImg.item, item)
                .where(itemImg.repimgYn.eq("Y"))
                .where(itemNmLike(itemSearchDto.getSearchQuery()))
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<ResultSellingItemDto> getResultSellingItemDto(ItemSearchDto itemSearchDto, Pageable pageable) {
        QItem item = QItem.item;
        QItemImg itemImg = QItemImg.itemImg;

        List<ResultSellingItemDto> content = queryFactory
                .select(
                        new QResultSellingItemDto(
                                item.id,
                                item.itemNm,
                                item.category,
                                item.itemDetail,
                                itemImg.imgUrl,
                                item.price,
                                item.orderTotalCount,
                                item.orderTotalIncome)
                ).from(itemImg)
                .join(itemImg.item, item)
                .where(itemImg.repimgYn.eq("Y"))
                .where(itemNmLike(itemSearchDto.getSearchQuery()))
                .orderBy(item.orderTotalCount.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .select(Wildcard.count)
                .from(itemImg)
                .join(itemImg.item, item)
                .where(itemImg.repimgYn.eq("Y"))
                .where(itemNmLike(itemSearchDto.getSearchQuery()))
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<ResultCategoryItemDto> getResultCategoryItemDto(ItemSearchDto itemSearchDto, Pageable pageable) {
        QItem item = QItem.item;
        QItemImg itemImg = QItemImg.itemImg;

        List<ResultCategoryItemDto> content = queryFactory
                .select(
                        new QResultCategoryItemDto(
                                item.category,
                                item.orderTotalCount.sum(),
                                item.orderTotalIncome.sum())
                ).from(itemImg)
                .join(itemImg.item, item)
                .where(itemImg.repimgYn.eq("Y"))
                .where(itemNmLike(itemSearchDto.getSearchQuery()))
                .groupBy(item.category)
                .orderBy(item.orderTotalIncome.sum().desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .select(Wildcard.count)
                .from(itemImg)
                .join(itemImg.item, item)
                .where(itemImg.repimgYn.eq("Y"))
                .where(itemNmLike(itemSearchDto.getSearchQuery()))
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }

    public List<CategoryItemsDto> getCategoryItemIncome(){
            QItem item = QItem.item;

    List<CategoryItemsDto> content = queryFactory
            .select(
                    new QCategoryItemsDto(
                            item.category.stringValue(),
                            item.orderTotalIncome.sum())
            ).from(item)
            .groupBy(item.category)
            .orderBy(item.orderTotalIncome.sum().desc())
            .fetch();

        return content;
        }


//    public List<MainGraphDto> getMainGraphData(){
//        QOrderItem orderItem = QOrderItem.orderItem;
//
//        StringTemplate formattedDate = Expressions.stringTemplate(
//                "DATE_FORMAT({0}, {1})"
//                , orderItem.regTime
//                , ConstantImpl.create("%Y-%m"));
//        Projections.fields(MainGraphDto.class,
//                        formattedDate,
//
//        List<MainGraphDto> content = queryFactory
//                .select(
//                        new QMainGraphDto(
//                                orderItem.count.sum(),
//                                orderItem.orderPrice.sum(),
//                                orderItem.regTime.stringValue())
//                        ).from(orderItem)
//                                .groupBy(orderItem.regTime)
//                .orderBy(orderItem.regTime.desc())
//                .fetch();
//
//        return content;
//    }




}
