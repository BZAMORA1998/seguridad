package com.sistema.ventas.dao;

import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.sistema.ventas.model.Roles;

@Service
public class RolesDAO extends BaseDAO<Roles, Integer>{

	@PersistenceContext
	EntityManager em;
	
	@Override
	protected EntityManager getEntityManager() {
		return em;
	}
	
	protected RolesDAO() {
		super(Roles.class);
	}

	@Override
	public void persist(Roles t) throws PersistenceException {
		super.persist(t);
	}

	@Override
	public void update(Roles t) throws PersistenceException {
		super.update(t);
	}

	@Override
	public Optional<Roles> find(@NonNull Integer id) {
		return super.find(id);
	}

}
