package com.yuri.shoppingsite.service;

import com.yuri.shoppingsite.Repository.ItemImgRepository;
import com.yuri.shoppingsite.Repository.ItemRepository;
import com.yuri.shoppingsite.domain.shop.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemImgService itemImgService;
    private final ItemImgRepository itemImgRepository;

    public Long saveItem(ItemFormDto itemFormDto,
                         List<MultipartFile> itemImgFileList) throws Exception {
        //상품등록
        //상품등록 폼으로 부터 입력받은 데이터를 이용하여 item 객체를 생성한다.ㅣ
        Item item = itemFormDto.createItem();
        //상품 데이터를 저장한다.
        itemRepository.save(item);

        //이미지 등록
        for(int i=0; i<itemImgFileList.size(); i++){
            ItemImg itemImg = new ItemImg();
            itemImg.setItem(item);
            if(i==0){ //첫번째 이미지일 경우 대표 상품 이미지 여부값을 "Y"로 셋팅한다.
                itemImg.setRepimgYn("Y");
            } else { //나머지 상품 이미지는 "N"으로 설정한다
                itemImg.setRepimgYn("N");
            }
            //상품 이미지 정보를 저장한다.
            itemImgService.saveItemImg(itemImg, itemImgFileList.get(i));
        }
        return item.getId();
    }

    //상품 데이터를 읽어오는 트랜잭션을 읽기 전용으로 설정한다.
    //이럴 경우 JPA가 더티체킹(변경감지)을 수행하지 않아서 성능을 향상 시킬 수 있다.
    @Transactional(readOnly = true)
    public ItemFormDto getItemDtl(Long itemId){
        List<ItemImg> itemImgList =
                //해당상품의 이미지를 조회한다. 등록순으로 가지고 오기 위해 상품 이미지 아이디 오름차순으로 가지고 오겠다.
                itemImgRepository.findByItemIdOrderByIdAsc(itemId);
        List<ItemImgDto> itemImgDtoList = new ArrayList<>();
        //조회한 Itemimg 엔티티를 ItemImgDto 객체로 만들어서 리스트에 추가한다.
        for(ItemImg itemImg : itemImgList){
            ItemImgDto itemImgDto = ItemImgDto.of(itemImg);
            itemImgDtoList.add(itemImgDto);
        }
        //상품의 아이디를 통해 상품 엔티티를 조회한다. 존재하지 않으면 예외 발생
        Item item = itemRepository.findById(itemId)
                .orElseThrow(EntityNotFoundException::new);
        ItemFormDto itemFormDto = ItemFormDto.of(item);
        itemFormDto.setItemImgDtoList(itemImgDtoList);
        return itemFormDto;
    }

    public Long updateItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception{
        //상품수정
        //상품 등록 화면으로부터 전달 받은 상품 아이디를 이용하여 상품 엔티티를 조회한다.
        Item item = itemRepository.findById(itemFormDto.getId())
                .orElseThrow(EntityNotFoundException::new);
                //상품 등록 화면으로부터 전달받은 ItemFormDto를 통해 상품 엔티티를 업데이트한다.
        item.updateItem(itemFormDto);
        //상품 이미지 아이디 리스트를 조회한다.
        List<Long> itemImgIds = itemFormDto.getItemImgIds();
        //이미지 등록
        for(int i=0; i< itemImgFileList.size(); i++){
            //상품 이미지를 업데이트하기 위해 updateItemImg() 메소드에 상품 이미지 아이디와, 상품 이미지 파일정보를
            //파라미터로 전달함
            itemImgService.updateItemImg(itemImgIds.get(i), itemImgFileList.get(i));
        }
    return item.getId();
    }

    @Transactional(readOnly = true)
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable){
        return itemRepository.getAdminItemPage(itemSearchDto, pageable);
    }

    @Transactional(readOnly = true)
    public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable){
        return itemRepository.getMainItemPage(itemSearchDto, pageable);
    }

    @Transactional(readOnly = true)
    public Page<BestSellerItemDto> getBestSellerItemPage(ItemSearchDto itemSearchDto, Pageable pageable){
        return itemRepository.getBestSellerItemPage(itemSearchDto, pageable);
    }

    @Transactional(readOnly = true)
    public Page<LatestItemDto> getLatestItemPage(ItemSearchDto itemSearchDto, Pageable pageable){
        return itemRepository.getLatestItemDto(itemSearchDto, pageable);
    }

    @Transactional(readOnly = true)
    public Page<ResultSellingItemDto> getResultSellingItemPage(ItemSearchDto itemSearchDto, Pageable pageable){
        return itemRepository.getResultSellingItemDto(itemSearchDto, pageable);
    }

    //총 아이템의주문 횟수를 가져옴
    public int getSellingCount(){
        int sellingCount = itemRepository.sellingCount();
        return sellingCount;
    }

    //총 아이템의주문 금액합을 가져옴
    public int getSellingIncome(){
        int sellingIncome = itemRepository.sellingIncome();
        return sellingIncome;
    }



}