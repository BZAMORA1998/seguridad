package com.sistema.ventas.dao;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Service;

import com.sistema.ventas.dto.ConsultarModulosDTO;
import com.sistema.ventas.model.Modulos;

import lombok.NonNull;

@Service
public class ModulosDAO extends BaseDAO<Modulos,Integer>{

	@PersistenceContext
	EntityManager em;
	
	@Override
	protected EntityManager getEntityManager() {
		return em;
	}
	
	protected ModulosDAO() {
		super(Modulos.class);
	}

	@Override
	public void persist(Modulos t) throws PersistenceException {
		super.persist(t);
	}

	@Override
	public void update(Modulos t) throws PersistenceException {
		super.update(t);
	}

	@Override
	public Optional<Modulos> find(@NonNull Integer id) {
		return super.find(id);
	}
	
	
	public List<ConsultarModulosDTO> consultarModulos(Integer intSecuenciaUsuario) {
		
		StringBuilder strJPQLBase = new StringBuilder();
		strJPQLBase.append("SELECT m.secuencia_modulo as secuenciaModulo,m.nombre as nombre ");
		strJPQLBase.append("from tbl_modulo_x_roles mr,tbl_modulos m ");
		strJPQLBase.append("where mr.secuencia_modulo=m.secuencia_modulo ");
		strJPQLBase.append("and   mr.es_activo='S' ");
		strJPQLBase.append("and   m.es_activo='S' ");
		strJPQLBase.append("and mr.secuencia_rol in (select distinct r.secuencia_rol from tbl_usuario_x_roles ru, tbl_roles r ");
		strJPQLBase.append("						where ru.secuencia_rol=r.secuencia_rol " );
		strJPQLBase.append("						and   ru.secuencia_usuario=:secuenciaUsuario " );
		strJPQLBase.append("  						and ru.es_activo='S'" );
		strJPQLBase.append("  						and r.es_activo='S') ");
		TypedQuery<Tuple> query = (TypedQuery<Tuple>) em.createNativeQuery(strJPQLBase.toString(), Tuple.class);
		//PARAMETROS
		query.setParameter("secuenciaUsuario", intSecuenciaUsuario);

		return query.getResultList().stream()
				.map(tuple -> ConsultarModulosDTO.builder()
				.secuenciaModulo(tuple.get("secuenciaModulo")!=null?tuple.get("secuenciaModulo", Number.class).intValue():null)
				.nombre(tuple.get("nombre", String.class))
				.build())
		.distinct()
		.collect(Collectors.toList());
	}


}
