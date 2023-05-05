package com.bisontecfacturacion.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.bisontecfacturacion.security.model.Print;
import com.bisontecfacturacion.security.repository.PrintRepository;

@RestController
@RequestMapping("print")
public class PrintController {
	
	@Autowired
	private PrintRepository printRepository;

	@RequestMapping(method=RequestMethod.GET)
	public Print getAll(){
		System.out.println("enqowieurpoqiwueproiquwpeoiru");
		Print p=printRepository.findById(1).get();
		return p;
	}
}
