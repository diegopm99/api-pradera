package com.pradera.praderaback.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="trab_pra")
public class TrabajadorModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nom_trab_pra", nullable=false)
    private String nombres;

    @Column(name = "apep_trab_pra", nullable=false)
    private String apellidop;

    @Column(name = "apem_trab_pra", nullable=false)
    private String apellidom;

    @Column(name = "dni_trab_pra", nullable=false)
    private String dni;

    public String nombreCompleto(){
        return this.nombres + " " + this.apellidop + " " + this.apellidom;
    }
}
