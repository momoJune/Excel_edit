package org.example.excel_edit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class TestController {

    @Autowired FileController fileController;

    @GetMapping("/")
    public String index() {

        // 1차 커밋

        // 2차 커밋

        return "index";
    }

}
