package com.sistema.ventas.bo.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.sistema.ventas.bo.IUsuariosBO;
import com.sistema.ventas.dao.GenerosDAO;
import com.sistema.ventas.dao.PersonasDAO;
import com.sistema.ventas.dao.RolesDAO;
import com.sistema.ventas.dao.TiposIdentificacionDAO;
import com.sistema.ventas.dao.UsuariosDAO;
import com.sistema.ventas.dto.ConsultarUsuarioDTO;
import com.sistema.ventas.dto.UsuariosDTO;
import com.sistema.ventas.enums.AlgoritmosIdentificacion;
import com.sistema.ventas.enums.FormatoFecha;
import com.sistema.ventas.enums.TipoIdentificacion;
import com.sistema.ventas.exceptions.BOException;
import com.sistema.ventas.model.Generos;
import com.sistema.ventas.model.Personas;
import com.sistema.ventas.model.Roles;
import com.sistema.ventas.model.TiposIdentificacion;
import com.sistema.ventas.model.Usuarios;
import com.sistema.ventas.util.GeneralUtil;

@Service
public class UsuariosBOImpl implements IUsuariosBO{
	
	@Autowired
	private PersonasDAO objPersonasDAO;
	@Autowired
	private UsuariosDAO objUsuariosDAO;
	@Autowired
	private GenerosDAO objGenerosDAO;
	@Autowired
	private TiposIdentificacionDAO objTiposIdentificacionDAO;
	@Autowired
	private RolesDAO objRolesDAO;
	
	@Override
	public Map<String,Object> crearUsuario(UsuariosDTO objUsuariosDTO) throws BOException {
		
		Usuarios objUsuario=null;
		
		// primerNombre.
		if (ObjectUtils.isEmpty(objUsuariosDTO.getPrimerNombre())) 
			throw new BOException("ven.warn.campoObligatorio", new Object[] { "ven.campos.primerNombre"});
		
		// primerApellido.
		if (ObjectUtils.isEmpty(objUsuariosDTO.getPrimerApellido())) 
			throw new BOException("ven.warn.campoObligatorio", new Object[] { "ven.campos.primerApellido"});
		
		// codigoIdentificacion
		if (ObjectUtils.isEmpty(objUsuariosDTO.getCodigoTipoIdentificacion())) 
			throw new BOException("ven.warn.campoObligatorio", new Object[] { "ven.campos.codigoTipoIdentificacion"});
		
		Optional<TiposIdentificacion> objTiposIdentificacion=objTiposIdentificacionDAO.find(objUsuariosDTO.getCodigoTipoIdentificacion());
		
		if(!objTiposIdentificacion.isPresent()) 
			throw new BOException("ven.warn.tipoIdentificacionNoExiste");
		
		if(!("S").equalsIgnoreCase(objTiposIdentificacion.get().getEsActivo())) 
			throw new BOException("ven.warn.tipoIdentificacionInactivo");
		
		// codigoIdentificacion
		if (ObjectUtils.isEmpty(objUsuariosDTO.getNumeroIdentificacion())) 
			throw new BOException("ven.warn.campoObligatorio", new Object[] { "ven.campos.numeroIdentificacion"});

		Boolean booNumeroIdentificacion=false;
		
		if(TipoIdentificacion.CEDULA.getValor().equals(objUsuariosDTO.getCodigoTipoIdentificacion())) 
			booNumeroIdentificacion=GeneralUtil.validaAlgoritmoIdentificacion(objUsuariosDTO.getNumeroIdentificacion(), AlgoritmosIdentificacion.CEDULA_IDENTIDAD_EC.getName());
		else if(TipoIdentificacion.RUC.getValor().equals(objUsuariosDTO.getCodigoTipoIdentificacion())) 
			booNumeroIdentificacion=GeneralUtil.validaAlgoritmoIdentificacion(objUsuariosDTO.getNumeroIdentificacion(), AlgoritmosIdentificacion.REGISTRO_UNICO_CONTRIBUYENTE_EC.getName());
		
		if(!booNumeroIdentificacion) 
			throw new BOException("ven.warn.numeroIdentificacionInvalida");
		
		objUsuario=objUsuariosDAO.consultarUsuarioSistemaPorCedula(objUsuariosDTO.getNumeroIdentificacion());
		
		if(!ObjectUtils.isEmpty(objUsuario)) 
			throw new BOException("ven.warn.numeroIdentificacionExiste");
		
		// codigoGenero.
		if (ObjectUtils.isEmpty(objUsuariosDTO.getCodigoGenero())) 
			throw new BOException("ven.warn.campoObligatorio", new Object[] { "ven.campos.codigoGenero"});
		
		Optional<Generos> objGenero=objGenerosDAO.find(objUsuariosDTO.getCodigoGenero());
		
		if(!objGenero.isPresent()) 
			throw new BOException("ven.warn.generoNoExiste");
		
		if(!("S").equalsIgnoreCase(objGenero.get().getEsActivo())) 
			throw new BOException("ven.warn.generoInactivo");
		
		// fechaNacimiento.
		if (ObjectUtils.isEmpty(objUsuariosDTO.getFechaNacimiento())) 
			throw new BOException("ven.warn.campoObligatorio", new Object[] { "ven.campos.fechaNacimiento"});
		
		// Usuario.
		if (ObjectUtils.isEmpty(objUsuariosDTO.getUser())) 
			throw new BOException("ven.warn.campoObligatorio", new Object[] { "ven.campos.usuario"});
		
		objUsuario=objUsuariosDAO.consultarUsuarioSistema(objUsuariosDTO.getUser());
		
		if(objUsuario!=null)
			throw new BOException("ven.warn.usuarioExiste", new Object[] {objUsuariosDTO.getUser()});
		
		// Contraseña.
		if (ObjectUtils.isEmpty(objUsuariosDTO.getPassword())) 
			throw new BOException("ven.warn.campoObligatorio", new Object[] {"ven.campos.contraseña"});
		
		Personas objPersona=new Personas();
		objPersona.setPrimerNombre(objUsuariosDTO.getPrimerNombre().toUpperCase());
		if(ObjectUtils.isEmpty(objPersona.getSegundoNombre()))
			objPersona.setSegundoNombre(objUsuariosDTO.getSegundoNombre().toUpperCase());
		objPersona.setPrimerApellido(objUsuariosDTO.getPrimerApellido().toUpperCase());
		if(!ObjectUtils.isEmpty(objPersona.getSegundoApellido()!=null))
			objPersona.setSegundoApellido(objUsuariosDTO.getSegundoNombre().toUpperCase());
		objPersona.setFechaNacimiento(GeneralUtil.stringToDate(objUsuariosDTO.getFechaNacimiento(),FormatoFecha.YYYY_MM_DD_GUION));
		objPersona.setTiposIdentificacion(objTiposIdentificacion.get());
		objPersona.setNumeroIdentificacion(objUsuariosDTO.getNumeroIdentificacion());
		objPersona.setGenero(objGenero.get());
		objPersona.setEsActivo("S");
		objPersonasDAO.persist(objPersona);
		
		Usuarios objUsuariosSistema=new Usuarios();
		objUsuariosSistema.setUsuario(objUsuariosDTO.getUser().toUpperCase());
		objUsuariosSistema.setContrasenia(objUsuariosDTO.getPassword());
		objUsuariosSistema.setPersonas(objPersona);
		objUsuariosSistema.setEsActivo("S");
		
		Optional<Roles> objRolSistema =objRolesDAO.find(2);
		
		objUsuariosSistema.setRoles(objRolSistema.get());
		
		objUsuariosDAO.persist(objUsuariosSistema);
		
		Map<String,Object> objMap=new HashMap<String,Object>();
		objMap.put("secuenciaPersona",objPersona.getSecuenciaPersona());
		return objMap;
	}
	
	@Override
	public void actualizarUsuario(Integer intIdUsuario,UsuariosDTO objUsuariosDTO) throws BOException {
		Usuarios objUsuario=null;
		Optional<TiposIdentificacion> objTiposIdentificacion=null;
		
		// codigoIdentificacion
		if (!ObjectUtils.isEmpty(objUsuariosDTO.getCodigoTipoIdentificacion())) { 
		
			objTiposIdentificacion=objTiposIdentificacionDAO.find(objUsuariosDTO.getCodigoTipoIdentificacion());
			
			if(!objTiposIdentificacion.isPresent()) 
				throw new BOException("ven.warn.tipoIdentificacionNoExiste");
			
			if(!("S").equalsIgnoreCase(objTiposIdentificacion.get().getEsActivo())) 
				throw new BOException("ven.warn.tipoIdentificacionInactivo");
		}
		
		// codigoIdentificacion
		if (!ObjectUtils.isEmpty(objUsuariosDTO.getNumeroIdentificacion())) {
			Boolean booNumeroIdentificacion=false;
			
			if(TipoIdentificacion.CEDULA.getValor().equals(objUsuariosDTO.getCodigoTipoIdentificacion())) 
				booNumeroIdentificacion=GeneralUtil.validaAlgoritmoIdentificacion(objUsuariosDTO.getNumeroIdentificacion(), AlgoritmosIdentificacion.CEDULA_IDENTIDAD_EC.getName());
			else if(TipoIdentificacion.RUC.getValor().equals(objUsuariosDTO.getCodigoTipoIdentificacion())) 
				booNumeroIdentificacion=GeneralUtil.validaAlgoritmoIdentificacion(objUsuariosDTO.getNumeroIdentificacion(), AlgoritmosIdentificacion.REGISTRO_UNICO_CONTRIBUYENTE_EC.getName());
			
			if(!booNumeroIdentificacion) 
				throw new BOException("ven.warn.numeroIdentificacionInvalida");
		}
		
		if(!ObjectUtils.isEmpty(objUsuariosDTO.getNumeroIdentificacion())) {
			objUsuario=objUsuariosDAO.consultarUsuarioSistemaPorCedula(objUsuariosDTO.getNumeroIdentificacion());
			
			if(ObjectUtils.isEmpty(objUsuario)) 
				throw new BOException("ven.warn.numeroIdentificacionNoExiste");
		}
		
		// codigoGenero.
		Optional<Generos> objGenero=null;
		if (!ObjectUtils.isEmpty(objUsuariosDTO.getCodigoGenero())) {
			
			objGenero=objGenerosDAO.find(objUsuariosDTO.getCodigoGenero());
			
			if(!objGenero.isPresent()) 
				throw new BOException("ven.warn.generoNoExiste");
			
			if(!("S").equalsIgnoreCase(objGenero.get().getEsActivo())) 
				throw new BOException("ven.warn.generoInactivo");
		}
		
		// Usuario.
		if (!ObjectUtils.isEmpty(objUsuariosDTO.getUser())) {
		
			objUsuario=objUsuariosDAO.consultarUsuarioSistema(objUsuariosDTO.getUser());
			
			if(objUsuario!=null)
				throw new BOException("ven.warn.usuarioExiste", new Object[] {objUsuariosDTO.getUser()});
		}
		
		Optional<Usuarios> objUsuariosSistema=objUsuariosDAO.find(intIdUsuario);
		
		if(!objUsuariosSistema.isPresent())
			throw new BOException("ven.warn.usuarioNoExiste");
		
		if(!ObjectUtils.isEmpty(objUsuariosDTO.getUser()))
			objUsuariosSistema.get().setUsuario(objUsuariosDTO.getUser().toUpperCase());
		
		if(!ObjectUtils.isEmpty(objUsuariosDTO.getPassword()))
			objUsuariosSistema.get().setContrasenia(objUsuariosDTO.getPassword());
		
		Optional<Personas> objPersona=objPersonasDAO.find(objUsuariosSistema.get().getPersonas().getSecuenciaPersona());
		if (!ObjectUtils.isEmpty(objUsuariosDTO.getPrimerNombre()))
			objPersona.get().setPrimerNombre(objUsuariosDTO.getPrimerNombre().toUpperCase());
		
		if(!ObjectUtils.isEmpty(objUsuariosDTO.getSegundoNombre()))
			objPersona.get().setSegundoNombre(objUsuariosDTO.getSegundoNombre().toUpperCase());
		
		if(!ObjectUtils.isEmpty(objUsuariosDTO.getPrimerApellido()))
			objPersona.get().setPrimerApellido(objUsuariosDTO.getPrimerApellido().toUpperCase());
		
		if(!ObjectUtils.isEmpty(objUsuariosDTO.getSegundoApellido()))
			objPersona.get().setSegundoApellido(objUsuariosDTO.getSegundoApellido().toUpperCase());
		
		if(!ObjectUtils.isEmpty(objUsuariosDTO.getFechaNacimiento()))
			objPersona.get().setFechaNacimiento(GeneralUtil.stringToDate(objUsuariosDTO.getFechaNacimiento(),FormatoFecha.YYYY_MM_DD_GUION));
		
		if(!ObjectUtils.isEmpty(objTiposIdentificacion.get()))
			objPersona.get().setTiposIdentificacion(objTiposIdentificacion.get());
		
		if(!ObjectUtils.isEmpty(objUsuariosDTO.getNumeroIdentificacion()))
			objPersona.get().setNumeroIdentificacion(objUsuariosDTO.getNumeroIdentificacion());
		
		if(!ObjectUtils.isEmpty(objGenero.get()))
			objPersona.get().setGenero(objGenero.get());
		
		objPersona.get().setEsActivo("S");
		
		objUsuariosSistema.get().setPersonas(objPersona.get());
		objUsuariosSistema.get().setEsActivo("S");
		
		objUsuariosDAO.persist(objUsuariosSistema.get());
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String,Object> consultarUsuarios(Integer intPage, Integer intPerPage) throws BOException {
		List<ConsultarUsuarioDTO> lsConsultarUsuarioDTO=new ArrayList<ConsultarUsuarioDTO>();
				
		List<Usuarios> lsUsuario=objUsuariosDAO.consultarUsuarioSistema(intPage,intPerPage);
		Long lngUsuario=objUsuariosDAO.contarConsultarUsuarioSistema();
		ConsultarUsuarioDTO objConsultarUsuarioDTO=null;
		
		for(Usuarios objUsuario:lsUsuario) {
			objConsultarUsuarioDTO=new ConsultarUsuarioDTO();
			objConsultarUsuarioDTO.setSecuenciaUsuarioSistema(objUsuario.getSecuenciaUsuario());
			objConsultarUsuarioDTO.setNumeroIdentificacion(objUsuario.getPersonas().getNumeroIdentificacion());
			objConsultarUsuarioDTO.setPrimerNombre(objUsuario.getPersonas().getPrimerNombre());
			objConsultarUsuarioDTO.setSegundoNombre(objUsuario.getPersonas().getSegundoNombre());
			objConsultarUsuarioDTO.setPrimerApellido(objUsuario.getPersonas().getPrimerApellido());
			objConsultarUsuarioDTO.setSegundoApellido(objUsuario.getPersonas().getSegundoApellido());
			objConsultarUsuarioDTO.setUsuario(objUsuario.getUsuario());
			lsConsultarUsuarioDTO.add(objConsultarUsuarioDTO);
		}

		Map<String, Object> mapResult = new HashMap();
		mapResult.put("rows",lsConsultarUsuarioDTO);
		mapResult.put("totalRows",lngUsuario);
		
		return mapResult;
	}

	@Override
	public void eliminarUsuario(Integer intIdUsuario) throws BOException {
		
		//Valida que el campo usuario sea obligatorio
		if (ObjectUtils.isEmpty(intIdUsuario)) 
			throw new BOException("ven.warn.campoObligatorio", new Object[] {"ven.campos.idUsuario"});
		
		Optional<Usuarios> objUsuario=objUsuariosDAO.find(intIdUsuario);
		objUsuario.get().setEsActivo("N");
		
		if(!objUsuario.isPresent())
			throw new BOException("ven.warn.idUsuarioNoExiste");
		
		if(!("S").equalsIgnoreCase(objUsuario.get().getEsActivo()))
			throw new BOException("ven.warn.idUsuarioInactivo");
		
		//Elimina el usuario
		objUsuariosDAO.update(objUsuario.get());
		
		
		//Elimina a la persona
		Optional<Personas> objPersonas =objPersonasDAO.find(objUsuario.get().getPersonas().getSecuenciaPersona());
		objPersonas.get().setEsActivo("N");
		objPersonasDAO.update(objPersonas.get());
		
	}

	@Override
	public ConsultarUsuarioDTO consultarUsuarioXId(Integer intIdUsuario) throws BOException {
		
		//Valida que el campo usuario sea obligatorio
		if (ObjectUtils.isEmpty(intIdUsuario)) 
			throw new BOException("ven.warn.campoObligatorio", new Object[] {"ven.campos.idUsuario"});
		
		Optional<Usuarios> objUsuario=objUsuariosDAO.find(intIdUsuario);
		
		if(!objUsuario.isPresent())
			throw new BOException("ven.warn.idUsuarioNoExiste");
		
		if(!("S").equalsIgnoreCase(objUsuario.get().getEsActivo()))
			throw new BOException("ven.warn.idUsuarioInactivo");
		
		ConsultarUsuarioDTO objUsuarioDTO=new ConsultarUsuarioDTO();
		objUsuarioDTO.setSecuenciaUsuarioSistema(objUsuario.get().getSecuenciaUsuario());
		if(objUsuario.get().getPersonas()!=null) {
			objUsuarioDTO.setCodigoTipoIdentificacion(objUsuario.get().getPersonas().getTiposIdentificacion().getSecuenciaTipoIdentificacion());
			objUsuarioDTO.setNumeroIdentificacion(objUsuario.get().getPersonas().getNumeroIdentificacion());
			objUsuarioDTO.setPrimerNombre(objUsuario.get().getPersonas().getPrimerNombre());
			objUsuarioDTO.setSegundoNombre(objUsuario.get().getPersonas().getSegundoNombre());
			objUsuarioDTO.setPrimerApellido(objUsuario.get().getPersonas().getPrimerApellido());
			objUsuarioDTO.setSegundoApellido(objUsuario.get().getPersonas().getSegundoApellido());
			objUsuarioDTO.setFechaNacimiento(GeneralUtil.dateToString(objUsuario.get().getPersonas().getFechaNacimiento(),FormatoFecha.YYYY_MM_DD_GUION));
			objUsuarioDTO.setCodigoGenero(objUsuario.get().getPersonas().getGenero().getSecuenciaGenero());
		}
		
		if(objUsuario.get().getRoles()!=null)
			objUsuarioDTO.setRolSistema(objUsuario.get().getRoles().getAbreviatura());
		
		return objUsuarioDTO;
	}

	@Override
	public void guardarPhoto(MultipartFile photo,Integer intIdPersona)throws BOException, IOException{
		
		System.out.print("=>"+photo.getBytes());
		
		//Valida que el campo usuario sea obligatorio
		if (ObjectUtils.isEmpty(intIdPersona)) 
			throw new BOException("ven.warn.campoObligatorio", new Object[] {"ven.campos.idPersona"});
		
		Optional<Personas> objPersona=objPersonasDAO.find(intIdPersona);
		
		if(!objPersona.isPresent())
			throw new BOException("ven.warn.idPersonaNoExiste");
		
		if(!("S").equalsIgnoreCase(objPersona.get().getEsActivo()))
			throw new BOException("ven.warn.idPersonaInactivo");
		
		objPersona.get().setFoto(photo.getBytes());
		objPersonasDAO.persist(objPersona.get());
	}
}
