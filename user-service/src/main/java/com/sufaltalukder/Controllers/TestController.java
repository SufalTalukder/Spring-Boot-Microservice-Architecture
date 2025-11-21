package com.sufaltalukder.Controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test-api")
public class TestController {

	@GetMapping("/get")
	public String getString() {
		return "Hello World";
	}
}
