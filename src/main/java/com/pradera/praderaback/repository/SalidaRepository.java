package com.pradera.praderaback.repository;

import com.pradera.praderaback.model.SalidaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalidaRepository extends JpaRepository<SalidaModel, Long> {
}
