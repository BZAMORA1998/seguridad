package com.sistema.ventas.bo.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sistema.ventas.bo.IServiciosUtilBO;
import com.sistema.ventas.dao.ITiposIdentificacionDAO;
import com.sistema.ventas.exceptions.BOException;
import com.sistema.ventas.model.TiposIdentificacion;

@Service
public class ServiciosUtilBOImpl implements IServiciosUtilBO{

	@Autowired
	private ITiposIdentificacionDAO objITiposIdenticacionDAO;
	
	@Override
	public List<TiposIdentificacion> findAll() throws BOException {
		
		return objITiposIdenticacionDAO.findAll();
	}

}
