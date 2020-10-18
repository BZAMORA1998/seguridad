package com.sistema.ventas.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sistema.ventas.model.UsuariosSistema;


@Repository
public interface IUsuarioSistemaDAO extends JpaRepository<UsuariosSistema,Integer>{

	
}
