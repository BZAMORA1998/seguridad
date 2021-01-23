package com.sistema.ventas.bo;

import java.util.List;

import com.sistema.ventas.dto.FotosDTO;
import com.sistema.ventas.exceptions.BOException;

public interface IFotosBO {

	public List<FotosDTO>  consultarFotos(Integer intCodigoEmpresa, String strNemonico, String strNombre)throws BOException;
}
