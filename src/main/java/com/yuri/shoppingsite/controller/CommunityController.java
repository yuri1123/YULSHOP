package com.yuri.shoppingsite.controller;

import com.yuri.shoppingsite.Repository.AnswerRepository;
import com.yuri.shoppingsite.Repository.ItemRepositoryCustom;
import com.yuri.shoppingsite.domain.community.*;
import com.yuri.shoppingsite.domain.company.Company;
import com.yuri.shoppingsite.domain.shop.ItemFormDto;
import com.yuri.shoppingsite.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Controller
public class CommunityController {

    private final BoardService boardService;
    private final CompanyService companyService;
    private final ItemService itemService;
    private final AnswerRepository answerRepository;
    //community - notice

    //공지사항 리스트 페이지로 이동
    @GetMapping(value = {"community/noticelist","community/noticelist/{page}"})
    public String goNotice(Model model, CommunitySearchDto communitySearchDto,
                           @PathVariable("page") Optional<Integer> page){
        System.out.println("notice 리스트로 이동하기");
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 10);
        Page<Board> boardList = boardService.getNoticeBoardList(communitySearchDto, pageable);
        model.addAttribute("boardList", boardList);
        model.addAttribute("communitySearchDto", communitySearchDto);
        model.addAttribute("maxPage", 10);

        List<Company> companyList = companyService.getcompanyList();
        Company company = companyList.get(0);
        System.out.println(company);
        model.addAttribute("company",company);
    return "community/noticelist";
    }

    //공지사항 상세보기
    @GetMapping("community/noticedtl/{id}")
    public String noticedtl(@PathVariable("id") Long id, Model model){
        boardService.updateView(id);
        Board board = boardService.getNoticeById(id);
        model.addAttribute("board",board);
        List<Company> companyList = companyService.getcompanyList();
        Company company = companyList.get(0);
        model.addAttribute("company",company);
        return "community/noticedtl";
    }


    //리뷰게시판 리스트 페이지로 이동
    @GetMapping(value = {"community/reviewlist","community/reviewlist/{page}"})
    public String goReview(Model model, CommunitySearchDto communitySearchDto,
                           @PathVariable("page") Optional<Integer> page){
        System.out.println("review 리스트로 이동하기");
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 10);
        Page<Board> boardList = boardService.getReviewBoardList(communitySearchDto, pageable);
        model.addAttribute("boardList", boardList);
        model.addAttribute("communitySearchDto", communitySearchDto);
        model.addAttribute("maxPage", 10);

        List<Company> companyList = companyService.getcompanyList();
        Company company = companyList.get(0);
        model.addAttribute("company",company);
        return "community/reviewlist";
    }

    //리뷰게시글 상세보기
    @GetMapping("community/reviewdtl/{id}")
    public String reviewdtl(@PathVariable("id") Long id, Model model,
                            Principal principal){
        boardService.updateView(id);
        Board board = boardService.getReviewById(id);
        model.addAttribute("board",board);

        List<Company> companyList = companyService.getcompanyList();
        Company company = companyList.get(0);
        model.addAttribute("company",company);
        return "community/reviewdtl";
    }

    //리뷰게시글 등록하기 폼 가기
    @GetMapping("community/reviewcreate")
    public String reviewcreate(Model model, ReviewFormDto reviewFormDto) {
        model.addAttribute("reviewFormDto", reviewFormDto);
        model.addAttribute("pagestate","등록");
        //아이템이름 가져오기
        List<String> items = itemService.getItemNames();
        model.addAttribute("items", items);
        System.out.println(items);

        List<Company> companyList = companyService.getcompanyList();
        Company company = companyList.get(0);
        System.out.println(company);
        model.addAttribute("company",company);
        return "community/reviewcreate";
    }

    //리뷰게시글 등록하기
    @PostMapping("community/reviewcreate")
    public String reviewcreate(@Valid ReviewFormDto reviewFormDto,
                               BindingResult bindingResult,
                               Model model) {
        if (bindingResult.hasErrors()) {
            return "community/reviewcreate";
        }
        try {
            //상품 저장 로직을 호출한다. 매개변수로 상품정보와 상품 이미지 정보를 담고있는 itemImgFileList 넘겨줌
            boardService.createReview(reviewFormDto);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "게시물 등록 중 에러가 발생하였습니다.");
            return "community/reviewcreate" ;
        }
        return "redirect:/community/reviewlist";
    }

    //리뷰 수정폼 가기
    @GetMapping("community/reviewupdate/{id}")
    public String goreviewupdate(@PathVariable("id") Long id, Model model){

        try {
            ReviewFormDto reviewFormDto = boardService.getReviewFormbyId(id);
            model.addAttribute("reviewFormDto", reviewFormDto);
            model.addAttribute("pagestate","수정");
            //아이템이름 가져오기
            List<String> items = itemService.getItemNames();
            model.addAttribute("items", items);
            List<Company> companyList = companyService.getcompanyList();
            Company company = companyList.get(0);
            System.out.println(company);
            model.addAttribute("company",company);
        } catch (EntityNotFoundException e){
            model.addAttribute("errorMessage","존재하지 않는 글입니다.");
            model.addAttribute("reviewFormDto",new ReviewFormDto());
            return "community/reviewdtl";
        }
        return "community/reviewcreate";
    }


    //리뷰게시글 수정하기
    @PostMapping("community/reviewupdate/{id}")
    public String reviewupdate(@PathVariable("id") Long id, Model model, ReviewFormDto reviewFormDto,
                               BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "community/reviewupdate";
        }
        try {
            boardService.updateReview(reviewFormDto);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "상품 수정 중 에러가 발생하였습니다.");
            return "community/reviewupdate";
        }
        return "redirect:/community/reviewlist";
    }

    //Q&A 게시판 페이지로 이동
    @GetMapping(value = {"community/qnalist","community/qnalist/{page}"})
    public String goqnaboard(Model model, CommunitySearchDto communitySearchDto,
                             @PathVariable("page") Optional<Integer> page){
        System.out.println("Q&A 리스트로 이동하기");
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 10);
        Page<Board> boardList = boardService.getQnaBoardList(communitySearchDto, pageable);
        model.addAttribute("boardList", boardList);
        model.addAttribute("communitySearchDto", communitySearchDto);
        model.addAttribute("maxPage", 10);

        List<Company> companyList = companyService.getcompanyList();
        Company company = companyList.get(0);
        System.out.println(company);
        model.addAttribute("company",company);
        return "community/qnalist";
    }
    //Q&A 상세보기
    @GetMapping("community/qnadtl/{id}")
    public String qnadtl(@PathVariable("id") Long id, Model model){
        boardService.updateView(id);
        Board board = boardService.getQnaById(id);
        model.addAttribute("board",board);

        List<Answer> answers = answerRepository.findByBoard(board);
//        List<Answer> answerList = answerService.getAnswerList(id);
        model.addAttribute("answers", answers);
        List<Company> companyList = companyService.getcompanyList();
        Company company = companyList.get(0);
        model.addAttribute("company",company);

        return "community/qnadtl";
    }
    //QNA 등록하기 폼 가기
    @GetMapping("community/qnacreate")
    public String goqnacreate(Model model, QnaFormDto qnaFormDto) {
        model.addAttribute("qnaFormDto", qnaFormDto);
        model.addAttribute("pagestate","등록");
        List<Company> companyList = companyService.getcompanyList();
        Company company = companyList.get(0);
        System.out.println(company);
        model.addAttribute("company",company);
        return "community/qnacreate";
    }

    //QNA 등록하기
    @PostMapping("community/qnacreate")
    public String reviewcreate(@Valid QnaFormDto qnaFormDto,
                               BindingResult bindingResult,
                               Model model) {
        if (bindingResult.hasErrors()) {
            return "community/qnacreate";
        }
        try {
            //상품 저장 로직을 호출한다. 매개변수로 상품정보와 상품 이미지 정보를 담고있는 itemImgFileList 넘겨줌
            boardService.createQna(qnaFormDto);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "게시물 등록 중 에러가 발생하였습니다.");
            return "community/qnacreate" ;
        }
        return "redirect:/community/qnalist";
    }

    //문의 수정폼 가기
    @GetMapping("community/qnaupdate/{id}")
    public String goqnaupdate(@PathVariable("id") Long id, Model model){

        try {
            QnaFormDto qnaFormDto = boardService.getQnaFormbyId(id);
            model.addAttribute("qnaFormDto", qnaFormDto);
            model.addAttribute("pagestate","수정");
            List<Company> companyList = companyService.getcompanyList();
            Company company = companyList.get(0);
            System.out.println(company);
            model.addAttribute("company",company);
        } catch (EntityNotFoundException e){
            model.addAttribute("errorMessage","존재하지 않는 글입니다.");
            model.addAttribute("qnaFormDto",new QnaFormDto());
            return "community/qnadtl";
        }
        return "community/qnacreate";
    }

    //문의글 수정하기
    @PostMapping("community/qnaupdate/{id}")
    public String qnaupdate(@PathVariable("id") Long id, Model model,  QnaFormDto qnaFormDto,
                               BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "community/qnaupdate";
        }
        try {
            boardService.updateQna(qnaFormDto);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "문의글 수정 중 에러가 발생하였습니다.");
            return "community/qnaupdate";
        }
        return "redirect:/community/qnalist";
    }
}