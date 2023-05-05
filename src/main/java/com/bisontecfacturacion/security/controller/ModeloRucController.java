package com.bisontecfacturacion.security.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bisontecfacturacion.security.config.ImportarExcel;
import com.bisontecfacturacion.security.config.RucFormato;
import com.bisontecfacturacion.security.model.ModeloRuc;
import com.bisontecfacturacion.security.model.Moneda;
import com.bisontecfacturacion.security.repository.ModeloRucRepository;

@Transactional()
@RestController
@RequestMapping("modeoRuc")
public class ModeloRucController {
	
	
}
