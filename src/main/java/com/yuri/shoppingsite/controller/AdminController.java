package com.yuri.shoppingsite.controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.yuri.shoppingsite.Repository.ItemRepository;
import com.yuri.shoppingsite.Repository.MemberRepository;
import com.yuri.shoppingsite.constant.Category;
import com.yuri.shoppingsite.domain.Chart.CategoryItemsInterface;
import com.yuri.shoppingsite.domain.Chart.MainGraphInterface;
import com.yuri.shoppingsite.domain.community.NoticeFormDto;
import com.yuri.shoppingsite.domain.shop.*;
import com.yuri.shoppingsite.domain.user.MemberSearchDto;
import com.yuri.shoppingsite.domain.user.Member;
import com.yuri.shoppingsite.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.security.Principal;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Controller
//@RestController
@RequiredArgsConstructor
public class AdminController {

    @Autowired
    private final ItemService itemService;
    @Autowired
    private final OrderService orderService;
    @Autowired
    private final MemberRepository memberRepository;
    @Autowired
    private final MemberService memberService;

    @Autowired
    ChartService chartService;
    //관리자 페이지로 가기
    @GetMapping(value={"/admin/adminmain", "/admin/adminmain/{year}"})
    public String adminmain(Model model){
        int memberCount = memberService.getMemberCount();
        int sellingCount = itemService.getSellingCount();
        int sellingIncome = itemService.getSellingIncome();
        List<OrderItem> sellingitems = orderService.recentselling();

        List<MainGraphInterface> mainGraph = itemService.getMainGraphDtos();
        System.out.println(mainGraph);

        Gson gson = new Gson();
        JsonArray jsonArray = new JsonArray();
        Iterator<MainGraphInterface> mg = mainGraph.iterator();
        while(mg.hasNext()){
            MainGraphInterface mainGraphDto = mg.next();
            JsonObject object = new JsonObject();
            int count = mainGraphDto.getCount();
            int revenue = mainGraphDto.getOrder_price();
            String date = mainGraphDto.getReg_time();

            System.out.println(count);
            System.out.println(revenue);

            object.addProperty("mgCount",count);
            object.addProperty("mgRevenue",revenue);
            object.addProperty("mgDate", String.valueOf(date));

            jsonArray.add(object);
        }

        String json = gson.toJson(jsonArray);
        System.out.println(json);
        model.addAttribute("json",json);

        model.addAttribute("memberCount",memberCount);
        model.addAttribute("sellingCount",sellingCount);
        model.addAttribute("sellingIncome",sellingIncome);
        model.addAttribute("sellingitems",sellingitems);
//        model.addAttribute("monthlySalesList",monthlySalesList);
        return "admin/adminmain";
    }

    //상품관리

    //상품관리 페이지 이동
    //value에 상품 관리 화면 진입시 URL에 페이지 번호가 없는 경우와 페이지 번호가 있는 경우 2가지를 매핑한다.
    @GetMapping(value = {"/admin/items","/admin/items/{page}"})
    public String itemManage(ItemSearchDto itemSearchDto, @PathVariable("page") Optional<Integer> page, Model model){

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 10);
        Page<Item> items = itemService.getAdminItemPage(itemSearchDto, pageable);

        model.addAttribute("items", items);
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("maxPage", 10);

        return "admin/productlist";
    }


    //상품 등록 페이지로 가기(insert)
    @GetMapping("admin/uploadproduct")
    public String itemForm(Model model) {
        model.addAttribute("itemFormDto", new ItemFormDto());
        model.addAttribute("pagestate", "등록");
        return "admin/uploadproduct";
    }

    //상품 상세 페이지로 가기
    @GetMapping("admin/item/{itemId}")
    public String itemDtl(@PathVariable("itemId") Long itemId, Model model){
        try {
            ItemFormDto itemFormDto = itemService.getItemDtl(itemId);
            model.addAttribute("itemFormDto",itemFormDto);
        } catch(EntityNotFoundException e){
            model.addAttribute("errorMessage","존재하지 않는 상품입니다.");
            model.addAttribute("itemFormDto", new ItemFormDto());
            model.addAttribute("pagestate", "상세");
            return "admin/uploadproduct";
        }
        return "admin/uploadproduct";
    }

    //상품 등록
    @PostMapping("admin/uploadproduct")
    public String itemNew(@Valid ItemFormDto itemFormDto, BindingResult bindingResult,
                           Model model, @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList){

        //상품등록시 필수 값이 없다면 다시 상품 등록 페이지로 전환
        if(bindingResult.hasErrors()){
            return "admin/uploadproduct";
        }
        //상품 등록시 첫번째 이미지가 없다면 에러메시지와 함께 상품 등록 페이지로 전환한다.
        //상품의 첫번째 이미지는 메인 페이지에서 보여줄 상품 이미지로 사용하기 위해 필수 값으로 지정한다.
        if(itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null){
            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력값 입니다.");
            return "admin/uploadproduct";
        }

        try {
            //상품 저장 로직을 호출한다. 매개변수로 상품정보와 상품 이미지 정보를 담고있는 itemImgFileList 넘겨줌
            itemService.saveItem(itemFormDto, itemImgFileList);
        } catch (Exception e){
            model.addAttribute("errorMessage","상품 등록 중 에러가 발생하였습니다.");
            return "admin/uploadproduct";
        }
        //정상적으로 저장되면 메인페이지로 이동
        return "redirect:/admin/items";
    }

    //상품 수정
    @PostMapping("admin/uploadproduct/{itemId}")
    public String itemUpdate(@Valid ItemFormDto itemFormDto,
                             BindingResult bindingResult,
                             @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList,
                             Model model){
        if(bindingResult.hasErrors()){
            return "admin/uploadproduct";
        }
        if(itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null){
            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력값입니다.");
            return "admin/uploadproduct";
        }
        try{
            itemService.updateItem(itemFormDto, itemImgFileList);
        } catch (Exception e){
            model.addAttribute("errorMessage", "상품 수정 중 에러가 발생하였습니다.");
            return "admin/uploadproduct";
        }
        return "redirect:/admin/items";
    }

    //상품 카테고리 관리페이지로 이동
    @GetMapping(value={"/admin/categorychange","/admin/categorychange/{page}"})
    public String categoryChange(ItemSearchDto itemSearchDto,Model model, @PathVariable("page") Optional<Integer> page){
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 10);
        Page<MainItemDto> categoryItem = itemService.getAdminCategoryPage(itemSearchDto, pageable);
        model.addAttribute("items",categoryItem);
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("maxPage", 10);
        return "admin/categorychange";
    }
    @Autowired
    ItemRepository itemRepository;
    //상품 카테고리 수정 업데이트
    @PutMapping(value="/admin/categorychange")
    public @ResponseBody ResponseEntity updateCategory(@PathVariable Long id,
                                                       @PathVariable Category category,
                                                       Principal principal){
//        if(!itemService.validateItem(id,principal.getName())){
//            return new ResponseEntity<String>("수정권한이 없습니다.", HttpStatus.FORBIDDEN);
//        }
        System.out.println(id);
        System.out.println(String.valueOf(category));
        itemRepository.updateCategory(id, String.valueOf(category));
        return new ResponseEntity<Long>(id,HttpStatus.OK);
    }


    //상품 판매상태 관리 페이지로 이동
    @GetMapping(value={"/admin/sellingstatechange","/admin/sellingstatechange/{page}"})
    public String sellingStateChange(ItemSearchDto itemSearchDto,@PathVariable("page") Optional<Integer> page, Model model){
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 10);
        Page<MainItemDto> sellingStateChange = itemService.getAllMain(itemSearchDto, pageable);
        model.addAttribute("items",sellingStateChange);
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("maxPage", 10);
        return "admin/sellingstatechange";
    }

    //기본 정보 관리

    //자사 정보 관리

    //유저 권한 관리
    //유저 권한 관리 페이지로 이동

    @GetMapping(value = {"/admin/userauth","/admin/userauth/{page}"})
    public String userAuth(MemberSearchDto memberSearchDto, @PathVariable("page") Optional<Integer> page, Model model){

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 10);
        Page<Member> members = memberService.getMemberAuth(memberSearchDto,pageable);
        model.addAttribute("members", members);
        model.addAttribute("memberSearchDto", memberSearchDto);
        model.addAttribute("maxPage", 10);

        return "admin/userauthority";
    }

//    //유저 권한 변경
//    @PatchMapping(value="/admin/member/{id}")
//    public @ResponseBody ResponseEntity updateMemberAuth(@PathVariable("id") Long id,
//                                                       Principal principal){
        //현재 로그인한 회원의 role 가져와서 ADMIN인지 체크
//        if(!memberService.validateMember(principal.getName())){
//            return new ResponseEntity<String>("수정 권한이 없습니다.", HttpStatus.FORBIDDEN);
//        }

//        //장바구니 상품의 개수 업데이트
//        memberService.updateMemberRole(cartItemId, count);
//        return new ResponseEntity<Long>(cartItemId,HttpStatus.OK);
//    }
//

    //유저 삭제
    @DeleteMapping(value = "/admin/member/{id}")
    public @ResponseBody ResponseEntity deleteMember(
            @PathVariable("id") Long id, Principal principal){
//        //삭제 권한 체크
        if(!memberService.validateMember(principal.getName())){
            return new ResponseEntity<String>("삭제 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }
        //멤버 삭제
        memberService.deleteMember(id);
        return new ResponseEntity<Long>(id, HttpStatus.OK);
    }


    //재고관리
    //재고현황페이지 가기
    @GetMapping(value={"/admin/stocknow","/admin/stocknow/{page}"})
    public String stocknow(Model model, @PathVariable("page") Optional<Integer> page,
                           ItemSearchDto itemSearchDto){
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 10);
        Page<MainItemDto> sellingStateChange = itemService.getAllMain(itemSearchDto, pageable);
        model.addAttribute("items",sellingStateChange);
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("maxPage", 10);
        return "admin/stocknow";
    }

    //재고변경 페이지 가기
    @GetMapping(value={"/admin/stockupdate","/admin/stockupdate/{page}"})
    public String stockUpdatePage(Model model, @PathVariable("page") Optional<Integer> page,
                           ItemSearchDto itemSearchDto){
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 10);
        Page<MainItemDto> sellingStateChange = itemService.getAllMain(itemSearchDto, pageable);
        model.addAttribute("items",sellingStateChange);
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("maxPage", 10);
        return "admin/stockupdate";
    }

    //재고변경 하기
    @PutMapping(value="/admin/stockupdate/{id}")
    public @ResponseBody ResponseEntity updateStock(
            @PathVariable("id") Long id, Principal principal, int stockNumber){

        if(!itemService.validateItem(id,principal.getName())){
            return new ResponseEntity<String>("수정 권한이 업습니다.", HttpStatus.FORBIDDEN);
        }
        itemService.updatestock(id,stockNumber);

        return new ResponseEntity<Long>(id,HttpStatus.OK);
    }

    //통계보기

    //전체 통계 페이지 가기
    @GetMapping("admin/totalresult")
        public String totalResult(Model model){
        List<CategoryItemsInterface> categoryitems = itemService.getCategoryContent();
        System.out.println(categoryitems);
        Gson gson = new Gson();
        JsonArray jsonArray = new JsonArray();

        Iterator<CategoryItemsInterface> cid = categoryitems.iterator();
        while(cid.hasNext()){
            CategoryItemsInterface categoryItemsInterface = cid.next();
            JsonObject object = new JsonObject();
            Category category = categoryItemsInterface.getCategory();
            int totalIncome = categoryItemsInterface.getTotalIncome();

            object.addProperty("Category", String.valueOf(category));
            object.addProperty("Income",totalIncome);
            jsonArray.add(object);
        }

        String json = gson.toJson(jsonArray);
        System.out.println(json);
        model.addAttribute("json",json);

        model.addAttribute("categoryitems",categoryitems);
        return "admin/totalresult";
}

    //카테고리별 통계 페이지 가기
    @GetMapping(value={"admin/categoryselling", "/admin/admin/categoryselling/{page}"})
    public String categoryselling(ItemSearchDto itemSearchDto, Optional<Integer> page, Model model){
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 10);
        Page<ResultCategoryItemDto> items = itemService.getResultCategoryItemDto(itemSearchDto, pageable);
        model.addAttribute("items",items);
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("maxPage", 1);
        return "admin/categorysellingresult";
    }

    //상품별 통계 페이지 가기
    @GetMapping(value={"/admin/productselling", "/admin/productselling/{page}"})
    public String productselling(ItemSearchDto itemSearchDto, Optional<Integer> page, Model model){
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 10);
        Page<ResultSellingItemDto> items = itemService.getResultSellingItemPage(itemSearchDto, pageable);
        model.addAttribute("items",items);
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("maxPage", 1);
        return "admin/productsellingresult";
    }
    NoticeService noticeService;

    //커뮤니티
    //공지사항 목록보기
    @GetMapping("admin/noticelist")
    public String adminnoticelist(Model model){
        model.addAttribute("noticeList",noticeService.getNotice());
        return "admin/adminnoticelist";
    }

    //공지사항 등록하기
    @GetMapping("admin/noticeenroll")
    public String adminnoticeenroll(Model model, NoticeFormDto noticeFormDto){
        model.addAttribute("noticeFormDto", noticeFormDto);
    return "admin/adminNoticeEnroll";
    }




    }
