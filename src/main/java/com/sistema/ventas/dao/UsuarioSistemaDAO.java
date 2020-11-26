package com.sistema.ventas.dao;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.apache.commons.lang3.ObjectUtils;
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
	public void remove(UsuariosSistema t) throws PersistenceException {
		super.remove(t);
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
						"  WHERE us.user=:usuario \n" +
						"  AND us.esActivo = 'S'",UsuariosSistema.class)
						.setParameter("usuario",strUsuario.toUpperCase())
						.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<UsuariosSistema>  consultarUsuarioSistema(Integer intPage, Integer intPerPage) {
		try {	
			
			Query query =em.createQuery(
					"SELECT us \n" +
					"  FROM UsuariosSistema us \n" +
					"  WHERE us.esActivo = 'S'"+
					"  order by us.personas.primerApellido");
			
			if(!ObjectUtils.isEmpty(intPage) && !ObjectUtils.isEmpty(intPerPage))
				query.setFirstResult(intPage * intPerPage - intPerPage).setMaxResults(intPerPage);
			
			
			
			return query.getResultList();
					
		} catch (NoResultException e) {
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public Long  contarConsultarUsuarioSistema() {
		try {	
			
			return em.createQuery(
					"SELECT count(us) \n" +
					"  FROM UsuariosSistema us \n" +
					"  WHERE us.esActivo = 'S'",Long.class)
					.getSingleResult();		
		} catch (NoResultException e) {
			return null;
		}
	}

	public UsuariosSistema consultarUsuarioSistemaPorCedula(String numeroIdentificacion) {
		try {	
			return em.createQuery(
						"SELECT us \n" +
						"  FROM UsuariosSistema us \n" +
						"  JOIN us.personas pe \n" +
						"  WHERE pe.numeroIdentificacion=:numeroIdentificacion \n" +
						"  AND pe.esActivo = 'S' \n" +
						"  AND us.esActivo = 'S'",UsuariosSistema.class)
						.setParameter("numeroIdentificacion",numeroIdentificacion)
						.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
}
