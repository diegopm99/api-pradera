package com.pradera.praderaback.controller;

import com.pradera.praderaback.dto.IngresoDTO;
import com.pradera.praderaback.service.IngresoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ingresos")
public class IngresoController {

    @Autowired
    private IngresoService service;

    @GetMapping("/bandeja")
    public ResponseEntity<Object> bandeja(
            IngresoDTO dto,
            @RequestParam(name = "page") Integer page,
            @RequestParam(name = "size") Integer size){
        try {
            return ResponseEntity.ok().body(service.bandeja(dto,page,size));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.toString());
        }
    }

    @GetMapping("/{id}")
    public @ResponseBody IngresoDTO obtenerID(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping("/guardar")
    public @ResponseBody void guardar(@RequestBody IngresoDTO dto) {
        service.guardar(dto);
    }

    @DeleteMapping("/eliminar/{id}")
    public @ResponseBody void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }

}
