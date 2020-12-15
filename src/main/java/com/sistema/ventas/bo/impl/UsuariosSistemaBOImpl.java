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

import com.sistema.ventas.bo.IUsuariosSistemaBO;
import com.sistema.ventas.dao.IGeneroDAO;
import com.sistema.ventas.dao.IPersonasDAO;
import com.sistema.ventas.dao.IRolSistemaDAO;
import com.sistema.ventas.dao.ITiposIdentificacionDAO;
import com.sistema.ventas.dao.IUsuarioSistemaDAO;
import com.sistema.ventas.dao.UsuarioSistemaDAO;
import com.sistema.ventas.dto.ConsultarUsuarioDTO;
import com.sistema.ventas.dto.UsuariosDTO;
import com.sistema.ventas.enums.AlgoritmosIdentificacion;
import com.sistema.ventas.enums.FormatoFecha;
import com.sistema.ventas.enums.TipoIdentificacion;
import com.sistema.ventas.exceptions.BOException;
import com.sistema.ventas.model.Genero;
import com.sistema.ventas.model.Personas;
import com.sistema.ventas.model.RolSistema;
import com.sistema.ventas.model.TiposIdentificacion;
import com.sistema.ventas.model.UsuariosSistema;
import com.sistema.ventas.util.GeneralUtil;

@Service
public class UsuariosSistemaBOImpl implements IUsuariosSistemaBO{
	
	@Autowired
	private IPersonasDAO objIPersonasDAO;
	@Autowired
	private UsuarioSistemaDAO objUsuarioSistemaDAO;
	@Autowired
	private IUsuarioSistemaDAO objIUsuarioSistemaDAO;
	@Autowired
	private IGeneroDAO objIGeneroDAO;
	@Autowired
	private ITiposIdentificacionDAO objITiposIdentificacionDAO;
	@Autowired
	private IRolSistemaDAO objIRolSistemaDAO;
	
	@Override
	public Map<String,Object> crearUsuario(UsuariosDTO objUsuariosDTO) throws BOException {
		
		UsuariosSistema objUsuario=null;
		
		// primerNombre.
		if (ObjectUtils.isEmpty(objUsuariosDTO.getPrimerNombre())) 
			throw new BOException("ven.warn.campoObligatorio", new Object[] { "ven.campos.primerNombre"});
		
		// primerApellido.
		if (ObjectUtils.isEmpty(objUsuariosDTO.getPrimerApellido())) 
			throw new BOException("ven.warn.campoObligatorio", new Object[] { "ven.campos.primerApellido"});
		
		// codigoIdentificacion
		if (ObjectUtils.isEmpty(objUsuariosDTO.getCodigoTipoIdentificacion())) 
			throw new BOException("ven.warn.campoObligatorio", new Object[] { "ven.campos.codigoTipoIdentificacion"});
		
		Optional<TiposIdentificacion> objTiposIdentificacion=objITiposIdentificacionDAO.findById(objUsuariosDTO.getCodigoTipoIdentificacion());
		
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
		
		objUsuario=objUsuarioSistemaDAO.consultarUsuarioSistemaPorCedula(objUsuariosDTO.getNumeroIdentificacion());
		
		if(!ObjectUtils.isEmpty(objUsuario)) 
			throw new BOException("ven.warn.numeroIdentificacionExiste");
		
		// codigoGenero.
		if (ObjectUtils.isEmpty(objUsuariosDTO.getCodigoGenero())) 
			throw new BOException("ven.warn.campoObligatorio", new Object[] { "ven.campos.codigoGenero"});
		
		Optional<Genero> objGenero=objIGeneroDAO.findById(objUsuariosDTO.getCodigoGenero());
		
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
		
		objUsuario=objUsuarioSistemaDAO.consultarUsuarioSistema(objUsuariosDTO.getUser());
		
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
		objIPersonasDAO.save(objPersona);
		
		UsuariosSistema objUsuariosSistema=new UsuariosSistema();
		objUsuariosSistema.setUser(objUsuariosDTO.getUser().toUpperCase());
		objUsuariosSistema.setPassword(objUsuariosDTO.getPassword());
		objUsuariosSistema.setPersonas(objPersona);
		objUsuariosSistema.setEsActivo("S");
		
		Optional<RolSistema> objRolSistema =objIRolSistemaDAO.findById(2);
		
		objUsuariosSistema.setRolSistema(objRolSistema.get());
		
		objIUsuarioSistemaDAO.save(objUsuariosSistema);
		
		Map<String,Object> objMap=new HashMap<String,Object>();
		objMap.put("secuenciaPersona",objPersona.getSecuenciaPersona());
		return objMap;
	}
	
	@Override
	public void actualizarUsuario(Integer intIdUsuario,UsuariosDTO objUsuariosDTO) throws BOException {
		UsuariosSistema objUsuario=null;
		Optional<TiposIdentificacion> objTiposIdentificacion=null;
		
		// codigoIdentificacion
		if (!ObjectUtils.isEmpty(objUsuariosDTO.getCodigoTipoIdentificacion())) { 
		
			objTiposIdentificacion=objITiposIdentificacionDAO.findById(objUsuariosDTO.getCodigoTipoIdentificacion());
			
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
			objUsuario=objUsuarioSistemaDAO.consultarUsuarioSistemaPorCedula(objUsuariosDTO.getNumeroIdentificacion());
			
			if(ObjectUtils.isEmpty(objUsuario)) 
				throw new BOException("ven.warn.numeroIdentificacionNoExiste");
		}
		
		// codigoGenero.
		Optional<Genero> objGenero=null;
		if (!ObjectUtils.isEmpty(objUsuariosDTO.getCodigoGenero())) {
			
			objGenero=objIGeneroDAO.findById(objUsuariosDTO.getCodigoGenero());
			
			if(!objGenero.isPresent()) 
				throw new BOException("ven.warn.generoNoExiste");
			
			if(!("S").equalsIgnoreCase(objGenero.get().getEsActivo())) 
				throw new BOException("ven.warn.generoInactivo");
		}
		
		// Usuario.
		if (!ObjectUtils.isEmpty(objUsuariosDTO.getUser())) {
		
			objUsuario=objUsuarioSistemaDAO.consultarUsuarioSistema(objUsuariosDTO.getUser());
			
			if(objUsuario!=null)
				throw new BOException("ven.warn.usuarioExiste", new Object[] {objUsuariosDTO.getUser()});
		}
		
		Optional<UsuariosSistema> objUsuariosSistema=objIUsuarioSistemaDAO.findById(intIdUsuario);
		
		if(!objUsuariosSistema.isPresent())
			throw new BOException("ven.warn.usuarioNoExiste");
		
		if(!ObjectUtils.isEmpty(objUsuariosDTO.getUser()))
			objUsuariosSistema.get().setUser(objUsuariosDTO.getUser().toUpperCase());
		
		if(!ObjectUtils.isEmpty(objUsuariosDTO.getPassword()))
			objUsuariosSistema.get().setPassword(objUsuariosDTO.getPassword());
		
		Optional<Personas> objPersona=objIPersonasDAO.findById(objUsuariosSistema.get().getPersonas().getSecuenciaPersona());
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
		
		objIUsuarioSistemaDAO.save(objUsuariosSistema.get());
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String,Object> consultarUsuarios(Integer intPage, Integer intPerPage) throws BOException {
		List<ConsultarUsuarioDTO> lsConsultarUsuarioDTO=new ArrayList<ConsultarUsuarioDTO>();
				
		List<UsuariosSistema> lsUsuario=objUsuarioSistemaDAO.consultarUsuarioSistema(intPage,intPerPage);
		Long lngUsuario=objUsuarioSistemaDAO.contarConsultarUsuarioSistema();
		ConsultarUsuarioDTO objConsultarUsuarioDTO=null;
		
		for(UsuariosSistema objUsuario:lsUsuario) {
			objConsultarUsuarioDTO=new ConsultarUsuarioDTO();
			objConsultarUsuarioDTO.setSecuenciaUsuarioSistema(objUsuario.getSecuenciaUsuarioSistema());
			objConsultarUsuarioDTO.setNumeroIdentificacion(objUsuario.getPersonas().getNumeroIdentificacion());
			objConsultarUsuarioDTO.setPrimerNombre(objUsuario.getPersonas().getPrimerNombre());
			objConsultarUsuarioDTO.setSegundoNombre(objUsuario.getPersonas().getSegundoNombre());
			objConsultarUsuarioDTO.setPrimerApellido(objUsuario.getPersonas().getPrimerApellido());
			objConsultarUsuarioDTO.setSegundoApellido(objUsuario.getPersonas().getSegundoApellido());
			objConsultarUsuarioDTO.setUsuario(objUsuario.getUser());
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
		
		Optional<UsuariosSistema> objUsuario=objIUsuarioSistemaDAO.findById(intIdUsuario);
		
		if(!objUsuario.isPresent())
			throw new BOException("ven.warn.idUsuarioNoExiste");
		
		if(!("S").equalsIgnoreCase(objUsuario.get().getEsActivo()))
			throw new BOException("ven.warn.idUsuarioInactivo");
		
		//Elimina el usuario
		objIUsuarioSistemaDAO.deleteById(intIdUsuario);
		//Elimina a la persona
		objIPersonasDAO.deleteById(objUsuario.get().getPersonas().getSecuenciaPersona());
	}

	@Override
	public ConsultarUsuarioDTO consultarUsuarioXId(Integer intIdUsuario) throws BOException {
		
		//Valida que el campo usuario sea obligatorio
		if (ObjectUtils.isEmpty(intIdUsuario)) 
			throw new BOException("ven.warn.campoObligatorio", new Object[] {"ven.campos.idUsuario"});
		
		Optional<UsuariosSistema> objUsuario=objIUsuarioSistemaDAO.findById(intIdUsuario);
		
		if(!objUsuario.isPresent())
			throw new BOException("ven.warn.idUsuarioNoExiste");
		
		if(!("S").equalsIgnoreCase(objUsuario.get().getEsActivo()))
			throw new BOException("ven.warn.idUsuarioInactivo");
		
		ConsultarUsuarioDTO objUsuarioDTO=new ConsultarUsuarioDTO();
		objUsuarioDTO.setSecuenciaUsuarioSistema(objUsuario.get().getSecuenciaUsuarioSistema());
		if(objUsuario.get().getPersonas()!=null) {
			objUsuarioDTO.setCodigoTipoIdentificacion(objUsuario.get().getPersonas().getTiposIdentificacion().getCodigoTipoIdentificacion());
			objUsuarioDTO.setNumeroIdentificacion(objUsuario.get().getPersonas().getNumeroIdentificacion());
			objUsuarioDTO.setPrimerNombre(objUsuario.get().getPersonas().getPrimerNombre());
			objUsuarioDTO.setSegundoNombre(objUsuario.get().getPersonas().getSegundoNombre());
			objUsuarioDTO.setPrimerApellido(objUsuario.get().getPersonas().getPrimerApellido());
			objUsuarioDTO.setSegundoApellido(objUsuario.get().getPersonas().getSegundoApellido());
			objUsuarioDTO.setFechaNacimiento(GeneralUtil.dateToString(objUsuario.get().getPersonas().getFechaNacimiento(),FormatoFecha.YYYY_MM_DD_GUION));
			objUsuarioDTO.setCodigoGenero(objUsuario.get().getPersonas().getGenero().getCodigoGenero());
		}
		
		if(objUsuario.get().getRolSistema()!=null)
			objUsuarioDTO.setRolSistema(objUsuario.get().getRolSistema().getAbreviatura());
		
		return objUsuarioDTO;
	}

	@Override
	public void guardarPhoto(MultipartFile photo,Integer intIdPersona)throws BOException, IOException{
		
		System.out.print("=>"+photo.getBytes());
		
		//Valida que el campo usuario sea obligatorio
		if (ObjectUtils.isEmpty(intIdPersona)) 
			throw new BOException("ven.warn.campoObligatorio", new Object[] {"ven.campos.idPersona"});
		
		Optional<Personas> objPersona=objIPersonasDAO.findById(intIdPersona);
		
		if(!objPersona.isPresent())
			throw new BOException("ven.warn.idPersonaNoExiste");
		
		if(!("S").equalsIgnoreCase(objPersona.get().getEsActivo()))
			throw new BOException("ven.warn.idPersonaInactivo");
		
		objPersona.get().setPhoto(photo.getBytes());
		objIPersonasDAO.save(objPersona.get());
	}
}
