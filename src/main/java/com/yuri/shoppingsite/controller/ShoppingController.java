package com.yuri.shoppingsite.controller;

import com.yuri.shoppingsite.domain.shop.*;
import com.yuri.shoppingsite.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ShoppingController {
    @Autowired
    private ItemService itemService;

    //상품 전체 보여주기 리스트로 가기
    @GetMapping(value="shopping/shopping")
    public String main(ItemSearchDto itemSearchDto, Optional<Integer> page, Model model){

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 6);
        Page<MainItemDto> items = itemService.getMainItemPage(itemSearchDto, pageable);

        model.addAttribute("items", items);
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("maxPage", 5);
        return "shopping/shopping";
    }

    //상품 상세보기로 가기
    @GetMapping(value="/item/{itemId}")
    public String itemDtl(Model model, @PathVariable("itemId") Long itemId){
        ItemFormDto itemFormDto = itemService.getItemDtl(itemId);
        model.addAttribute("item", itemFormDto);
        return "shopping/itemDtl";
    }


    //베스트셀러 페이지로 이동
    @GetMapping(value="shopping/bestseller")
    public String bestSeller(ItemSearchDto itemSearchDto, Optional<Integer> page, Model model){

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 12);
        Page<BestSellerItemDto> items = itemService.getBestSellerItemPage(itemSearchDto, pageable);

        model.addAttribute("items", items);
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("maxPage", 1);
        return "shopping/bestseller";
    }

    //최신상품 페이지로 이동
    @GetMapping(value="shopping/latest")
    public String latest(ItemSearchDto itemSearchDto, Optional<Integer> page, Model model){

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 12);
        Page<LatestItemDto> items = itemService.getLatestItemPage(itemSearchDto, pageable);

        model.addAttribute("items", items);
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("maxPage", 1);
        return "shopping/latest";
    }




//    @GetMapping("shopping/test")
//    public void gotest(Model model){
//        ItemDTO itemDTO = new ItemDTO();
//        itemDTO.setItemNm("테스트상품1");
//        itemDTO.setItemDetail("상품 상세 설명");
//        itemDTO.setPrice(10000);
//        itemDTO.setRegTime(LocalDateTime.now());
//
//        model.addAttribute("itemDTO",itemDTO);
//
//        List<ItemDTO> itemDTOList = new ArrayList<>();
//        for(int i=1; i<10; i++){
//            ItemDTO itemDTO1 = new ItemDTO();
//            itemDTO1.setItemDetail("상품 상세 설명");
//            itemDTO1.setItemNm("테스트상품"+i);
//            itemDTO1.setPrice(1000*i);
//            itemDTO1.setRegTime(LocalDateTime.now());
//
//            itemDTOList.add(itemDTO1);
//            model.addAttribute("itemDtoList",itemDTOList);
//        }
//    }
//
//    @GetMapping("shopping/test2")
//    public void test2(String param1, String param2, Model model) {
//        model.addAttribute("param1",param1);
//        model.addAttribute("param2",param2);
//    }


}