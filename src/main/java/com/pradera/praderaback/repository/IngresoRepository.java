package com.pradera.praderaback.repository;

import com.pradera.praderaback.model.IngresosModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IngresoRepository extends JpaRepository<IngresosModel,Long> {
}
