package com.sistema.ventas.bo;

import java.util.List;

import com.sistema.ventas.dto.OpcionesMenuDTO;
import com.sistema.ventas.exceptions.BOException;
import com.sistema.ventas.model.Generos;
import com.sistema.ventas.model.TiposIdentificacion;

public interface IGeneralBO {

	public List<TiposIdentificacion> findAllTiposIdentificacion() throws BOException;
	public List<Generos> findAllGeneros() throws BOException ;
	public List<OpcionesMenuDTO> opcionesMenu(String strNemonico, Integer intCodigoEmpresa) throws BOException;
}
