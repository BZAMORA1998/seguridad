package com.sistema.ventas.bo.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sistema.ventas.bo.IRolesBO;
import com.sistema.ventas.dao.ModuloXRolesDAO;
import com.sistema.ventas.dao.ModulosDAO;
import com.sistema.ventas.dao.RolesDAO;
import com.sistema.ventas.dao.RutasXRolesDAO;
import com.sistema.ventas.dao.UsuariosDAO;
import com.sistema.ventas.dto.ConsultarRolesDTO;
import com.sistema.ventas.dto.ConsultarRolesRutaUsuarioDTO;
import com.sistema.ventas.dto.CrearRolDTO;
import com.sistema.ventas.exceptions.BOException;
import com.sistema.ventas.model.ModuloXRoles;
import com.sistema.ventas.model.ModuloXRolesCPK;
import com.sistema.ventas.model.Modulos;
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
	@Autowired
    private ModulosDAO objModulosDAO;
	@Autowired
    private ModuloXRolesDAO objModuloXRolesDAO;

	@Override
	public List<ConsultarRolesDTO> consultarRolesUsuario(String username) throws BOException {
		
		Usuarios objUsuario=objUsuariosDAO.consultarUsuarioSistemaPorCorreo(username);
		
		return objRolesDAO.consultarRolesUsuario(objUsuario.getSecuenciaUsuario());
	}
	
	@Override
	public List<ConsultarRolesDTO> consultarRolesNoUsuario(Integer intIdUsuario,String username) throws BOException {
		
		//Valida que el campo usuario sea obligatorio
		if (ObjectUtils.isEmpty(intIdUsuario)) 
			throw new BOException("ven.warn.campoObligatorio", new Object[] {"ven.campos.idUsuario"});
		
		return objRolesDAO.consultarRolesNoUsuario(intIdUsuario);
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
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class})
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
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class})
	public void crearRol(CrearRolDTO objCrearRol,String strUsername) throws BOException {
		
		if (ObjectUtils.isEmpty(objCrearRol.getNombre())) 
			throw new BOException("ven.warn.campoObligatorio", new Object[] { "ven.campos.nombre"});
		
		Boolean booExiste=objRolesDAO.consultarRolesPorNombre(objCrearRol.getNombre());
		
		if(booExiste)
			throw new BOException("ven.warn.nombreRolExiste");
		
		if (ObjectUtils.isEmpty(objCrearRol.getSecuenciaModulo())) 
			throw new BOException("ven.warn.campoObligatorio", new Object[] { "ven.campos.secuenciaModulo"});
		
		Optional<Modulos> objModulos=objModulosDAO.find(objCrearRol.getSecuenciaModulo());
		
		if(!objModulos.isPresent())
			throw new BOException("ven.warn.campoNoExiste", new Object[] { "ven.campos.secuenciaModulo"});
		
		if("N".equalsIgnoreCase(objModulos.get().getEsActivo()))
			throw new BOException("ven.warn.campoInactivo", new Object[] { "ven.campos.secuenciaModulo"});

		Roles objRol=new Roles();
		objRol.setNombre(StringUtil.eliminarAcentos(objCrearRol.getNombre().trim()));
		objRol.setEsActivo("S");
		objRolesDAO.persist(objRol);
		
		ModuloXRoles objModuloXRol=new ModuloXRoles();
		objModuloXRol.setModuloXRolesCPK(new ModuloXRolesCPK(objRol.getSecuenciaRol(),objCrearRol.getSecuenciaModulo()));
		objModuloXRol.setEsActivo("S");
		objModuloXRol.setFechaIngreso(new Date());
		objModuloXRol.setUsuarioIngreso(strUsername);
		objModuloXRolesDAO.persist(objModuloXRol);
		
	}


}
