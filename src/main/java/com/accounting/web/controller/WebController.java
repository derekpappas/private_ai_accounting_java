package com.accounting.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.accounting.service.CompanyService;
import com.accounting.service.TransactionService;
import com.accounting.service.FileService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/web")
@RequiredArgsConstructor
public class WebController {

    private final CompanyService companyService;
    private final TransactionService transactionService;
    private final FileService fileService;

    @GetMapping
    public String home(Model model) {
        return "index";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("companies", companyService.getAllCompanies());
        return "dashboard";
    }

    @GetMapping("/transactions")
    public String transactions(Model model) {
        // We'll implement this later with pagination and filtering
        return "transactions";
    }

    @GetMapping("/upload")
    public String upload(Model model) {
        model.addAttribute("companies", companyService.getAllCompanies());
        return "upload";
    }
} 