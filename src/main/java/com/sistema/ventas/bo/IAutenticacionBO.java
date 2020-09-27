package com.sistema.ventas.bo;

import java.util.List;

import com.sistema.ventas.dto.AutenticacionDTO;
import com.sistema.ventas.exceptions.BOException;


public interface IAutenticacionBO {

	public AutenticacionDTO login(String strBasic, String strApplication) throws BOException;

}
