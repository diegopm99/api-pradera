package com.pradera.praderaback.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "user_pra")
public class UsuarioModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "usnam_user_pra", nullable=false)
    private String username;

    @Column(name = "pass_user_pra", nullable=false)
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name="user_rol_pra",
            joinColumns = @JoinColumn(
                    name="id_user_pra",
                    nullable = false,
                    foreignKey = @ForeignKey(name="FK_user_rol_id_user_pra")
            ),
            inverseJoinColumns = @JoinColumn(
                    name="id_rol_pra",
                    nullable = false,
                    foreignKey = @ForeignKey(name="FK_user_rol_id_rol_pra")
            )
    )
    private List<RolModel> roles = new ArrayList<>();
}
