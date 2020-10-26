package com.sistema.ventas.api;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sistema.ventas.bo.IUsuariosSistemaBO;
import com.sistema.ventas.dto.ConsultarUsuarioDTO;
import com.sistema.ventas.dto.ResponseOk;
import com.sistema.ventas.dto.UsuariosDTO;
import com.sistema.ventas.exceptions.BOException;
import com.sistema.ventas.exceptions.CustomExceptionHandler;
import com.sistema.ventas.util.MensajesUtil;

@RestController
@RequestMapping("/usuariosSistema")
public class UsuariosSistemaApi {
	
	@SuppressWarnings("unused")
	private static final Logger logger = LogManager.getLogger(UsuariosSistemaApi.class.getName());
	
	@Autowired
	private IUsuariosSistemaBO objIUsuariosSistemaBO;
	
	@RequestMapping(value="/crearUsuario",method = RequestMethod.POST)
	public ResponseEntity<?> crearUsuario(
			@RequestHeader(	value = "Accept-Language", 	required = false) String strLanguage, 
			@RequestBody UsuariosDTO objUsuariosDTO
			) throws BOException {
		
		try {

			objIUsuariosSistemaBO.crearUsuario(objUsuariosDTO);

			return new ResponseEntity<>(new ResponseOk(
					MensajesUtil.getMensaje("ven.response.ok", MensajesUtil.validateSupportedLocale(null)),
					null), HttpStatus.OK);
		} catch (BOException be) {
			logger.error(" ERROR => " + be.getTranslatedMessage(null));
			throw new CustomExceptionHandler(be.getTranslatedMessage(null), be.getData());
		}
	}
	
	@RequestMapping(value="/usuarios",method = RequestMethod.GET)
	public ResponseEntity<?> consultarUsuarios(
			@RequestHeader(	value = "Accept-Language", 	required = false) String strLanguage
			) throws BOException {
		
		try {

			List<ConsultarUsuarioDTO> lsConsultarUsuarioDTO=objIUsuariosSistemaBO.consultarUsuarios();

			return new ResponseEntity<>(new ResponseOk(
					MensajesUtil.getMensaje("ven.response.ok", MensajesUtil.validateSupportedLocale(strLanguage)),
					lsConsultarUsuarioDTO), HttpStatus.OK);
		} catch (BOException be) {
			logger.error(" ERROR => " + be.getTranslatedMessage(null));
			throw new CustomExceptionHandler(be.getTranslatedMessage(null), be.getData());
		}
	}
	
	@RequestMapping(value="/{idUsuario}/usuarios",method = RequestMethod.DELETE)
	@Transactional 
	public ResponseEntity<?> eliminarUsuario(
			@RequestHeader(	value = "Accept-Language", 	required = false) String strLanguage,
			@PathVariable(value="idUsuario", required = false)  Integer  intIdUsuario
			) throws BOException {
		
		try {

			objIUsuariosSistemaBO.eliminarUsuario(intIdUsuario);

			return new ResponseEntity<>(new ResponseOk(
					MensajesUtil.getMensaje("ven.response.ok", MensajesUtil.validateSupportedLocale(strLanguage)),
					null), HttpStatus.OK);
		} catch (BOException be) {
			logger.error(" ERROR => " + be.getTranslatedMessage(null));
			throw new CustomExceptionHandler(be.getTranslatedMessage(null), be.getData());
		}
	}
}
