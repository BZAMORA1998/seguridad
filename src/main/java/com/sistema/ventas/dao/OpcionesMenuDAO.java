package com.sistema.ventas.dao;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.sistema.ventas.model.OpcionesMenu;

@Service
public class OpcionesMenuDAO extends BaseDAO<OpcionesMenu, Integer>{

	@PersistenceContext
	EntityManager em;
	
	@Override
	protected EntityManager getEntityManager() {
		return em;
	}
	
	protected OpcionesMenuDAO() {
		super(OpcionesMenu.class);
	}

	@Override
	public void persist(OpcionesMenu t) throws PersistenceException {
		super.persist(t);
	}

	@Override
	public void update(OpcionesMenu t) throws PersistenceException {
		super.update(t);
	}

	@Override
	public Optional<OpcionesMenu> find(@NonNull Integer id) {
		return super.find(id);
	}

	public List<OpcionesMenu> findByNemonic(String strNemonico, Integer intCodigoEmpresa) {
		try {	
			return em.createQuery(
						"SELECT t \n" +
						"  FROM OpcionesMenu t \n" +
						"  WHERE t.esActivo = 'S' "+
						"  AND 	 t.opcionesMenuCPK.nemonico = :nemonico"+
						"  AND 	 t.opcionesMenuCPK.secuenciaEmpresa = :secuenciaEmpresa"+
						"  ORDER BY t.orden asc",OpcionesMenu.class)
						.setParameter("nemonico",strNemonico)
						.setParameter("secuenciaEmpresa",intCodigoEmpresa)
						.getResultList();
		} catch (NoResultException e) {
			return null;
		}
	}
}
