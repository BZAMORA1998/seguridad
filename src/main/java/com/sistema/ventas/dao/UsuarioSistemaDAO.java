package com.sistema.ventas.dao;

import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import org.springframework.stereotype.Service;

import com.sistema.ventas.model.UsuarioSistema;

import lombok.NonNull;

@Service
public class UsuarioSistemaDAO extends BaseDAO<UsuarioSistema, Integer>{

	@PersistenceContext
	EntityManager em;
	
	@Override
	protected EntityManager getEntityManager() {
		return em;
	}
	
	protected UsuarioSistemaDAO() {
		super(UsuarioSistema.class);
	}

	@Override
	public void persist(UsuarioSistema t) throws PersistenceException {
		super.persist(t);
	}

	@Override
	public void update(UsuarioSistema t) throws PersistenceException {
		super.update(t);
	}

	@Override
	public Optional<UsuarioSistema> find(@NonNull Integer id) {
		return super.find(id);
	}
	
	public UsuarioSistema  consultarUsuarioSistema(String strUsuario) {
		try {	
			return em.createQuery(
						"SELECT us \n" +
						"  FROM UsuarioSistema us \n" +
						"  WHERE us.usuario=:usuario \n" +
						"  AND us.esActivo = 'S'",UsuarioSistema.class)
						.setParameter("usuario",strUsuario)
						.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
}
