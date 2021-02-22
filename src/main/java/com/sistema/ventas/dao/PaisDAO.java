package com.sistema.ventas.dao;

import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import org.springframework.stereotype.Service;

import com.sistema.ventas.model.Pais;

import lombok.NonNull;

@Service
public class PaisDAO extends BaseDAO<Pais,Integer>{

	@PersistenceContext
	EntityManager em;
	
	@Override
	protected EntityManager getEntityManager() {
		return em;
	}
	
	protected PaisDAO() {
		super(Pais.class);
	}

	@Override
	public void persist(Pais t) throws PersistenceException {
		super.persist(t);
	}

	@Override
	public void update(Pais t) throws PersistenceException {
		super.update(t);
	}

	@Override
	public Optional<Pais> find(@NonNull Integer id) {
		return super.find(id);
	}
}
