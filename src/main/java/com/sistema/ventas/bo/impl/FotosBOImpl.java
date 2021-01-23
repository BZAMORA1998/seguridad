package com.sistema.ventas.bo.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.sistema.ventas.bo.IFotosBO;
import com.sistema.ventas.dao.FotosDAO;
import com.sistema.ventas.dto.FotosDTO;
import com.sistema.ventas.exceptions.BOException;

@Service
public class FotosBOImpl implements IFotosBO{
	
	@Autowired
	private FotosDAO objFotosDAO;
	
	@Override
	public List<FotosDTO> consultarFotos(Integer intCodigoEmpresa,String strNemonico,String strNombre) throws BOException {
		// Valida que el campo CodigoEmpresas diferente a null.
		if (ObjectUtils.isEmpty(intCodigoEmpresa)){
			throw new BOException("ven.warn.campoObligatorio", new Object[] { "ven.campos.secuenciaEmpresa" });
		}
		return objFotosDAO.consultarFotos(intCodigoEmpresa,strNemonico,strNombre);
	}
}
