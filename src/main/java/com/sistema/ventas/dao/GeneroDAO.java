package com.sistema.ventas.dao;

import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.sistema.ventas.model.Genero;
import com.sistema.ventas.model.TiposIdentificacion;

@Service
public class GeneroDAO extends BaseDAO<Genero, Integer>{

	@PersistenceContext
	EntityManager em;
	
	@Override
	protected EntityManager getEntityManager() {
		return em;
	}
	
	protected GeneroDAO() {
		super(Genero.class);
	}

	@Override
	public void persist(Genero t) throws PersistenceException {
		super.persist(t);
	}

	@Override
	public void update(Genero t) throws PersistenceException {
		super.update(t);
	}

	@Override
	public Optional<Genero> find(@NonNull Integer id) {
		return super.find(id);
	}
}
