package com.yuri.shoppingsite.Repository;

import com.yuri.shoppingsite.domain.shop.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ItemRepositoryCustom {

    Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable);

    Page<MainItemDto> getALLMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable);
    Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable);
    Page<MainItemDto> getDiaryItemPage(ItemSearchDto itemSearchDto, Pageable pageable);
    Page<MainItemDto> getWallDecoItemPage(ItemSearchDto itemSearchDto, Pageable pageable);
    Page<MainItemDto> getPenItemPage(ItemSearchDto itemSearchDto, Pageable pageable);
    Page<MainItemDto> getLivingItemPage(ItemSearchDto itemSearchDto, Pageable pageable);
    Page<MainItemDto> getCardItemPage(ItemSearchDto itemSearchDto, Pageable pageable);
    Page<MainItemDto> getAccItemPage(ItemSearchDto itemSearchDto, Pageable pageable);

    Page<BestSellerItemDto> getBestSellerItemPage(ItemSearchDto itemSearchDto, Pageable pageable);

    Page<LatestItemDto> getLatestItemDto(ItemSearchDto itemSearchDto, Pageable pageable);

    Page<ResultSellingItemDto> getResultSellingItemDto(ItemSearchDto itemSearchDto,Pageable pageable);

    Page<ResultCategoryItemDto> getResultCategoryItemDto(ItemSearchDto itemSearchDto, Pageable pageable);
    List<CategoryItemsDto> getCategoryItemIncome();

}
