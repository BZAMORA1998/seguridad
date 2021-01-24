package com.sistema.ventas.bo;

import java.util.List;

import com.sistema.ventas.dto.EmpresaDTO;
import com.sistema.ventas.dto.InfoEmpresaDTO;
import com.sistema.ventas.exceptions.BOException;

public interface IEmpresasBO {

	public List<InfoEmpresaDTO> infoEmpresa(Integer intCodigoEmpresa, String strVariable) throws BOException ;

	public EmpresaDTO consultarEmpresa(Integer intCodigoEmpresa) throws BOException;
}
