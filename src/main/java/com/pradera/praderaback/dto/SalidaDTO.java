package com.pradera.praderaback.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.pradera.praderaback.model.ProductoModel;
import com.pradera.praderaback.model.TrabajadorModel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
public class SalidaDTO {

    private Long id;
    private Integer cantidad;
    @JsonFormat(pattern = "yyyy/MM/dd")
    private LocalDateTime fecha;
    private String productoNombre;
    private Long productoId;
    private String trabajadorNombre;
    private Long trabajadorId;
}
