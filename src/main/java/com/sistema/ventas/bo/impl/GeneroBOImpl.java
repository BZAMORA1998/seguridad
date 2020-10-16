package com.sistema.ventas.bo.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sistema.ventas.bo.IGeneroBO;
import com.sistema.ventas.dao.IGeneroDAO;
import com.sistema.ventas.exceptions.BOException;
import com.sistema.ventas.model.Genero;

@Service
public class GeneroBOImpl implements IGeneroBO{

	@Autowired
	private IGeneroDAO objIGeneroDAO;
	
	@Override
	public List<Genero> findAll() throws BOException {
		// TODO Auto-generated method stub
		return objIGeneroDAO.findAll();
	}

}
