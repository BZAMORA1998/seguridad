package com.sistema.ventas.dao;

import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.sistema.ventas.model.UsuariosSistema;

@Service
public class UsuarioSistemaDAO extends BaseDAO<UsuariosSistema, Integer>{

	@PersistenceContext
	EntityManager em;
	
	@Override
	protected EntityManager getEntityManager() {
		return em;
	}
	
	protected UsuarioSistemaDAO() {
		super(UsuariosSistema.class);
	}

	@Override
	public void persist(UsuariosSistema t) throws PersistenceException {
		super.persist(t);
	}

	@Override
	public void update(UsuariosSistema t) throws PersistenceException {
		super.update(t);
	}

	@Override
	public Optional<UsuariosSistema> find(@NonNull Integer id) {
		return super.find(id);
	}
	
	public UsuariosSistema  consultarUsuarioSistema(String strUsuario) {
		try {	
			return em.createQuery(
						"SELECT us \n" +
						"  FROM UsuariosSistema us \n" +
						"  WHERE us.usuario=:usuario \n" +
						"  AND us.esActivo = 'S'",UsuariosSistema.class)
						.setParameter("usuario",strUsuario.toUpperCase())
						.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
}
