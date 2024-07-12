package com.pradera.praderaback.controller;

import com.pradera.praderaback.dto.KardexResponse;
import com.pradera.praderaback.dto.ProductoDTO;
import com.pradera.praderaback.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/kardex")
public class KardexController {

    @Autowired
    private ProductoService productoService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Object> listar(
            @RequestParam(name = "categoria") String categoria,
            @RequestParam(name = "producto") String producto,
            @RequestParam(name = "page") Integer page,
            @RequestParam(name = "size") Integer size
    ) {
        try{
            Page<KardexResponse> data = productoService.kardex(categoria, producto, page, size);
            return new ResponseEntity<>(data, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
