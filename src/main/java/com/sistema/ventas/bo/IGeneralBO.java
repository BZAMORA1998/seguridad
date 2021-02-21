package com.sistema.ventas.bo;

import java.util.List;

import com.sistema.ventas.exceptions.BOException;
import com.sistema.ventas.model.Generos;
import com.sistema.ventas.model.TiposIdentificacion;

public interface IGeneralBO {

	/**
	 * Busca los tipos de identificacion
	 * 
	 * @author Bryan Zamora
	 * @return
	 * @throws BOException
	 */
	public List<TiposIdentificacion> findAllTiposIdentificacion() throws BOException;
	
	/**
	 * Busca los tipos de generos
	 * 
	 * @author Bryan Zamora
	 * @return
	 * @throws BOException
	 */
	public List<Generos> findAllGeneros() throws BOException ;
}
