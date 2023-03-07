package com.yuri.shoppingsite.controller;


import com.yuri.shoppingsite.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class BoardController {
    @Autowired
    private BoardService boardService;

    @GetMapping("board")
    public void goboard(Model model){
    model.addAttribute("list",boardService.listAll());
    }

    @GetMapping("selectOne/{id}")
    public String goDetail(Model model, @PathVariable("id") int bid){
        model.addAttribute("boarddto",boardService.selectOne(bid));
        return "selectOne";
    }


}
