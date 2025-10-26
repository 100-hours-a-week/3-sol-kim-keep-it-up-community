package com.project.community.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;

@Controller
@RequestMapping("/legal")
public class LegalController {

    /*
    GET 이용약관 조회
    => terms.html
     */
    @GetMapping("/terms")
    public String terms(Model model) {
        model.addAttribute("serviceName", "keepit-up");
        model.addAttribute("effectiveDate", LocalDate.now()); // "2025-10-26" 등으로 고정해도 됨
        model.addAttribute("supportEmail", "support@keepit-up.example");
        model.addAttribute("privacyEmail", "privacy@keepit-up.example");
        return "legal/terms"; // resources/templates/legal/terms.html
    }

    /*
    GET 개인정보처리방침 조회
    => privacy.html
     */
    @GetMapping("/privacy")
    public String privacy(Model model) {
        model.addAttribute("serviceName", "keepit-up");
        model.addAttribute("effectiveDate", LocalDate.now());
        model.addAttribute("supportEmail", "support@keepit-up.example");
        model.addAttribute("privacyEmail", "privacy@keepit-up.example");
        return "legal/privacy"; // resources/templates/legal/privacy.html
    }
}
