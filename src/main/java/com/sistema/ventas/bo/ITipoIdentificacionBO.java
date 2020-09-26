package com.sistema.ventas.bo;

import java.util.List;

import com.sistema.ventas.exceptions.BOException;
import com.sistema.ventas.model.TiposIdentificacion;

public interface ITipoIdentificacionBO {

	public List<TiposIdentificacion> findAll() throws BOException;

}
