package com.sistema.ventas.bo.impl;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.sistema.ventas.bo.IUsuariosBO;
import com.sistema.ventas.dao.CiudadDAO;
import com.sistema.ventas.dao.GenerosDAO;
import com.sistema.ventas.dao.PaisDAO;
import com.sistema.ventas.dao.PersonasDAO;
import com.sistema.ventas.dao.ProvinciaDAO;
import com.sistema.ventas.dao.RolesDAO;
import com.sistema.ventas.dao.TiposIdentificacionDAO;
import com.sistema.ventas.dao.UsuariosDAO;
import com.sistema.ventas.dto.ConsultarUsuarioDTO;
import com.sistema.ventas.dto.PersonaDTO;
import com.sistema.ventas.dto.UsuariosDTO;
import com.sistema.ventas.email.SendEmail;
import com.sistema.ventas.enums.AlgoritmosIdentificacion;
import com.sistema.ventas.enums.FormatoFecha;
import com.sistema.ventas.enums.TipoIdentificacion;
import com.sistema.ventas.exceptions.BOException;
import com.sistema.ventas.model.Ciudad;
import com.sistema.ventas.model.CiudadCPK;
import com.sistema.ventas.model.Generos;
import com.sistema.ventas.model.Pais;
import com.sistema.ventas.model.Personas;
import com.sistema.ventas.model.Provincia;
import com.sistema.ventas.model.ProvinciaCPK;
import com.sistema.ventas.model.Roles;
import com.sistema.ventas.model.TiposIdentificacion;
import com.sistema.ventas.model.Usuarios;
import com.sistema.ventas.util.FechasUtil;
import com.sistema.ventas.util.FormatoEmailUtil;
import com.sistema.ventas.util.IdentificacionUtil;
import com.sistema.ventas.util.StringUtil;

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
	private PaisDAO objPaisDAO;
	@Autowired
	private ProvinciaDAO objProvinciaDAO;
	@Autowired
	private CiudadDAO objCiudadDAO;
	@Autowired
	private SendEmail objSendEmail;
	
	@Override
	@Transactional
	public Map<String,Object> crearUsuario(UsuariosDTO objUsuariosDTO,String strUsuario) throws BOException {
		
		Usuarios objUsuario=null;
		String strContrasenia=StringUtil.generateRandomString(10);
		
		//***********Prime nombrer*************1
		// Valida que el primer nombre sea obligatorio.
		if (ObjectUtils.isEmpty(objUsuariosDTO.getPrimerNombre())) 
			throw new BOException("ven.warn.campoObligatorio", new Object[] { "ven.campos.primerNombre"});
		
		if(!StringUtil.soloLetrasYEspacio(objUsuariosDTO.getPrimerNombre()))
			throw new BOException("ven.warn.campoSoloLetrasEspacios", new Object[] { "ven.campos.primerNombre"});
		
		//**************************************
		
		//***********Prime apellido*************2
		// Valida que el primer apellido sea obligatorio.
		if (ObjectUtils.isEmpty(objUsuariosDTO.getPrimerApellido())) 
			throw new BOException("ven.warn.campoObligatorio", new Object[] { "ven.campos.primerApellido"});
		
		if(!StringUtil.soloLetrasYEspacio(objUsuariosDTO.getPrimerApellido()))
			throw new BOException("ven.warn.campoSoloLetrasEspacios", new Object[] { "ven.campos.primerApellido"});
		
		//**************************************
		
		//******************Secuencia tipo Identificacion********************3
		// Valida que la secuenica tipo identificacion sea obligatorio
		if (ObjectUtils.isEmpty(objUsuariosDTO.getSecuenciaTipoIdentificacion())) 
			throw new BOException("ven.warn.campoObligatorio", new Object[] { "ven.campos.secuenciaTipoIdentificacion"});

		//Busca el tipo de identificacion
		Optional<TiposIdentificacion> objTiposIdentificacion=objTiposIdentificacionDAO.find(objUsuariosDTO.getSecuenciaTipoIdentificacion());
		
		//Valida que el tipo de identificacion exista
		if(!objTiposIdentificacion.isPresent()) 
			throw new BOException("ven.warn.campoNoExiste", new Object[] { "ven.campos.secuenciaTipoIdentificacion"});
		
		//Valida que el tipo de identificacion este activo
		if(!("S").equalsIgnoreCase(objTiposIdentificacion.get().getEsActivo())) 
			throw new BOException("ven.warn.campoInactivo", new Object[] { "ven.campos.secuenciaTipoIdentificacion"});
		
		//**************************************************************
		
		//***************Numero identificacion************************4
		// Valida que el numero de identificacion sea obligatoria
		if (ObjectUtils.isEmpty(objUsuariosDTO.getNumeroIdentificacion())) 
			throw new BOException("ven.warn.campoObligatorio", new Object[] { "ven.campos.numeroIdentificacion"});

		Boolean booNumeroIdentificacion=false;
		//Valida el tipo de indeificacion segun el formato
		if(TipoIdentificacion.CEDULA.getValor().equals(objUsuariosDTO.getSecuenciaTipoIdentificacion())) 
			booNumeroIdentificacion=IdentificacionUtil.validaAlgoritmoIdentificacion(objUsuariosDTO.getNumeroIdentificacion(), AlgoritmosIdentificacion.CEDULA_IDENTIDAD_EC.getName());
		else if(TipoIdentificacion.RUC.getValor().equals(objUsuariosDTO.getSecuenciaTipoIdentificacion())) 
			booNumeroIdentificacion=IdentificacionUtil.validaAlgoritmoIdentificacion(objUsuariosDTO.getNumeroIdentificacion(), AlgoritmosIdentificacion.REGISTRO_UNICO_CONTRIBUYENTE_EC.getName());
		
		//Valida el tipo de indeificacion segun el formato
		if(!booNumeroIdentificacion) 
			throw new BOException("ven.warn.numeroIdentificacionInvalida");
		
		//Busca el usuario por cedula
		objUsuario=objUsuariosDAO.consultarUsuarioSistemaPorCedula(objUsuariosDTO.getNumeroIdentificacion());
		
		//Valida que el usuario no exista
		if(!ObjectUtils.isEmpty(objUsuario)) 
			throw new BOException("ven.warn.numeroIdentificacionExiste");
		
		//*******************************************************************
		
		//***************Secuencia Genero************************5
		// Valida que la secuencia genero sea obligatorio
		if (ObjectUtils.isEmpty(objUsuariosDTO.getSecuenciaGenero())) 
			throw new BOException("ven.warn.campoObligatorio", new Object[] { "ven.campos.secuenciaGenero"});
		
		//Busca el genero
		Optional<Generos> objGenero=objGenerosDAO.find(objUsuariosDTO.getSecuenciaGenero());
		
		//Valida que exista el genero
		if(!objGenero.isPresent()) 
			throw new BOException("ven.warn.campoNoExiste", new Object[] { "ven.campos.secuenciaGenero"});
		
		//Valida que este activo el genero
		if(!("S").equalsIgnoreCase(objGenero.get().getEsActivo())) 
			throw new BOException("ven.warn.campoInactivo", new Object[] { "ven.campos.secuenciaGenero"});
		
		//***********************************************************
		
		//***************Fecha de nacimiento************************6
		// Valida que la fecha de nacimiento sea requerida
		if (ObjectUtils.isEmpty(objUsuariosDTO.getFechaNacimiento())) 
			throw new BOException("ven.warn.campoObligatorio", new Object[] { "ven.campos.fechaNacimiento"});
		//***********************************************************
		
		//********************Usuario*******************************8
		// Usuario.
		if (ObjectUtils.isEmpty(objUsuariosDTO.getUsuario())) 
			throw new BOException("ven.warn.campoObligatorio", new Object[] { "ven.campos.usuario"});
		
		objUsuario=objUsuariosDAO.consultarUsuarioSistema(objUsuariosDTO.getUsuario());
		
		if(objUsuario!=null)
			throw new BOException("ven.warn.usuarioExiste", new Object[] {objUsuariosDTO.getUsuario()});
		
		//**************************************************************
		
		//************Secuencia Pais*********************************** 9
		// Valida que la secuenica pais sea obligatorio
		if (ObjectUtils.isEmpty(objUsuariosDTO.getSecuenciaPais())) 
			throw new BOException("ven.warn.campoObligatorio", new Object[] { "ven.campos.secuenciaPais"});
		
		//Busca el el pais
		Optional<Pais> objPais=objPaisDAO.find(objUsuariosDTO.getSecuenciaPais());
		
		//Valida que el tipo de identificacion exista
		if(!objPais.isPresent()) 
			throw new BOException("ven.warn.campoNoExiste", new Object[] { "ven.campos.secuenciaPais"});
		
		//Valida que el tipo de identificacion este activo
		if(!("S").equalsIgnoreCase(objPais.get().getEsActivo())) 
			throw new BOException("ven.warn.campoInactivo", new Object[] { "ven.campos.secuenciaPais"});
		
		//*************************************************************
		
		//************Secuencia Provincia***********************************10
		// Valida que la secuencia provincia sea obligatorio
		if (ObjectUtils.isEmpty(objUsuariosDTO.getSecuenciaProvincia())) 
			throw new BOException("ven.warn.campoObligatorio", new Object[] { "ven.campos.secuenciaProvincia"});
		
		//Busca el el pais
		Optional<Provincia> objProvincia=objProvinciaDAO.find(new ProvinciaCPK(objUsuariosDTO.getSecuenciaPais(),objUsuariosDTO.getSecuenciaProvincia()));
		
		//Valida que el tipo de identificacion exista
		if(!objProvincia.isPresent()) 
			throw new BOException("ven.warn.campoNoExiste",new Object[]{"ven.campos.secuenciaProvincia"});
		
		//Valida que el tipo de identificacion este activo
		if(!("S").equalsIgnoreCase(objProvincia.get().getEsActivo())) 
			throw new BOException("ven.warn.campoInactivo",new Object[]{"ven.campos.secuenciaProvincia"});
		
		//*************************************************************
		
		//************Secuencia Ciudad***********************************11
		// Valida que la secuencia ciudad sea obligatorio
		if (ObjectUtils.isEmpty(objUsuariosDTO.getSecuenciaCiudad())) 
			throw new BOException("ven.warn.campoObligatorio", new Object[] { "ven.campos.secuenciaCiudad"});
		
		//Busca el el pais
		Optional<Ciudad> objCiudad=objCiudadDAO.find(new CiudadCPK(objUsuariosDTO.getSecuenciaPais(),objUsuariosDTO.getSecuenciaProvincia(),objUsuariosDTO.getSecuenciaCiudad()));
		
		//Valida que el tipo de identificacion exista
		if(!objCiudad.isPresent()) 
			throw new BOException("ven.warn.campoNoExiste",new Object[]{"ven.campos.secuenciaCiudad"});
		
		//Valida que el tipo de identificacion este activo
		if(!("S").equalsIgnoreCase(objCiudad.get().getEsActivo())) 
			throw new BOException("ven.warn.campoInactivo",new Object[]{"ven.campos.secuenciaCiudad"});
				
		//*************************************************************
			
//		//************Secuencia rol***********************************12
//		// Valida que la secuencia ciudad sea obligatorio
//		if (ObjectUtils.isEmpty(objUsuariosDTO.getSecuenciaRol())) 
//			throw new BOException("ven.warn.campoObligatorio", new Object[] { "ven.campos.secuenciaRol"});
//		
//		//Busca el el pais
//		Optional<Roles> objRoles=objRolesDAO.find(objUsuariosDTO.getSecuenciaRol());
//		
//		//Valida que el tipo de identificacion exista
//		if(!objRoles.isPresent()) 
//			throw new BOException("ven.warn.campoNoExiste",new Object[]{"ven.campos.secuenciaRol"});
//		
//		//Valida que el tipo de identificacion este activo
//		if(!("S").equalsIgnoreCase(objRoles.get().getEsActivo())) 
//			throw new BOException("ven.warn.campoInactivo",new Object[]{"ven.campos.secuenciaRol"});
				
		//*************************************************************
		
		//**************************Email***********************************
		// Valida que la email sea obligatorio
		if (ObjectUtils.isEmpty(objUsuariosDTO.getEmail())) 
			throw new BOException("ven.warn.campoObligatorio", new Object[] { "ven.campos.email"});		

		if(!FormatoEmailUtil.emailValido(objUsuariosDTO.getEmail())) 
			throw new BOException("ven.warn.correoInvalido");
		
		Personas existeCorreo=objPersonasDAO.consultarExisteCorreo(objUsuariosDTO.getEmail());
		
		//Valida si existe el correo
		if(!ObjectUtils.isEmpty(existeCorreo)) 
			throw new BOException("ven.warn.correoExiste");
		
		//*************************************************************
		
		Personas objPersona=new Personas();
		
		objPersona.setPrimerNombre(StringUtil.eliminarAcentos(objUsuariosDTO.getPrimerNombre().toUpperCase()));
		
		if(!ObjectUtils.isEmpty(objUsuariosDTO.getSegundoNombre())) {
			
			if(!StringUtil.soloLetrasYEspacio(objUsuariosDTO.getSegundoNombre()))
				throw new BOException("ven.warn.campoSoloLetrasEspacios", new Object[] { "ven.campos.segundoNombre"});
			
			objPersona.setSegundoNombre(StringUtil.eliminarAcentos(objUsuariosDTO.getSegundoNombre().toUpperCase()));
		}
		
		objPersona.setPrimerApellido(StringUtil.eliminarAcentos(objUsuariosDTO.getPrimerApellido().toUpperCase()));
		
		if(!ObjectUtils.isEmpty(objUsuariosDTO.getSegundoApellido())) {
			
			if(!StringUtil.soloLetrasYEspacio(objUsuariosDTO.getSegundoApellido()))
				throw new BOException("ven.warn.campoSoloLetrasEspacios", new Object[] { "ven.campos.segundoApellido"});
			
			objPersona.setSegundoApellido(StringUtil.eliminarAcentos(objUsuariosDTO.getSegundoApellido().toUpperCase()));
		}
		
		Date datFechaActual=new Date();
		
		objPersona.setFechaNacimiento(FechasUtil.stringToDate(objUsuariosDTO.getFechaNacimiento(),FormatoFecha.YYYY_MM_DD_GUION));
		objPersona.setTiposIdentificacion(objTiposIdentificacion.get());
		objPersona.setNumeroIdentificacion(StringUtil.eliminarAcentos(objUsuariosDTO.getNumeroIdentificacion()));
		objPersona.setGenero(objGenero.get());
		objPersona.setCiudad(objCiudad.get());
		objPersona.setEmail(objUsuariosDTO.getEmail());
		objPersona.setEsActivo("S");
		objPersona.setUsuarioIngreso(strUsuario);
		objPersona.setFechaIngreso(datFechaActual);
		objPersonasDAO.persist(objPersona);
		
		Usuarios objUsuarios=new Usuarios();
		objUsuarios.setUsuario(StringUtil.eliminarAcentos(objUsuariosDTO.getUsuario().toUpperCase()));
		objUsuarios.setPersonas(objPersona);
		objUsuarios.setEsActivo("S");
		objUsuarios.setUsuarioIngreso(strUsuario);
		objUsuarios.setFechaIngreso(datFechaActual);
		//objUsuarios.setRoles(objRoles.get());
		objUsuarios.setContrasenia(StringUtil.base64Encode(strContrasenia));
		objUsuariosDAO.persist(objUsuarios);
		
		String strContenido="Usuario:"+objUsuarios.getUsuario()+" - Contraseña: "+strContrasenia;
		objSendEmail.envioEmail(objUsuariosDTO.getEmail(),"Contraseña Ventas",strContenido);
		
		Map<String,Object> objMap=new HashMap<String,Object>();
		objMap.put("secuenciaPersona",objPersona.getSecuenciaPersona());
		return objMap;
	}
	
	@Override
	public void actualizarUsuario(Integer intIdUsuario,UsuariosDTO objUsuariosDTO,String strUsuario) throws BOException {
		Usuarios objUsuario=null;
		Optional<TiposIdentificacion> objTiposIdentificacion=null;
		
		// codigoIdentificacion
		if (!ObjectUtils.isEmpty(objUsuariosDTO.getSecuenciaTipoIdentificacion())) { 
		
			objTiposIdentificacion=objTiposIdentificacionDAO.find(objUsuariosDTO.getSecuenciaTipoIdentificacion());
			
			if(!objTiposIdentificacion.isPresent()) 
				throw new BOException("ven.warn.tipoIdentificacionNoExiste");
			
			if(!("S").equalsIgnoreCase(objTiposIdentificacion.get().getEsActivo())) 
				throw new BOException("ven.warn.tipoIdentificacionInactivo");
		}
		
		// codigoIdentificacion
		if (!ObjectUtils.isEmpty(objUsuariosDTO.getNumeroIdentificacion())) {
			Boolean booNumeroIdentificacion=false;
			
			if(TipoIdentificacion.CEDULA.getValor().equals(objUsuariosDTO.getSecuenciaTipoIdentificacion())) 
				booNumeroIdentificacion=IdentificacionUtil.validaAlgoritmoIdentificacion(objUsuariosDTO.getNumeroIdentificacion(), AlgoritmosIdentificacion.CEDULA_IDENTIDAD_EC.getName());
			else if(TipoIdentificacion.RUC.getValor().equals(objUsuariosDTO.getSecuenciaTipoIdentificacion())) 
				booNumeroIdentificacion=IdentificacionUtil.validaAlgoritmoIdentificacion(objUsuariosDTO.getNumeroIdentificacion(), AlgoritmosIdentificacion.REGISTRO_UNICO_CONTRIBUYENTE_EC.getName());
			
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
		if (!ObjectUtils.isEmpty(objUsuariosDTO.getSecuenciaGenero())) {
			
			objGenero=objGenerosDAO.find(objUsuariosDTO.getSecuenciaGenero());
			
			if(!objGenero.isPresent()) 
				throw new BOException("ven.warn.generoNoExiste");
			
			if(!("S").equalsIgnoreCase(objGenero.get().getEsActivo())) 
				throw new BOException("ven.warn.generoInactivo");
		}
		
		// Usuario.
		if (!ObjectUtils.isEmpty(objUsuariosDTO.getUsuario())) {
		
			objUsuario=objUsuariosDAO.consultarUsuarioSistema(objUsuariosDTO.getUsuario());
			
			if(objUsuario!=null)
				throw new BOException("ven.warn.usuarioExiste", new Object[] {objUsuariosDTO.getUsuario()});
		}
		
		Optional<Usuarios> objUsuariosSistema=objUsuariosDAO.find(intIdUsuario);
		
		if(!objUsuariosSistema.isPresent())
			throw new BOException("ven.warn.usuarioNoExiste");
		
		if(!ObjectUtils.isEmpty(objUsuariosDTO.getUsuario()))
			objUsuariosSistema.get().setUsuario(objUsuariosDTO.getUsuario().toUpperCase());
		
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
			objPersona.get().setFechaNacimiento(FechasUtil.stringToDate(objUsuariosDTO.getFechaNacimiento(),FormatoFecha.YYYY_MM_DD_GUION));
		
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


	@Override
	@Transactional
	public Map<String, Object> activarOInactivarUsuario(Integer intIdUsuario,String strUsuario) throws BOException {
		
		Date datFechaActual=new Date();
		
		//Valida que el campo usuario sea obligatorio
		if (ObjectUtils.isEmpty(intIdUsuario)) 
			throw new BOException("ven.warn.campoObligatorio", new Object[] {"ven.campos.idUsuario"});
		
		Optional<Usuarios> objUsuario=objUsuariosDAO.find(intIdUsuario);
		
		if(!objUsuario.isPresent())
			throw new BOException("ven.warn.idUsuarioNoExiste");
		
		if(("N").equalsIgnoreCase(objUsuario.get().getEsActivo()))
			objUsuario.get().setEsActivo("S");
		else
			objUsuario.get().setEsActivo("N");
		
		//Elimina el usuario
		objUsuario.get().setUsuarioActualizacion(strUsuario);
		objUsuario.get().setFechaActualizacion(datFechaActual);
			
		Map<String, Object> mapResult = new HashMap();
		mapResult.put("secuenciaUsuario",intIdUsuario);
		
		return mapResult;
		
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
		
//		ConsultarUsuarioDTO objUsuarioDTO=new ConsultarUsuarioDTO();
//		objUsuarioDTO.setSecuenciaUsuarioSistema(objUsuario.get().getSecuenciaUsuario());
//		if(objUsuario.get().getPersonas()!=null) {
//			objUsuarioDTO.setCodigoTipoIdentificacion(objUsuario.get().getPersonas().getTiposIdentificacion().getSecuenciaTipoIdentificacion());
//			objUsuarioDTO.setNumeroIdentificacion(objUsuario.get().getPersonas().getNumeroIdentificacion());
//			objUsuarioDTO.setPrimerNombre(objUsuario.get().getPersonas().getPrimerNombre());
//			objUsuarioDTO.setSegundoNombre(objUsuario.get().getPersonas().getSegundoNombre());
//			objUsuarioDTO.setPrimerApellido(objUsuario.get().getPersonas().getPrimerApellido());
//			objUsuarioDTO.setSegundoApellido(objUsuario.get().getPersonas().getSegundoApellido());
//			objUsuarioDTO.setFechaNacimiento(GeneralUtil.dateToString(objUsuario.get().getPersonas().getFechaNacimiento(),FormatoFecha.YYYY_MM_DD_GUION));
//			objUsuarioDTO.setCodigoGenero(objUsuario.get().getPersonas().getGenero().getSecuenciaGenero());
//		}
		
//		if(objUsuario.get().getRoles()!=null)
//			objUsuarioDTO.setRolSistema(objUsuario.get().getRoles().getAbreviatura());
//		
		return null;
	}

	@Override
	public void guardarPhoto(MultipartFile photo,Integer intIdPersona,String strUsuario)throws BOException, IOException{
		
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

	@Override
	public Map<String, Object> consultarUsuarios(Integer intPage, Integer intPerPage,String strCedulaCodigoUsuario, String strEstado, String strUser)
			throws BOException {
		
		//intPage.
		if (ObjectUtils.isEmpty(intPage)) 
			throw new BOException("ven.warn.campoObligatorio", new Object[] { "ven.campos.page"});
		
		//intPerPage.
		if (ObjectUtils.isEmpty(intPerPage)) 
			throw new BOException("ven.warn.campoObligatorio", new Object[] { "ven.campos.perPage"});
		
	
		List<ConsultarUsuarioDTO> lsUsuario=objUsuariosDAO.consultarUsuarioSistema(intPage,intPerPage,strCedulaCodigoUsuario,strEstado,strUser);
		Long lngUsuario=objUsuariosDAO.contarConsultarUsuarioSistema(strCedulaCodigoUsuario,strEstado,strUser);
		
		Map<String, Object> mapResult = new HashMap();
		mapResult.put("rows",lsUsuario);
		mapResult.put("totalRows",lngUsuario);
		
		return mapResult;
	}

	@Override
	public Map<String, Object> consultarUsuarioDisponible(String strPrimerNombre, String strSegundoNombre,
			String strPrimerApellido, String strSegundoApellido) throws BOException {

		PersonaDTO objPersonaDTO=new PersonaDTO();
		// Valida que el primer nombre sea obligatorio.
		if (ObjectUtils.isEmpty(strPrimerNombre)) 
			throw new BOException("ven.warn.campoObligatorio", new Object[] { "ven.campos.primerNombre"});
		
		if(!StringUtil.soloLetrasYEspacio(strPrimerNombre))
			throw new BOException("ven.warn.campoSoloLetrasEspacios", new Object[] { "ven.campos.primerNombre"});
		
		objPersonaDTO.setPrimerNombre(strPrimerNombre);

		// Valida que el primer apellido sea obligatorio.
		if (ObjectUtils.isEmpty(strPrimerApellido)) 
			throw new BOException("ven.warn.campoObligatorio", new Object[] { "ven.campos.primerApellido"});
		
		if(!StringUtil.soloLetrasYEspacio(strPrimerApellido))
			throw new BOException("ven.warn.campoSoloLetrasEspacios", new Object[] { "ven.campos.primerApellido"});
		
		objPersonaDTO.setPrimerApellido(strPrimerApellido);
		
		if(!ObjectUtils.isEmpty(strSegundoNombre)) {
			
			if(!StringUtil.soloLetrasYEspacio(strSegundoNombre))
				throw new BOException("ven.warn.campoSoloLetrasEspacios", new Object[] { "ven.campos.segundoNombre"});
			
			objPersonaDTO.setSegundoNombre(strSegundoNombre);
			
		}
		
		if(!ObjectUtils.isEmpty(strSegundoApellido)) {
			
			if(!StringUtil.soloLetrasYEspacio(strSegundoApellido))
				throw new BOException("ven.warn.campoSoloLetrasEspacios", new Object[] { "ven.campos.segundoApellido"});
			
			objPersonaDTO.setSegundoApellido(strSegundoApellido);
		}
		
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("usuarioDisponible",validarCodUsuario(objPersonaDTO,"ALG_USUARIOS_1",10));
		
		return map;
	}
	
	public String validarCodUsuario(PersonaDTO objPersona,String strAlgoritmoCodigosEmpleados, Integer maxCantCaracteres) throws BOException{
		String strCodUsuario = "";
		
		if(strAlgoritmoCodigosEmpleados.toUpperCase().trim().equals("ALG_USUARIOS_1")){
			strCodUsuario=algoritmoUsuarios1(objPersona,maxCantCaracteres);
		}else {
			throw new BOException("seg.warn.algoritmo");
		}
		
		return strCodUsuario;
	}
	
	public String algoritmoUsuarios1(PersonaDTO objPersonal, Integer maxCantCaracteres) {
		try{
			//1)1ra letra primer nombre + 1er apellido.
			String strCodUsuario = "";
			String strCodUsuarioAux = "";
			
			if(!StringUtils.isBlank(objPersonal.getPrimerNombre()) && !StringUtils.isBlank(objPersonal.getPrimerApellido())){
		    	strCodUsuario = strCodUsuario+ objPersonal.getPrimerNombre().trim().substring(0, 1);
				strCodUsuario = StringUtil.eliminarAcentos(strCodUsuario + objPersonal.getPrimerApellido().trim());
				strCodUsuario = usuarioExtenso(strCodUsuario,maxCantCaracteres);
			
				//2) 1ra letra segundo nombre + 1er apellido.
				
				if(StringUtils.isBlank(strCodUsuario) || (!StringUtils.isBlank(strCodUsuario) 
						&& !objUsuariosDAO.validarCodigoRepetido(strCodUsuario.toUpperCase().trim()))){
					strCodUsuarioAux = "";
					strCodUsuario = "";
					if(!StringUtils.isBlank(objPersonal.getSegundoNombre())&& !StringUtils.isBlank(objPersonal.getPrimerApellido())){
						strCodUsuarioAux = strCodUsuarioAux + objPersonal.getSegundoNombre().trim().substring(0,1);
						strCodUsuarioAux = StringUtil.eliminarAcentos(strCodUsuarioAux + objPersonal.getPrimerApellido().trim());
						strCodUsuario = usuarioExtenso(strCodUsuarioAux,maxCantCaracteres);
					}
					
					if(StringUtils.isBlank(strCodUsuario) || (!StringUtils.isBlank(strCodUsuario) 
							&& !objUsuariosDAO.validarCodigoRepetido(strCodUsuario.toUpperCase().trim()))){
						strCodUsuarioAux = "";
						strCodUsuario = "";
						//3) 1ra letra primer nombre + 2do apellido.
						if(!StringUtils.isBlank(objPersonal.getPrimerNombre()) && !StringUtils.isBlank(objPersonal.getSegundoApellido())){
							strCodUsuarioAux = strCodUsuarioAux + objPersonal.getPrimerNombre().trim().substring(0,1);
							strCodUsuarioAux = StringUtil.eliminarAcentos(strCodUsuarioAux + objPersonal.getSegundoApellido().trim());
							strCodUsuario = usuarioExtenso(strCodUsuarioAux,maxCantCaracteres);
						} 
						
						//4) 1ra letra segundo nombre + 2do apellido.
						
						if(StringUtils.isBlank(strCodUsuario) || (!StringUtils.isBlank(strCodUsuario) 
								&& !objUsuariosDAO.validarCodigoRepetido(strCodUsuario.toUpperCase().trim()))){
							strCodUsuarioAux = "";
							strCodUsuario = "";
							if(!StringUtils.isBlank(objPersonal.getSegundoNombre()) && !StringUtils.isBlank(objPersonal.getSegundoApellido())){
								strCodUsuarioAux =strCodUsuarioAux + objPersonal.getSegundoNombre().trim().substring(0,1);
								strCodUsuarioAux = StringUtil.eliminarAcentos(strCodUsuarioAux + objPersonal.getSegundoApellido().trim());
								strCodUsuario = usuarioExtenso(strCodUsuarioAux,maxCantCaracteres);
							}
							
							//5)1ra letra primer nombre + 1ra letra segundo nombre + 1er apellido.
							if(StringUtils.isBlank(strCodUsuario) || (!StringUtils.isBlank(strCodUsuario) 
									&& !objUsuariosDAO.validarCodigoRepetido(strCodUsuario.toUpperCase().trim()))){
								strCodUsuarioAux = "";
								strCodUsuario = "";	
								if(!StringUtils.isBlank(objPersonal.getPrimerNombre())&&!StringUtils.isBlank(objPersonal.getSegundoNombre())&&!StringUtils.isBlank(objPersonal.getPrimerApellido())){
									strCodUsuarioAux = strCodUsuarioAux + objPersonal.getPrimerNombre().trim().substring(0, 1);
									strCodUsuarioAux = strCodUsuarioAux + objPersonal.getSegundoNombre().trim().substring(0, 1);
									strCodUsuarioAux = StringUtil.eliminarAcentos(strCodUsuarioAux + objPersonal.getPrimerApellido().trim());
									strCodUsuario = usuarioExtenso(strCodUsuarioAux,maxCantCaracteres);
								} 
								
								//6)1ra letra primer nombre + 1ra letra segundo nombre + 2do apellido.
								if(StringUtils.isBlank(strCodUsuario) || (!StringUtils.isBlank(strCodUsuario) 
										&& !objUsuariosDAO.validarCodigoRepetido(strCodUsuario.toUpperCase().trim()))){
									strCodUsuarioAux = "";
									strCodUsuario = "";	
									if(!StringUtils.isBlank(objPersonal.getPrimerNombre()) && !StringUtils.isBlank(objPersonal.getSegundoNombre())&& !StringUtils.isBlank(objPersonal.getSegundoApellido())){
										strCodUsuarioAux = strCodUsuarioAux + objPersonal.getPrimerNombre().trim().substring(0, 1);
										strCodUsuarioAux = strCodUsuarioAux + objPersonal.getSegundoNombre().trim().substring(0, 1);
										strCodUsuarioAux = StringUtil.eliminarAcentos(strCodUsuarioAux + objPersonal.getSegundoApellido().trim());
										strCodUsuario = usuarioExtenso(strCodUsuarioAux,maxCantCaracteres);
									}
									
									//7. 1raLetra primerApellido + 1erApellido + 1raLetra d segundodoApellido
									if(StringUtils.isBlank(strCodUsuario) || (!StringUtils.isBlank(strCodUsuario) 
											&& !objUsuariosDAO.validarCodigoRepetido(strCodUsuario.toUpperCase().trim()))){
										strCodUsuarioAux = "";
										strCodUsuario = "";	
										if(!StringUtils.isBlank(objPersonal.getPrimerApellido()) && !StringUtils.isBlank(objPersonal.getSegundoApellido())){
											strCodUsuarioAux = strCodUsuarioAux + objPersonal.getPrimerApellido().trim().substring(0, 1);
											strCodUsuarioAux = strCodUsuarioAux + objPersonal.getPrimerApellido().trim();
											strCodUsuarioAux = StringUtil.eliminarAcentos(strCodUsuarioAux + objPersonal.getSegundoApellido().trim().substring(0, 1));
											strCodUsuario = usuarioExtenso(strCodUsuarioAux,maxCantCaracteres);
										}
										
										//8. 1ra y 2da Letra del 1erNombre + 1erApellido
										if(StringUtils.isBlank(strCodUsuario) || (!StringUtils.isBlank(strCodUsuario) 
												&& !objUsuariosDAO.validarCodigoRepetido(strCodUsuario.toUpperCase().trim()))){
											strCodUsuarioAux = "";
											strCodUsuario = "";	
											if(!StringUtils.isBlank(objPersonal.getPrimerNombre()) && !StringUtils.isBlank(objPersonal.getPrimerApellido())){
												strCodUsuarioAux = strCodUsuarioAux + objPersonal.getPrimerNombre().trim().substring(0, 2);
												strCodUsuarioAux = StringUtil.eliminarAcentos(strCodUsuarioAux + objPersonal.getPrimerApellido().trim());
												strCodUsuario = usuarioExtenso(strCodUsuarioAux,maxCantCaracteres);
											}
											
											//9. En caso no cumpla ningun caso
											if(!StringUtils.isBlank(strCodUsuario) 
													&& !objUsuariosDAO.validarCodigoRepetido(strCodUsuario.toUpperCase().trim()))
												strCodUsuario = "";	
										}
									}
								}
							}
						}
					}	
				}
			}
			
			return strCodUsuario.toUpperCase().trim();
		
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	private String usuarioExtenso(String strUsuario,Integer maxCantCaracteres){
		try{
			String strUser="";
			if(strUsuario.length()<= maxCantCaracteres){
				strUser = strUsuario.replaceAll(" ", "");				
			}else{
				strUser = strUsuario.substring(0,maxCantCaracteres).replaceAll(" ", "");				
			}
			return strUser;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
}
