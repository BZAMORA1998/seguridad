package com.sistema.ventas.bo.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sistema.ventas.bo.IGeneralBO;
import com.sistema.ventas.dao.CiudadDAO;
import com.sistema.ventas.dao.GenerosDAO;
import com.sistema.ventas.dao.PaisDAO;
import com.sistema.ventas.dao.ProvinciaDAO;
import com.sistema.ventas.dao.TiposIdentificacionDAO;
import com.sistema.ventas.dto.CiudadDTO;
import com.sistema.ventas.dto.EnviarEmailDTO;
import com.sistema.ventas.dto.ProvinciaDTO;
import com.sistema.ventas.email.SendEmail;
import com.sistema.ventas.enums.FormatoEdad;
import com.sistema.ventas.exceptions.BOException;
import com.sistema.ventas.model.Generos;
import com.sistema.ventas.model.Pais;
import com.sistema.ventas.model.TiposIdentificacion;
import com.sistema.ventas.util.EdadUtil;

@Service
public class GeneralBOImpl implements IGeneralBO{

	@Autowired
	private TiposIdentificacionDAO objTiposIdenticacionDAO;
	@Autowired
	private GenerosDAO objGeneroDAO;
	@Autowired
	private PaisDAO objPaisDAO;
	@Autowired
	private ProvinciaDAO objProvinciaDAO;
	@Autowired
	private CiudadDAO objCiudadDAO;
	@Autowired
	private SendEmail objSendEmail;
	
	@Override
	public List<TiposIdentificacion> findAllTiposIdentificacion() throws BOException {
		
		return objTiposIdenticacionDAO.findAll();
	}
	
	@Override
	public List<Generos> findAllGeneros() throws BOException {
		// TODO Auto-generated method stub
		return objGeneroDAO.findAll();
	}

	@Override
	public List<Pais> findAllPais() throws BOException {
		// TODO Auto-generated method stub
		return objPaisDAO.findAll();
	}

	@Override
	public List<ProvinciaDTO> findAllProvincia(Integer intSecuenciaPais) throws BOException {
		
		if (ObjectUtils.isEmpty(intSecuenciaPais)) 
			throw new BOException("ven.warn.campoObligatorio", new Object[] { "ven.campos.secuenciaPais"});
		
		return objProvinciaDAO.findAll(intSecuenciaPais);
	}

	@Override
	public List<CiudadDTO> findAllCiudad(Integer intSecuenciaPais, Integer intSecuenciaProvincia) throws BOException {
		
		if (ObjectUtils.isEmpty(intSecuenciaPais)) 
			throw new BOException("ven.warn.campoObligatorio", new Object[] { "ven.campos.secuenciaPais"});
		
		if (ObjectUtils.isEmpty(intSecuenciaProvincia)) 
			throw new BOException("ven.warn.campoObligatorio", new Object[] { "ven.campos.secuenciaProvincia"});
		
		
		return objCiudadDAO.findAll(intSecuenciaPais,intSecuenciaProvincia);
	}

	@Override
	public Map<String,Object> consultarFechaNacimiento(String strLanguage, String strFechaNacimiento) throws BOException {
		
		if (ObjectUtils.isEmpty(strFechaNacimiento)) 
			throw new BOException("ven.warn.campoObligatorio", new Object[] { "ven.campos.fechaNacimiento"});
		
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("edad", EdadUtil.convertirFechaNacimientoAEdad(strFechaNacimiento, FormatoEdad.SHORT, strLanguage));
		
		return map;
	}

	@Override
	public void enviarEmailDTO(EnviarEmailDTO objEnviarEmail) throws BOException {
		if (ObjectUtils.isEmpty(objEnviarEmail.getEmail())) 
			throw new BOException("ven.warn.campoObligatorio", new Object[] { "ven.campos.email"});
		
		if (ObjectUtils.isEmpty(objEnviarEmail.getAsunto())) 
			throw new BOException("ven.warn.campoObligatorio", new Object[] { "ven.campos.asunto"});
		
		if (ObjectUtils.isEmpty(objEnviarEmail.getContenido())) 
			throw new BOException("ven.warn.campoObligatorio", new Object[] { "ven.campos.contenido"});
		
		objSendEmail.envioEmail(objEnviarEmail.getEmail(), objEnviarEmail.getAsunto(), objEnviarEmail.getContenido());
	}

}
