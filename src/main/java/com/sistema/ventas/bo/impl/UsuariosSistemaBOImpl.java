package com.sistema.ventas.bo.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sistema.ventas.bo.IUsuariosSistemaBO;
import com.sistema.ventas.dao.IGeneroDAO;
import com.sistema.ventas.dao.IPersonasDAO;
import com.sistema.ventas.dao.IRolSistemaDAO;
import com.sistema.ventas.dao.ITiposIdentificacionDAO;
import com.sistema.ventas.dao.IUsuarioSistemaDAO;
import com.sistema.ventas.dao.PersonasDAO;
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
	public void crearUsuario(UsuariosDTO objUsuariosDTO) throws BOException {
		
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
		objPersona.setFechaNacimiento(GeneralUtil.stringToDate(objUsuariosDTO.getFechaNacimiento(),FormatoFecha.DD_MM_YYYY));
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
		
	}

	@Override
	public List<ConsultarUsuarioDTO> consultarUsuarios() {
		List<ConsultarUsuarioDTO> lsConsultarUsuarioDTO=new ArrayList<ConsultarUsuarioDTO>();
		List<UsuariosSistema> lsUsuario=objUsuarioSistemaDAO.consultarUsuarioSistema();
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
		
		return lsConsultarUsuarioDTO;
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
			objUsuarioDTO.setFechaNacimiento(GeneralUtil.dateToString(objUsuario.get().getPersonas().getFechaNacimiento(),FormatoFecha.DD_MM_YYYY));
			objUsuarioDTO.setCodigoGenero(objUsuario.get().getPersonas().getGenero().getCodigoGenero());
		}
		
		if(objUsuario.get().getRolSistema()!=null)
			objUsuarioDTO.setRolSistema(objUsuario.get().getRolSistema().getAbreviatura());
		
		return objUsuarioDTO;
	}
}
