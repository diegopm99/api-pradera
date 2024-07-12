package com.pradera.praderaback.dto;

import com.pradera.praderaback.model.RolModel;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UsuarioDTO {

    private Long id;
    private String username;
    private String password;
    private List<RolDTO> roles;
}
