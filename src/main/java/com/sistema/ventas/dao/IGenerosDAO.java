package com.sistema.ventas.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sistema.ventas.model.Personas;

@Repository
public interface IGenerosDAO extends JpaRepository<Personas, Integer>{

}
