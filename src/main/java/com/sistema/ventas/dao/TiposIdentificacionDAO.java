package com.sistema.ventas.dao;

import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import org.springframework.stereotype.Service;

import com.sistema.ventas.model.TiposIdentificacion;

import lombok.NonNull;

@Service
public class TiposIdentificacionDAO extends BaseDAO<TiposIdentificacion, Integer>{

	@PersistenceContext
	EntityManager em;
	
	@Override
	protected EntityManager getEntityManager() {
		return em;
	}
	
	protected TiposIdentificacionDAO() {
		super(TiposIdentificacion.class);
	}

	@Override
	public void persist(TiposIdentificacion t) throws PersistenceException {
		super.persist(t);
	}

	@Override
	public void update(TiposIdentificacion t) throws PersistenceException {
		super.update(t);
	}

	@Override
	public Optional<TiposIdentificacion> find(@NonNull Integer id) {
		return super.find(id);
	}
}
