package com.sistema.ventas.bo;

import java.util.List;
import java.util.Map;

import com.sistema.ventas.dto.CiudadDTO;
import com.sistema.ventas.dto.ProvinciaDTO;
import com.sistema.ventas.exceptions.BOException;
import com.sistema.ventas.model.Generos;
import com.sistema.ventas.model.Pais;
import com.sistema.ventas.model.TiposIdentificacion;

public interface IGeneralBO {

	/**
	 * Consulta todos los tipos de identificacion
	 * 
	 * @author Bryan Zamora
	 * @return
	 * @throws BOException
	 */
	public List<TiposIdentificacion> findAllTiposIdentificacion() throws BOException;
	
	/**
	 * Consulta todos los tipos de generos
	 * 
	 * @author Bryan Zamora
	 * @return
	 * @throws BOException
	 */
	public List<Generos> findAllGeneros() throws BOException ;

	/**
	 * Consulta todos los paises
	 * 
	 * @author Bryan Zamora
	 * @return
	 * @throws BOException
	 */
	public List<Pais> findAllPais()throws BOException ;
	
	/**
	 * Consulta todos los paises
	 * 
	 * @author Bryan Zamora
	 * @param intSecuenciaPais
	 * @return
	 * @throws BOException
	 */
	public List<ProvinciaDTO> findAllProvincia(Integer intSecuenciaPais)throws BOException ;
	
	/**
	 * Consulta todos los paises
	 * 
	 * @author Bryan Zamora
	 * @param intSecuenciaPais
	 * @param intSecuenciaProvincia
	 * @return
	 * @throws BOException
	 */
	public List<CiudadDTO> findAllCiudad(Integer intSecuenciaPais,Integer intSecuenciaProvincia)throws BOException ;

	/**
	 * Consulta edad
	 * 
	 * @author Bryan Zamora
	 * @param strLanguage
	 * @param strFechaNacimiento
	 * @return
	 * @throws BOException
	 */
	public Map<String,Object> consultarFechaNacimiento(String strLanguage, String strFechaNacimiento)throws BOException;

}
