package com.pradera.praderaback.controller;

import com.pradera.praderaback.dto.SalidaDTO;
import com.pradera.praderaback.service.SalidaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/salidas")
public class SalidaController {

    @Autowired
    private SalidaService service;

    @GetMapping("/bandeja")
    public ResponseEntity<Object> bandeja(
            SalidaDTO dto,
            @RequestParam(name = "page") Integer page,
            @RequestParam(name = "size") Integer size){
        try {
            return ResponseEntity.ok().body(service.bandeja(dto,page,size));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.toString());
        }
    }

    @RequestMapping(path = "/listar", method = RequestMethod.GET)
    public ResponseEntity<List<SalidaDTO>> listar() {
        try{
            return new ResponseEntity<>(service.listar(), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(path = "/obtener/{id}", method = RequestMethod.GET)
    public ResponseEntity<SalidaDTO> obtener(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(service.obtener(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(path = "/guardar", method = RequestMethod.POST)
    public ResponseEntity<Void> guardar(@RequestBody SalidaDTO dto) {
        try{
            service.guardar(dto);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e){
            System.out.println(e.toString());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(path = "/eliminar/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        try{
            service.eliminar(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
