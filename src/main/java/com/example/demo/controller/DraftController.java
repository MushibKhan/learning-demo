package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DraftController {

	@GetMapping("/draft")
	public String showDraft() {
		return "pages/draft";
	}

}
