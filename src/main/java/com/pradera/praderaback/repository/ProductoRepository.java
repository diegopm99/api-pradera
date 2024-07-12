package com.pradera.praderaback.repository;

import com.pradera.praderaback.model.ProductoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.Tuple;
import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<ProductoModel, Long> {

    @Query(value = "SELECT \n" +
            "    pp.id AS id,\n" +
            "    pp.nom_prod_pra AS producto, \n" +
            "    COALESCE(ip.cant_ingr_pra, 0) AS entradas, \n" +
            "    COALESCE(sp.cant_sali_pra, 0) AS salidas, \n" +
            "    COALESCE(ip.cant_ingr_pra, 0) - COALESCE(sp.cant_sali_pra, 0) AS stock \n" +
            "FROM prod_pra pp \n" +
            "LEFT JOIN ( \n" +
            "    SELECT id_prod_pra, SUM(cant_ingr_pra) AS cant_ingr_pra \n" +
            "    FROM ingr_pra \n" +
            "    GROUP BY id_prod_pra \n" +
            ") ip ON pp.id = ip.id_prod_pra \n" +
            "LEFT JOIN ( \n" +
            "    SELECT id_prod_pra, SUM(cant_sali_pra) AS cant_sali_pra \n" +
            "    FROM sali_pra \n" +
            "    GROUP BY id_prod_pra \n" +
            ") sp ON pp.id = sp.id_prod_pra \n" +
            "INNER JOIN cat_pra cp ON pp.id_cat_pra = cp.id \n" +
            "WHERE cp.nom_cat_pra LIKE %?1% \n" +
            "AND pp.nom_prod_pra LIKE %?2% " +
            "GROUP BY pp.id, pp.nom_prod_pra", nativeQuery = true)
    List<Tuple> findKardexByFilter(String categoria, String producto);
}
