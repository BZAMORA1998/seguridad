package com.sistema.ventas.dao;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.sistema.ventas.model.Usuarios;

@Service
public class UsuariosDAO extends BaseDAO<Usuarios, Integer>{

	@PersistenceContext
	EntityManager em;
	
	@Override
	protected EntityManager getEntityManager() {
		return em;
	}
	
	protected UsuariosDAO() {
		super(Usuarios.class);
	}

	@Override
	public void persist(Usuarios t) throws PersistenceException {
		super.persist(t);
	}

	@Override
	public void update(Usuarios t) throws PersistenceException {
		super.update(t);
	}

	@Override
	public void remove(Usuarios t) throws PersistenceException {
		super.remove(t);
	}

	
	@Override
	public Optional<Usuarios> find(@NonNull Integer id) {
		return super.find(id);
	}
	
	public Usuarios  consultarUsuarioSistema(String strUsuario) {
		try {	
			return em.createQuery(
						"SELECT us \n" +
						"  FROM Usuarios us \n" +
						"  WHERE us.usuario=:usuario \n" +
						"  AND us.esActivo = 'S'",Usuarios.class)
						.setParameter("usuario",strUsuario.toUpperCase())
						.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Usuarios>  consultarUsuarioSistema(Integer intPage, Integer intPerPage, String strCedulaCodigoUsuario, String strEstado) {
		try {	
			
			Query query =em.createQuery(
					"SELECT us \n" +
					"  FROM Usuarios us \n" +
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
	public Long  contarConsultarUsuarioSistema(String strCedulaCodigoUsuario, String strEstado) {
		try {	
			
			return em.createQuery(
					"SELECT count(us) \n" +
					"  FROM Usuarios us \n" +
					"  WHERE us.esActivo = 'S'",Long.class)
					.getSingleResult();		
		} catch (NoResultException e) {
			return null;
		}
	}

	public Usuarios consultarUsuarioSistemaPorCedula(String numeroIdentificacion) {
		try {	
			return em.createQuery(
						"SELECT us \n" +
						"  FROM Usuarios us \n" +
						"  JOIN us.personas pe \n" +
						"  WHERE pe.numeroIdentificacion=:numeroIdentificacion \n" +
						"  AND pe.esActivo = 'S' \n" +
						"  AND us.esActivo = 'S'",Usuarios.class)
						.setParameter("numeroIdentificacion",numeroIdentificacion)
						.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
}
