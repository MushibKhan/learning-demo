package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MuSqEntryController {

	@GetMapping("/forwarded-cases")
	public String showForwardedCasesPage() {
	    return "pages/forwardedcases";
	}
}
