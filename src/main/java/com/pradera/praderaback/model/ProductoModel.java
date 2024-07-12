package com.pradera.praderaback.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="prod_pra")
public class ProductoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nom_prod_pra", nullable=false)
    private String nombre;

    @Column(name = "pres_prod_pra", nullable=false)
    private String presentacion;

    @ManyToOne
    @JoinColumn(
            name = "id_cat_pra",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_prod_id_cat_pra")
    )
    private CategoriaModel Categoria;
}
