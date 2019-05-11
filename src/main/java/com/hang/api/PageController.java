package com.hang.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author hangs.zhang
 * @date 19-5-11
 * *****************
 * function:
 */
@Slf4j
@Controller
@RequestMapping("/page")
public class PageController {

    @GetMapping("/menu")
    public String menu() {
        return "menu";
    }

    @GetMapping("/{page}")
    public String page1(@PathVariable String page) {
        return page;
    }

}
