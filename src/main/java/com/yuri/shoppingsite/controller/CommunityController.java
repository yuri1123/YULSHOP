package com.yuri.shoppingsite.controller;

import com.yuri.shoppingsite.domain.community.Board;
import com.yuri.shoppingsite.domain.community.CommunitySearchDto;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Controller
public class CommunityController {

    private final BoardService boardService;
    private final CompanyService companyService;
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
        Board board = boardService.getNoticebyId(id);
        model.addAttribute("board",board);
        List<Company> companyList = companyService.getcompanyList();
        Company company = companyList.get(0);
        model.addAttribute("company",company);

        return "community/noticedtl";
    }


    //리뷰게시판 페이지로 이동
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

    //Q&A 게시판 페이지로 이동
    @GetMapping(value = {"community/qnalist","community/qnalist/{page}"})
    public String goqnaboard(Model model, CommunitySearchDto communitySearchDto,
                             @PathVariable("page") Optional<Integer> page){
        System.out.println("review 리스트로 이동하기");
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




//    //qna 게시판으로 이동
//    @GetMapping("community/qna")
//    public String list(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
//                       @RequestParam(value = "kw", defaultValue = "") String kw) {
//        Page<Question> paging = this.questionService.getList(page, kw);
//        model.addAttribute("paging", paging);
//        model.addAttribute("kw", kw);
//        return "community/qna";
//    }
//
//    //질문등록 폼가기
//    @PreAuthorize("isAuthenticated()")
//    @GetMapping("question/create")
//    public String qcreate(QuestionForm questionForm){
//        return "community/qnacreate";
//    }
//
//    //질문 등록하기
//    @PreAuthorize("isAuthenticated()")
//    @PostMapping("question/create")
//    public String qcreatedo(@Valid QuestionForm questionForm, BindingResult bindingResult,
//                            Principal principal){
//
//        if(bindingResult.hasErrors()){
//            return "community/qnacreate";
//        }
//        String member = principal.getName();
//        this.questionService.create(questionForm.getSubject(),questionForm.getContent(),member);
//        return "redirect:/community/qna";
//    }
//
//    //상세보기화면 가기
//    @GetMapping("/community/qnadetail/{id}")
//    public String detail(Model model, @PathVariable("id") Integer id, AnswerForm answerForm){
//        Question question = this.questionService.getQuestion(id);
//        model.addAttribute("question", question);
//    return "community/qnadetail";
//
//    }
//
//    //수정페이지 이동
//    @PreAuthorize("isAuthenticated()")
//    @GetMapping("community/modify/{id}")
//    public String questionModify(QuestionForm questionForm, @PathVariable("id") int id
//    ,Principal principal){
//        Question question = this.questionService.getQuestion(id);
////        if(!question.getAuthor().getUsername().equals(principal.getName())){
////            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다");
////        }
//    questionForm.setSubject(question.getSubject());
//    questionForm.setContent(question.getContent());
//    return "community/qnacreate";
//    }
//
//    //수정하기
//    @PreAuthorize("isAuthenticated()")
//    @PostMapping("community/modify/{id}")
//    public String questionModify(@Valid QuestionForm questionForm,
//                                 BindingResult bindingResult,
//                                 Principal principal,
//                                 @PathVariable("id") int id){
//        if(bindingResult.hasErrors()){
//            return "community/qnacreate";
//        }
//        Question question = this.questionService.getQuestion(id);
////        if(!question.getAuthor().getUsername().equals(principal.getName())){
////            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
////        }
//    this.questionService.modify(question, questionForm.getSubject(),questionForm.getContent());
//        return String.format("redirect:/community/qnadetail/%s",id);
//
//    }
//
//    //삭제하기
//    @PreAuthorize("isAuthenticated()")
//    @GetMapping("community/delete/{id}")
//    public String questionDelete(Principal principal, @PathVariable("id") Integer id) {
//        Question question = this.questionService.getQuestion(id);
////        if (!question.getAuthor().getUsername().equals(principal.getName())) {
////            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
////        }
//        this.questionService.delete(question);
//   return "redirect:/community/qna";
//    }
//
//    //추천하기
//    @PreAuthorize("isAuthenticated()")
//    @GetMapping("question/vote/{id}")
//    public String questionVote(Principal principal, @PathVariable("id") Integer id){
//        Question question = this.questionService.getQuestion(id);
//        SiteUser siteUser = this.userService.getUser(principal.getName());
//        this.questionService.vote(question, siteUser);
//        return String.format("redirect:/community/qnadetail/%s",id);
//    }
//
//
//    //답글달기
//    @PreAuthorize("isAuthenticated()")
//    @PostMapping("community/create/{id}")
//    public String createAnswer(Model model,
//                               @PathVariable("id") Integer id,
//                               @Valid AnswerForm answerForm, BindingResult bindingResult,
//                               Principal principal){
//    Question question = this.questionService.getQuestion(id);
//        SiteUser siteUser = this.userService.getUser(principal.getName());
//    if(bindingResult.hasErrors()) {
//        model.addAttribute("question", question);
//        return "community/qnadetail";
//    }
//    this.answerService.create(question,answerForm.getContent(),siteUser);
//        return String.format("redirect:/community/qnadetail/%s", id);
//    }
//
//    //답글 수정하기 페이지 이동
//    @PreAuthorize("isAuthenticated()")
//    @GetMapping("answer/modify/{id}")
//    public String answerModify(AnswerForm answerForm,@PathVariable("id") Integer id,
//                               Principal principal){
//        Answer answer = this.answerService.getAnswer(id);
////        if (!answer.getAuthor().getUsername().equals(principal.getName())) {
////            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
////        }
//        answerForm.setContent(answer.getContent());
//        return "/community/answer_form";
//    }
//
//    //답글 수정하기
//    @PreAuthorize("isAuthenticated()")
//    @PostMapping("answer/modify/{id}")
//    public String answerModify(@Valid AnswerForm answerForm, BindingResult bindingResult,
//                               @PathVariable("id") Integer id, Principal principal){
//        if(bindingResult.hasErrors()){
//            return "answer_form";
//        }
//        Answer answer = this.answerService.getAnswer(id);
////        if(!answer.getAuthor().getUsername().equals(principal.getName())){
////            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"수정권한이 없습니다.");
////        }
//        this.answerService.modify(answer, answerForm.getContent());
//        return String.format("redirect:/community/qnadetail/%s", answer.getQuestion().getId());
//        }
//
//
//        //답글 삭제하기
//     @PreAuthorize("isAuthenticated()")
//     @GetMapping("answer/delete/{id}")
//    public String answerDelete(@PathVariable("id") Integer id, Principal principal){
//        Answer answer = this.answerService.getAnswer(id);
////        if(!answer.getAuthor().getUsername().equals(principal.getName())){
////            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"삭제 권한이 없습니다");
////        }
//        this.answerService.delete(answer);
//        return String.format("redirect:/community/qnadetail/%s", answer.getQuestion().getId());
//    }
//
//    //답글추천하기
//    @PreAuthorize("isAuthenticated()")
//    @GetMapping("answer/vote/{id}")
//    public String answerVote(Principal principal, @PathVariable("id") Integer id) {
//        Answer answer = this.answerService.getAnswer(id);
//        SiteUser siteUser = this.userService.getUser(principal.getName());
//        this.answerService.vote(answer, siteUser);
//        return String.format("redirect:/community/qnadetail/%s", answer.getQuestion().getId());
//    }

    }

