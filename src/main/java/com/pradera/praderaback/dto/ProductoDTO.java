package com.pradera.praderaback.dto;

import com.pradera.praderaback.model.CategoriaModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductoDTO {
    private Long id;
    private String nombre;
    private String presentacion;
    private Long categoriaId;
    private String categoriaNombre;
}
