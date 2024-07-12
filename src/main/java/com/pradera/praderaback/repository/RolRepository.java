package com.pradera.praderaback.repository;

import com.pradera.praderaback.model.RolModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolRepository extends JpaRepository<RolModel, Long> {

    RolModel findByNombre(String nombre);
}
