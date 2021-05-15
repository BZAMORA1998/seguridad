package com.sistema.ventas.dao;

import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import org.springframework.stereotype.Service;

import com.sistema.ventas.model.ModuloXRoles;
import com.sistema.ventas.model.ModuloXRolesCPK;

import lombok.NonNull;

@Service
public class ModuloXRolesDAO extends BaseDAO<ModuloXRoles,ModuloXRolesCPK>{

	@PersistenceContext
	EntityManager em;
	
	@Override
	protected EntityManager getEntityManager() {
		return em;
	}
	
	protected ModuloXRolesDAO() {
		super(ModuloXRoles.class);
	}

	@Override
	public void persist(ModuloXRoles t) throws PersistenceException {
		super.persist(t);
	}

	@Override
	public void update(ModuloXRoles t) throws PersistenceException {
		super.update(t);
	}

	@Override
	public Optional<ModuloXRoles> find(@NonNull ModuloXRolesCPK id) {
		return super.find(id);
	}
}