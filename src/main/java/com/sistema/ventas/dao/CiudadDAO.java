package com.sistema.ventas.dao;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Service;

import com.sistema.ventas.dto.CiudadDTO;
import com.sistema.ventas.model.Ciudad;
import com.sistema.ventas.model.CiudadCPK;

import lombok.NonNull;

@Service
public class CiudadDAO extends BaseDAO<Ciudad,CiudadCPK>{

	@PersistenceContext
	EntityManager em;
	
	@Override
	protected EntityManager getEntityManager() {
		return em;
	}
	
	protected CiudadDAO() {
		super(Ciudad.class);
	}

	@Override
	public void persist(Ciudad t) throws PersistenceException {
		super.persist(t);
	}

	@Override
	public void update(Ciudad t) throws PersistenceException {
		super.update(t);
	}

	@Override
	public Optional<Ciudad> find(@NonNull CiudadCPK id) {
		return super.find(id);
	}

	/*
	 * Consulta todos las provincia por pais
	 * 
	 * @author Bryan Zamora
	 * @param  intSecuenciaPais
	 * @param intSecuenciaProvincia
	 * @return
	 */
	public List<CiudadDTO> findAll(Integer intSecuenciaPais, Integer intSecuenciaProvincia) {
		
		StringBuilder strJPQL = new StringBuilder();
		
		try {	
			
			strJPQL.append(" SELECT c.ciudadCPK.secuenciaCiudad as secuenciaCiudad, ");
			strJPQL.append("    	c.nombre as nombre,");
			strJPQL.append("    	c.esActivo as esActivo");
			strJPQL.append(" FROM 	Ciudad c");
			strJPQL.append(" WHERE  c.esActivo = 'S'");	
			strJPQL.append(" AND 	c.ciudadCPK.secuenciaPais=:secuenciaPais");
			strJPQL.append(" AND 	c.ciudadCPK.secuenciaProvincia=:secuenciaProvincia");
			strJPQL.append(" ORDER BY nombre");

			TypedQuery<Tuple> query = (TypedQuery<Tuple>) em.createQuery(strJPQL.toString(), Tuple.class);
			
			query.setParameter("secuenciaPais",intSecuenciaPais);
			query.setParameter("secuenciaProvincia",intSecuenciaProvincia);
			
			return query	
					.getResultList()
					.stream()
					.map(tuple -> {return CiudadDTO.builder()
					.secuenciaCiudad(tuple.get("secuenciaCiudad",Number.class).intValue())
					.nombre(tuple.get("nombre",String.class))
					.esActivo(tuple.get("esActivo",String.class))
					.build();})
					.collect(Collectors.toList());
		} catch (NoResultException e) {
			return null;
		}
		
	}
}
