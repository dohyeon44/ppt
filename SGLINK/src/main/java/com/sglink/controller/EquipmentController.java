package com.sglink.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
@RequestMapping(value="/equipment")
@Controller
public class EquipmentController {
	@RequestMapping(value="/view", method = RequestMethod.GET)
	public String main() {
		return "/equipment/equipment";
	}

}