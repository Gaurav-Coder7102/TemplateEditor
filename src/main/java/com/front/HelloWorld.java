package com.front;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class HelloWorld {
	
	@GetMapping("/")
	public String get()
	{
		return "New";
		
	}

}
