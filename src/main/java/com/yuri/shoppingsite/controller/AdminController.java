package com.yuri.shoppingsite.controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.yuri.shoppingsite.Repository.CompanyRepository;
import com.yuri.shoppingsite.Repository.ItemRepository;
import com.yuri.shoppingsite.constant.Category;
import com.yuri.shoppingsite.constant.ItemSellStatus;
import com.yuri.shoppingsite.constant.Role;
import com.yuri.shoppingsite.domain.chart.CategoryItemsInterface;
import com.yuri.shoppingsite.domain.chart.MainGraphInterface;
import com.yuri.shoppingsite.domain.community.Board;
import com.yuri.shoppingsite.domain.community.NoticeFormDto;
import com.yuri.shoppingsite.domain.company.Company;
import com.yuri.shoppingsite.domain.company.CompanyFormDto;
import com.yuri.shoppingsite.domain.shop.*;
import com.yuri.shoppingsite.domain.user.MemberSearchDto;
import com.yuri.shoppingsite.domain.user.Member;
import com.yuri.shoppingsite.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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

    private final ItemService itemService;
    private final OrderService orderService;
    private final MemberService memberService;
    @Autowired
    CompanyService companyService;
    @Autowired
    BoardService boardService;

    //관리자 페이지로 가기
    @GetMapping(value = {"/admin/adminmain", "/admin/adminmain/{year}"})
    public String adminmain(Model model) {
        int memberCount = memberService.getMemberCount();
        int sellingCount = itemService.getSellingCount();
        int sellingIncome = itemService.getSellingIncome();
        List<OrderItem> sellingitems = orderService.recentselling();
        List<MainGraphInterface> mainGraph = itemService.getMainGraphDtos();
        System.out.println(mainGraph);

        Gson gson = new Gson();
        JsonArray jsonArray = new JsonArray();
        Iterator<MainGraphInterface> mg = mainGraph.iterator();
        while (mg.hasNext()) {
            MainGraphInterface mainGraphDto = mg.next();
            JsonObject object = new JsonObject();
            int count = mainGraphDto.getCount();
            int revenue = mainGraphDto.getOrder_price();
            String date = mainGraphDto.getReg_time();

            System.out.println(count);
            System.out.println(revenue);

            object.addProperty("mgCount", count);
            object.addProperty("mgRevenue", revenue);
            object.addProperty("mgDate", String.valueOf(date));

            jsonArray.add(object);
        }

        String json = gson.toJson(jsonArray);
        System.out.println(json);
        model.addAttribute("json", json);

        model.addAttribute("memberCount", memberCount);
        model.addAttribute("sellingCount", sellingCount);
        model.addAttribute("sellingIncome", sellingIncome);
        model.addAttribute("sellingitems", sellingitems);
        return "admin/adminmain";
    }

    //상품관리

    //상품관리 페이지 이동
    //value에 상품 관리 화면 진입시 URL에 페이지 번호가 없는 경우와 페이지 번호가 있는 경우 2가지를 매핑한다.
    @GetMapping(value = {"/admin/items", "/admin/items/{page}"})
    public String itemManage(ItemSearchDto itemSearchDto, @PathVariable("page") Optional<Integer> page, Model model) {

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
    public String itemDtl(@PathVariable("itemId") Long itemId, Model model) {
        try {
            ItemFormDto itemFormDto = itemService.getItemDtl(itemId);
            model.addAttribute("itemFormDto", itemFormDto);
        } catch (EntityNotFoundException e) {
            model.addAttribute("errorMessage", "존재하지 않는 상품입니다.");
            model.addAttribute("itemFormDto", new ItemFormDto());
            model.addAttribute("pagestate", "상세");
            return "admin/uploadproduct";
        }
        return "admin/uploadproduct";
    }

    //상품 등록
    @PostMapping("admin/uploadproduct")
    public String itemNew(@Valid ItemFormDto itemFormDto, BindingResult bindingResult,
                          Model model, @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList) {

        //상품등록시 필수 값이 없다면 다시 상품 등록 페이지로 전환
        if (bindingResult.hasErrors()) {
            return "admin/uploadproduct";
        }
        //상품 등록시 첫번째 이미지가 없다면 에러메시지와 함께 상품 등록 페이지로 전환한다.
        //상품의 첫번째 이미지는 메인 페이지에서 보여줄 상품 이미지로 사용하기 위해 필수 값으로 지정한다.
        if (itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null) {
            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력값 입니다.");
            return "admin/uploadproduct";
        }

        try {
            //상품 저장 로직을 호출한다. 매개변수로 상품정보와 상품 이미지 정보를 담고있는 itemImgFileList 넘겨줌
            itemService.saveItem(itemFormDto, itemImgFileList);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "상품 등록 중 에러가 발생하였습니다.");
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
                             Model model) {
        if (bindingResult.hasErrors()) {
            return "admin/uploadproduct";
        }
        if (itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null) {
            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력값입니다.");
            return "admin/uploadproduct";
        }
        try {
            itemService.updateItem(itemFormDto, itemImgFileList);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "상품 수정 중 에러가 발생하였습니다.");
            return "admin/uploadproduct";
        }
        return "redirect:/admin/items";
    }

    //상품 카테고리 관리페이지로 이동
    @GetMapping(value = {"/admin/categorychange", "/admin/categorychange/{page}"})
    public String categoryChange(ItemSearchDto itemSearchDto, Model model, @PathVariable("page") Optional<Integer> page) {
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 10);
        Page<MainItemDto> categoryItem = itemService.getAdminCategoryPage(itemSearchDto, pageable);
        model.addAttribute("items", categoryItem);
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("maxPage", 10);
        return "admin/categorychange";
    }

    //상품 카테고리 수정 업데이트
    @PutMapping(value = "/admin/categorychange/modify/{id}")
    public @ResponseBody ResponseEntity updateCategory(@PathVariable Long id,
                                                       String category,
                                                       Principal principal) {
        System.out.println(id);
        System.out.println(category);
        itemService.updateCategory(id, Category.valueOf(category));
        return new ResponseEntity<Long>(id, HttpStatus.OK);
    }


    //상품 판매상태 관리 페이지로 이동
    @GetMapping(value = {"/admin/sellingstatechange", "/admin/sellingstatechange/{page}"})
    public String sellingStateChange(ItemSearchDto itemSearchDto, @PathVariable("page") Optional<Integer> page, Model model) {
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 10);
        Page<MainItemDto> sellingStateChange = itemService.getAllMain(itemSearchDto, pageable);
        model.addAttribute("items", sellingStateChange);
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("maxPage", 10);
        return "admin/sellingstatechange";
    }

    //상품 판매상태 변경
    @PutMapping(value = "/admin/sellingstatechange/modify/{id}")
    public @ResponseBody ResponseEntity updateItemSellStatus(@PathVariable Long id,
                                                             String itemSellStatus,
                                                             Principal principal) {
//        if(!itemService.validateItem(id,principal.getName())){
//            return new ResponseEntity<String>("수정권한이 없습니다.", HttpStatus.FORBIDDEN);
//        }
        System.out.println(id);
        System.out.println(itemSellStatus);
        itemService.updateItemSellStatus(id, ItemSellStatus.valueOf(itemSellStatus));
        return new ResponseEntity<Long>(id, HttpStatus.OK);
    }

    //기본 정보 관리

    //자사 정보 관리(상세/등록폼)
    @GetMapping(value = {"/admin/companyinfo",})
    public String companyinfo(Model model) {
        if (companyService.getcompanyList() != null) {
            try {
                CompanyFormDto companyFormDto = companyService.findbyFirstId();
                model.addAttribute("companyFormDto", companyFormDto);
            } catch (EntityNotFoundException e) {
                model.addAttribute("errorMessage", "회사 정보가 없습니다.");
                model.addAttribute("companyFormDto", new CompanyFormDto());
                model.addAttribute("pagestate", "상세");
            }
        } else if (companyService.getcompanyList() == null) {
            model.addAttribute("companyFormDto", new CompanyFormDto());
            model.addAttribute("pagestate", "등록");
        }

        return "/admin/companyinfo";
    }


    //자사 정보 등록하기
    @PostMapping("admin/enrollcompany")
    public String enrollcompany(@Valid CompanyFormDto companyFormDto,
                                BindingResult bindingResult,
                                Model model) {
        if (bindingResult.hasErrors()) {
            return "admin/companyinfo";
        }
        try {
            //상품 저장 로직을 호출한다. 매개변수로 상품정보와 상품 이미지 정보를 담고있는 itemImgFileList 넘겨줌
            Company company = Company.createCompany(companyFormDto);
            companyService.saveCompany(company);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "회사 정보 등록 중 에러가 발생하였습니다.");
            return "admin/companyinfo";
        }

        return "redirect:/admin/companyinfo";
    }

    //자사 정보 수정하기
    @PostMapping("admin/updatecompany/{id}")
    public String updateCompany(@Valid CompanyFormDto companyFormDto,
                                BindingResult bindingResult,
                                Model model, @PathVariable Long id) throws Exception {
        System.out.println("수정하기 폼 오남");
        if (bindingResult.hasErrors()) {
            return "admin/companyinfo";
        }
        try {
            companyService.updateCompany(companyFormDto);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "상품 수정 중 에러가 발생하였습니다.");
            return "admin/uploadproduct";
        }

        return "redirect:/admin/companyinfo";
    }

    //유저 권한 관리
    //유저 권한 관리 페이지로 이동

    @GetMapping(value = {"/admin/userauth", "/admin/userauth/{page}"})
    public String userAuth(MemberSearchDto memberSearchDto, @PathVariable("page") Optional<Integer> page, Model model) {

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 10);
        Page<Member> members = memberService.getMemberAuth(memberSearchDto, pageable);
        model.addAttribute("members", members);
        model.addAttribute("memberSearchDto", memberSearchDto);
        model.addAttribute("maxPage", 10);

        return "admin/userauthority";
    }

    //    //유저 권한 변경
    @PutMapping(value = "/admin/userauth/modify/{id}")
    public @ResponseBody ResponseEntity updateUserRole(@PathVariable Long id,
                                                       String role,
                                                       Principal principal) {
        System.out.println(id);
        System.out.println(role);
        memberService.updateUserRole(id, Role.valueOf(role));
        return new ResponseEntity<Long>(id, HttpStatus.OK);
    }

    //유저 삭제
    @DeleteMapping(value = "/admin/member/{id}")
    public @ResponseBody ResponseEntity deleteMember(
            @PathVariable("id") Long id, Principal principal) {
//        //삭제 권한 체크
        if (!memberService.validateMember(principal.getName())) {
            return new ResponseEntity<String>("삭제 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }
        //멤버 삭제
        memberService.deleteMember(id);
        return new ResponseEntity<Long>(id, HttpStatus.OK);
    }


    //재고관리
    //재고현황페이지 가기
    @GetMapping(value = {"/admin/stocknow", "/admin/stocknow/{page}"})
    public String stocknow(Model model, @PathVariable("page") Optional<Integer> page,
                           ItemSearchDto itemSearchDto) {
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 10);
        Page<MainItemDto> sellingStateChange = itemService.getAllMain(itemSearchDto, pageable);
        model.addAttribute("items", sellingStateChange);
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("maxPage", 10);
        return "admin/stocknow";
    }

    //재고변경 페이지 가기
    @GetMapping(value = {"/admin/stockupdate", "/admin/stockupdate/{page}"})
    public String stockUpdatePage(Model model, @PathVariable("page") Optional<Integer> page,
                                  ItemSearchDto itemSearchDto) {
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 10);
        Page<MainItemDto> sellingStateChange = itemService.getAllMain(itemSearchDto, pageable);
        model.addAttribute("items", sellingStateChange);
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("maxPage", 10);
        return "admin/stockupdate";
    }

    Item item;
    ItemRepository itemRepository;

    //재고변경 하기
    @PatchMapping(value = "/admin/stockupdate/modify/{id}")
    public @ResponseBody ResponseEntity updateStock(@PathVariable Long id,
                                                    Integer addStock, Integer stockNumber,
                                                    Principal principal) {
        System.out.println(id);
        System.out.println(addStock);

//        stockNumber = itemRepository.findStockById(id);
//        itemService.updatestock(id,addStock, stockNumber);
//
        return new ResponseEntity<Long>(id, HttpStatus.OK);
    }

    //통계보기

    //전체 통계 페이지 가기
    @GetMapping("admin/totalresult")
    public String totalResult(Model model) {
        //매출통계
        List<OrderItem> sellingitems = orderService.recentselling();
        List<MainGraphInterface> mainGraph = itemService.getMainGraphDtos();
        System.out.println(mainGraph);

        Gson gson = new Gson();
        JsonArray jsonArray = new JsonArray();
        Iterator<MainGraphInterface> mg = mainGraph.iterator();
        while (mg.hasNext()) {
            MainGraphInterface mainGraphDto = mg.next();
            JsonObject object = new JsonObject();
            int count = mainGraphDto.getCount();
            int revenue = mainGraphDto.getOrder_price();
            String date = mainGraphDto.getReg_time();

            System.out.println(count);
            System.out.println(revenue);

            object.addProperty("mgCount", count);
            object.addProperty("mgRevenue", revenue);
            object.addProperty("mgDate", String.valueOf(date));

            jsonArray.add(object);
        }
        String json = gson.toJson(jsonArray);
        System.out.println(json);
        model.addAttribute("json", json);
        model.addAttribute("sellingitems", sellingitems);


        List<CategoryItemsInterface> categoryitems = itemService.getCategoryContent();
//        System.out.println(categoryitems);
        JsonArray jsonArray1 = new JsonArray();

        Iterator<CategoryItemsInterface> cid = categoryitems.iterator();
        while (cid.hasNext()) {
            CategoryItemsInterface categoryItemsInterface = cid.next();
            JsonObject object1 = new JsonObject();
            Category category = categoryItemsInterface.getCategory();
            int totalIncome = categoryItemsInterface.getTotalIncome();

            object1.addProperty("Category", String.valueOf(category));
            object1.addProperty("Income", totalIncome);
            jsonArray1.add(object1);
        }

        String json1 = gson.toJson(jsonArray1);
        System.out.println(json1);
        model.addAttribute("json1", json1);
        return "admin/totalresult";
    }

    //카테고리별 통계 페이지 가기
    @GetMapping(value = {"admin/categoryselling", "/admin/admin/categoryselling/{page}"})
    public String categoryselling(ItemSearchDto itemSearchDto, Optional<Integer> page, Model model) {
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 10);
        Page<ResultCategoryItemDto> items = itemService.getResultCategoryItemDto(itemSearchDto, pageable);
        model.addAttribute("items", items);
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("maxPage", 1);
        return "admin/categorysellingresult";
    }

    //상품별 통계 페이지 가기
    @GetMapping(value = {"/admin/productselling", "/admin/productselling/{page}"})
    public String productselling(ItemSearchDto itemSearchDto, Optional<Integer> page, Model model) {
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 10);
        Page<ResultSellingItemDto> items = itemService.getResultSellingItemPage(itemSearchDto, pageable);
        model.addAttribute("items", items);
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("maxPage", 1);
        return "admin/productsellingresult";
    }

    private final CompanyRepository companyRepository;

    //커뮤니티
    //공지사항 목록보기
    @GetMapping("admin/noticelist")
    public String adminnoticelist(Model model) {
//        if(boardService == null) {
//            boardService = new BoardService(); // boardService 객체 생성
//        } else if(boardService != null){
        List<Board> boardList = boardService.getNotice();
        model.addAttribute("boardList", boardList);
//        }
        return "admin/adminnoticelist";
    }


    //공지사항 등록하기 가기 페이지
    @GetMapping("admin/noticeenroll")
    public String adminnoticeenroll(Model model, NoticeFormDto noticeFormDto) {
        model.addAttribute("noticeFormDto", noticeFormDto);
        return "admin/adminnoticeenroll";
    }

    @PostMapping("admin/adminnoticeenroll")
    public String uploadnotice(@Valid NoticeFormDto noticeFormDto,
                               BindingResult bindingResult,
                               Model model) {
            if (noticeFormDto.getType() == null) {
                // type 필드가 null인 경우 처리할 로직
            } else {
                // type 필드가 null이 아닌 경우 처리할 로직
            }
            boardService.createNotice(noticeFormDto);
        return "redirect:/admin/noticelist";
    }







}