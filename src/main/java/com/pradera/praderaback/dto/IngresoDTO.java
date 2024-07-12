package com.pradera.praderaback.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
public class IngresoDTO {

    //LISTAR
    private Long id;
    private String productoNombre;
    private String proveedorNombre;
    private String comprobante;
    private int cantidad;
    private double precio;
    private double total;
    @JsonFormat(pattern = "yyyy/MM/dd")
    private LocalDateTime fecha;

    //GUARDAR
    private Long productoId;
    private Long proveedorId;
}
