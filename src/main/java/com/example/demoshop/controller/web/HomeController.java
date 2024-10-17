package com.example.demoshop.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/view/sale")
    public String saleList() {
        return "sell/sellItemList";
    }

    @GetMapping("/view/shop/item")
    public String shopItem() {
        return "home/shopItem";
    }

    @GetMapping("/view/home")
    public String viewHome() {
        return "home/home";
    }

    @GetMapping("/view/category")
    public String viewCategory() {
        return "home/category";
    }


    @GetMapping("/view/write/item")
    public String write() {
        return "sell/write";
    }

    @GetMapping("/view/update/item")
    public String viewUpdateItemForm() {
        return "sell/update";
    }

    @GetMapping("/view/signUp")
    public String signUp() {
        return "user/signUp";
    }

    @GetMapping("/view/login")
    public String login() {
        return "user/login";
    }

    @GetMapping("/view/update/user")
    public String uddateUser() {
        return "user/update";
    }

    @GetMapping("/view/user/wish")
    public String wishList() {
        return "wish/wishItemList";
    }

    @GetMapping("/view/user/order")
    public String orderList() {
        return "order/orderList";
    }

    @GetMapping("/view/message")
    public String messageList() {
        return "home/message";
    }
}
