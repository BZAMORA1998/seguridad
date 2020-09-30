package com.sistema.ventas.bo;

import com.sistema.ventas.dto.AutenticacionDTO;
import com.sistema.ventas.exceptions.BOException;


public interface IAutenticacionBO {

	public AutenticacionDTO login(String strAuth) throws BOException;

}
