package com.pradera.praderaback.repository;

import com.pradera.praderaback.model.UsuarioModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioModel, Long> {

    Optional<UsuarioModel> findByUsername(String username);
    Boolean existsByUsername(String username);

    @Query("select u from UsuarioModel u where u.username = :username and u.id != :id")
    Optional<UsuarioModel> findUserByUsername(String username, Long id);
}
