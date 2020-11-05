package com.sistema.ventas.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sistema.ventas.model.Genero;
import com.sistema.ventas.model.RolSistema;


@Repository
public interface IRolSistemaDAO extends JpaRepository<RolSistema,Integer>{

	
}
