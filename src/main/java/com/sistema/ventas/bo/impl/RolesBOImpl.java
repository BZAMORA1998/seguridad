package com.sistema.ventas.bo.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sistema.ventas.bo.IRolesBO;
import com.sistema.ventas.dao.RolesDAO;
import com.sistema.ventas.dao.RutasXRolesDAO;
import com.sistema.ventas.dao.UsuariosDAO;
import com.sistema.ventas.dto.ConsultarRolesDTO;
import com.sistema.ventas.dto.ConsultarRolesRutaUsuarioDTO;
import com.sistema.ventas.dto.GuardarRolesDTO;
import com.sistema.ventas.exceptions.BOException;
import com.sistema.ventas.model.Roles;
import com.sistema.ventas.model.RutasXRoles;
import com.sistema.ventas.model.RutasXRolesCPK;
import com.sistema.ventas.model.Usuarios;
import com.sistema.ventas.util.StringUtil;

@Service
public class RolesBOImpl implements IRolesBO{

	@Autowired
	private RolesDAO objRolesDAO;
	@Autowired
	private UsuariosDAO objUsuariosDAO;
	@Autowired
    private RutasXRolesDAO objRutasXRolesDAO;
	

	@Override
	public List<ConsultarRolesDTO> consultarRolesUsuario(String username) throws BOException {
		
		Usuarios objUsuario=objUsuariosDAO.consultarUsuarioSistemaPorCorreo(username);
		
		return objRolesDAO.consultarRolesUsuario(objUsuario.getSecuenciaUsuario());
	}


	@Override
	public List<ConsultarRolesDTO> consultarRolesRuta(String strRuta) throws BOException {
		
		if (ObjectUtils.isEmpty(strRuta)) 
			throw new BOException("ven.warn.campoObligatorio", new Object[] { "ven.campos.ruta"});
		
		return objRolesDAO.consultarRolesRuta(strRuta);
	}


	@Override
	public List<ConsultarRolesRutaUsuarioDTO> consultarRolesRutaUsuario(String username, Integer intSecuenciaRol)
			throws BOException {
		
		if (ObjectUtils.isEmpty(intSecuenciaRol)) 
			throw new BOException("ven.warn.campoObligatorio", new Object[] { "ven.campos.secuenciaRol"});
		
		Usuarios objUsuario=objUsuariosDAO.consultarUsuarioSistemaPorCorreo(username);
		
		return objRolesDAO.consultarRolesRutaUsuario(intSecuenciaRol,objUsuario.getSecuenciaUsuario(),true,null);
	}


	@Override
	public List<ConsultarRolesDTO> consultarRoles() throws BOException {
		return objRolesDAO.consultarRoles();
	}


	@Override
	@Transactional
	public void guardaRolesPorUrl(List<Integer> lsSecuenciaRutas, Integer intSecuenciaRol, String username) throws BOException {
		
		Date dateFechaActual=new Date();
		
		//Requiere la secuencia de rutas
		if (ObjectUtils.isEmpty(intSecuenciaRol)) 
			throw new BOException("ven.warn.campoObligatorio", new Object[] { "ven.campos.secuenciaRol"});
		
		List<RutasXRoles> lsRutasXRoles =objRutasXRolesDAO.findAllPorRol(intSecuenciaRol);
		
		for(RutasXRoles objRol:lsRutasXRoles) {
			objRol.setEsActivo("N");
			objRol.setFechaActualizacion(dateFechaActual);
			objRol.setUsuarioIngreso(username);
			objRutasXRolesDAO.update(objRol);
		}
		
		//Requiere los roles
		if (!ObjectUtils.isEmpty(lsSecuenciaRutas)) {
			Optional<RutasXRoles> optRutasXRoles;
			RutasXRoles objRutasXRoles;
			for(Integer intRuta:lsSecuenciaRutas) {
				optRutasXRoles=objRutasXRolesDAO.find(new RutasXRolesCPK(intSecuenciaRol,intRuta));
				
				if(!optRutasXRoles.isPresent()) {
					
					objRutasXRoles=new RutasXRoles();
					objRutasXRoles.setRutasXRolesCPK(new RutasXRolesCPK(intSecuenciaRol,intRuta));
					objRutasXRoles.setEsActivo("S");
					objRutasXRoles.setEsSelect("S");
					objRutasXRoles.setFechaIngreso(dateFechaActual);
					objRutasXRoles.setUsuarioIngreso(username);
					objRutasXRolesDAO.persist(objRutasXRoles);
					
				}else {
					
					if("N".equalsIgnoreCase(optRutasXRoles.get().getEsActivo())) {
						optRutasXRoles.get().setEsActivo("S");
						optRutasXRoles.get().setEsSelect("S");
						optRutasXRoles.get().setFechaActualizacion(dateFechaActual);
						optRutasXRoles.get().setUsuarioIngreso(username);
						objRutasXRolesDAO.update(optRutasXRoles.get());
					}
					
				}
			}
			
		}

	}


	@Override
	@Transactional
	public void crearRol(String strNombre,String username) throws BOException {
		
		if (ObjectUtils.isEmpty(strNombre)) 
			throw new BOException("ven.warn.campoObligatorio", new Object[] { "ven.campos.nombre"});
		
		Boolean booExiste=objRolesDAO.consultarRolesPorNombre(strNombre);
		
		if(booExiste)
			throw new BOException("ven.warn.nombreRolExiste");
		
		Roles objRol=new Roles();
		objRol.setNombre(StringUtil.eliminarAcentos(strNombre.toUpperCase().trim()));
		objRol.setEsActivo("S");
		objRolesDAO.persist(objRol);
	}


}
