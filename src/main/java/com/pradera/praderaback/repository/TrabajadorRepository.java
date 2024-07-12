package com.pradera.praderaback.repository;

import com.pradera.praderaback.model.TrabajadorModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrabajadorRepository extends JpaRepository<TrabajadorModel, Long> {
}
