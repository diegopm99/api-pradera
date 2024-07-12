package com.pradera.praderaback.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class KardexResponse {
    private long id;
    private String producto;
    private int entradas;
    private int salidas;
    private int stock;
}
