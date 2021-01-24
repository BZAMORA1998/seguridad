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
import org.springframework.util.ObjectUtils;

import com.sistema.ventas.dto.FotosDTO;
import com.sistema.ventas.model.Fotos;
import com.sistema.ventas.model.FotosCPK;

import lombok.NonNull;

@Service
public class FotosDAO extends BaseDAO<Fotos,FotosCPK>{

	@PersistenceContext
	EntityManager em;
	
	@Override
	protected EntityManager getEntityManager() {
		return em;
	}
	
	protected FotosDAO() {
		super(Fotos.class);
	}

	@Override
	public void persist(Fotos t) throws PersistenceException {
		super.persist(t);
	}

	@Override
	public void update(Fotos t) throws PersistenceException {
		super.update(t);
	}

	@Override
	public Optional<Fotos> find(@NonNull FotosCPK id) {
		return super.find(id);
	}

	public List<FotosDTO> consultarFotos(Integer intCodigoEmpresa, String strNemonico, String strNombre) {
		StringBuilder strJPQL = new StringBuilder();

		try {
			strJPQL.append(" SELECT de.fotosCPK.nemonico as nemonico, ");
			strJPQL.append("    	de.fotosCPK.nombre as nombre, ");
			strJPQL.append("    	de.foto as foto");
			strJPQL.append(" FROM 	Fotos de");
			strJPQL.append(" WHERE 	de.fotosCPK.secuenciaEmpresa=:secuenciaEmpresa");
			
			if(!ObjectUtils.isEmpty(strNemonico))
				strJPQL.append(" AND  	de.fotosCPK.nemonico =:nemonico");
			
			if(!ObjectUtils.isEmpty(strNombre))
				strJPQL.append(" AND  	de.fotosCPK.nombre =:nombre");
			
			strJPQL.append(" AND  	de.esActivo ='S'");
			
			TypedQuery<Tuple> query = (TypedQuery<Tuple>) em.createQuery(strJPQL.toString(), Tuple.class);
			query.setParameter("secuenciaEmpresa", intCodigoEmpresa);
			
			if(!ObjectUtils.isEmpty(strNemonico))
				query.setParameter("nemonico", strNemonico.toUpperCase());
			
			if(!ObjectUtils.isEmpty(strNombre))
				query.setParameter("nombre", strNombre.toUpperCase());
			
			return query	
					.getResultList()
					.stream()
					.map(tuple -> {return FotosDTO.builder()
					.nemonico(tuple.get("nemonico",String.class))
					.nombre(tuple.get("nombre",String.class))
					.foto(tuple.get("foto",byte[].class))
					.build();})
					.collect(Collectors.toList());

		} catch (NoResultException e) {
			return null;
		}
	}
}
