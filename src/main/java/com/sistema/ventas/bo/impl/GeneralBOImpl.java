package com.sistema.ventas.bo.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.sistema.ventas.bo.IGeneralBO;
import com.sistema.ventas.dao.GenerosDAO;
import com.sistema.ventas.dao.OpcionesMenuDAO;
import com.sistema.ventas.dao.TiposIdentificacionDAO;
import com.sistema.ventas.dto.OpcionesMenuDTO;
import com.sistema.ventas.exceptions.BOException;
import com.sistema.ventas.model.Generos;
import com.sistema.ventas.model.OpcionesMenu;
import com.sistema.ventas.model.TiposIdentificacion;

@Service
public class GeneralBOImpl implements IGeneralBO{

	@Autowired
	private TiposIdentificacionDAO objTiposIdenticacionDAO;
	@Autowired
	private GenerosDAO objGeneroDAO;
	@Autowired
	private OpcionesMenuDAO objOpcionesMenuDAO;
	
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
	public List<OpcionesMenuDTO> opcionesMenu(String strNemonico, Integer intCodigoEmpresa) throws BOException {
		
		// Valida que el campo CodigoEmpresas diferente a null.
		if (ObjectUtils.isEmpty(intCodigoEmpresa)){
			throw new BOException("ven.warn.campoObligatorio", new Object[] { "ven.campos.secuenciaEmpresa" });
		}
		
		// Valida que el campo strNemonico diferente a null.
		if (ObjectUtils.isEmpty(strNemonico)) {
			throw new BOException("ven.warn.campoObligatorio", new Object[] { "ven.campos.nemonico" });
		}
		
		 List<OpcionesMenu> lsOpcionesMenu =objOpcionesMenuDAO.findByNemonic(strNemonico,intCodigoEmpresa);
		
		 List<OpcionesMenuDTO>lsOpcionesMenuDTO=new ArrayList<OpcionesMenuDTO>();
		 OpcionesMenuDTO objOpcionesMenuDTO=null;
		 for(OpcionesMenu objOpcionesMenu:lsOpcionesMenu) {
			 objOpcionesMenuDTO=new OpcionesMenuDTO();
			 objOpcionesMenuDTO.setNombre(objOpcionesMenu.getOpcionesMenuCPK().getNombre());
			 objOpcionesMenuDTO.setUrl(objOpcionesMenu.getUrl());
			 lsOpcionesMenuDTO.add(objOpcionesMenuDTO);
		 }
		 
		return lsOpcionesMenuDTO;
	}
}
