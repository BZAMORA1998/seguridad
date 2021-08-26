package com.sistema.ventas.dao;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import org.springframework.stereotype.Service;

import com.sistema.ventas.model.ModuloXRoles;
import com.sistema.ventas.model.ModuloXRolesCPK;
import com.sistema.ventas.model.ModulosXUsuario;
import com.sistema.ventas.model.ModulosXUsuarioCPK;

import lombok.NonNull;

@Service
public class ModulosXUsuarioDAO extends BaseDAO<ModulosXUsuario,ModulosXUsuarioCPK>{

	@PersistenceContext
	EntityManager em;
	
	@Override
	protected EntityManager getEntityManager() {
		return em;
	}
	
	protected ModulosXUsuarioDAO() {
		super(ModulosXUsuario.class);
	}

	@Override
	public void persist(ModulosXUsuario t) throws PersistenceException {
		super.persist(t);
	}

	@Override
	public void update(ModulosXUsuario t) throws PersistenceException {
		super.update(t);
	}

	@Override
	public Optional<ModulosXUsuario> find(@NonNull ModulosXUsuarioCPK id) {
		return super.find(id);
	}
	
	
	public List<ModulosXUsuario> findModuloPorUsuario(Integer intSecuenciaUsuario) {
		try {	
			return em.createQuery(
						"SELECT t " +
						"  FROM ModulosXUsuario t " +
						"  WHERE t.modulosXUsuarioCPK.secuenciaUsuario = :secuenciaUsuario ",ModulosXUsuario.class)
						.setParameter("secuenciaUsuario", intSecuenciaUsuario)
						.getResultList();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public List<ModuloXRoles> findModuloPorRolExc(Integer intSecuenciaRol,Integer intSecuenciaModulo) {
		try {	
			return em.createQuery(
						"SELECT t \n" +
						"  FROM ModuloXRoles t \n" +
						"  WHERE t.moduloXRolesCPK.secuenciaRol = :secuenciaRol"+
						"  AND t.moduloXRolesCPK.secuenciaModulo != :secuenciaModulo",ModuloXRoles.class)
						.setParameter("secuenciaRol", intSecuenciaRol)
						.setParameter("secuenciaModulo", intSecuenciaModulo)
						.getResultList();
		} catch (NoResultException e) {
			return null;
		}
	}

}