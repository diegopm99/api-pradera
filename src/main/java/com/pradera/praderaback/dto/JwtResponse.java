package com.pradera.praderaback.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JwtResponse {
    private Boolean respuesta;
    private final String token;
    private UsuarioDTO usuario;
}
