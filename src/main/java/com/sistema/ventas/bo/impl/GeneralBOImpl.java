package com.sistema.ventas.bo.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sistema.ventas.bo.IGeneralBO;
import com.sistema.ventas.dao.GenerosDAO;
import com.sistema.ventas.dao.TiposIdentificacionDAO;
import com.sistema.ventas.exceptions.BOException;
import com.sistema.ventas.model.Generos;
import com.sistema.ventas.model.TiposIdentificacion;

@Service
public class GeneralBOImpl implements IGeneralBO{

	@Autowired
	private TiposIdentificacionDAO objTiposIdenticacionDAO;
	@Autowired
	private GenerosDAO objGeneroDAO;
	
	@Override
	public List<TiposIdentificacion> findAllTiposIdentificacion() throws BOException {
		
		return objTiposIdenticacionDAO.findAll();
	}
	
	@Override
	public List<Generos> findAllGeneros() throws BOException {
		// TODO Auto-generated method stub
		return objGeneroDAO.findAll();
	}
}
