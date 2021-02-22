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

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import com.sistema.ventas.dto.ConsultarUsuarioDTO;
import com.sistema.ventas.dto.ProvinciaDTO;
import com.sistema.ventas.model.Pais;
import com.sistema.ventas.model.Provincia;
import com.sistema.ventas.model.ProvinciaCPK;
import com.sistema.ventas.util.GeneralUtil;

import lombok.NonNull;

@Service
public class ProvinciaDAO extends BaseDAO<Provincia,ProvinciaCPK>{

	@PersistenceContext
	EntityManager em;
	
	@Override
	protected EntityManager getEntityManager() {
		return em;
	}
	
	protected ProvinciaDAO() {
		super(Provincia.class);
	}

	@Override
	public void persist(Provincia t) throws PersistenceException {
		super.persist(t);
	}

	@Override
	public void update(Provincia t) throws PersistenceException {
		super.update(t);
	}

	@Override
	public Optional<Provincia> find(@NonNull ProvinciaCPK id) {
		return super.find(id);
	}

	
	/*
	 * 
	 * Consulta todos las provincia por pais
	 * 
	 * @author Bryan Zamora
	 * @param  intSecuenciaPais
	 * @return
	 */
	public List<ProvinciaDTO> findAll(Integer intSecuenciaPais) {

		StringBuilder strJPQL = new StringBuilder();
		
		try {	
			
			strJPQL.append(" SELECT p.provinciaCPK.secuenciaProvincia as secuenciaProvincia, ");
			strJPQL.append(" 		p.provinciaCPK.secuenciaPais as secuenciaPais, ");
			strJPQL.append("    	p.nombre as nombre,");
			strJPQL.append("    	p.esActivo as esActivo");
			strJPQL.append(" FROM 	Provincia p");
			strJPQL.append(" WHERE  p.esActivo = 'S'");	
			strJPQL.append(" AND 	p.provinciaCPK.secuenciaPais=:secuenciaPais");
			strJPQL.append(" ORDER BY nombre");

			TypedQuery<Tuple> query = (TypedQuery<Tuple>) em.createQuery(strJPQL.toString(), Tuple.class);
			
			query.setParameter("secuenciaPais",intSecuenciaPais);
			
			return query	
					.getResultList()
					.stream()
					.map(tuple -> {return ProvinciaDTO.builder()
					.secuenciaProvincia(tuple.get("secuenciaProvincia",Number.class).intValue())
					.secuenciaPais(tuple.get("secuenciaPais",Number.class).intValue())
					.nombre(tuple.get("nombre",String.class))
					.esActivo(tuple.get("esActivo",String.class))
					.build();})
					.collect(Collectors.toList());
		} catch (NoResultException e) {
			return null;
		}
	}
}
