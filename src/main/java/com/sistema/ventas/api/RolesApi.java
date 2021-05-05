package com.sistema.ventas.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sistema.ventas.bo.IModulosBO;
import com.sistema.ventas.bo.IRolesBO;
import com.sistema.ventas.dto.ResponseOk;
import com.sistema.ventas.exceptions.BOException;
import com.sistema.ventas.exceptions.CustomExceptionHandler;
import com.sistema.ventas.util.MensajesUtil;

@RestController
@RequestMapping("/roles")
public class RolesApi {

	@SuppressWarnings("unused")
	private static final Logger logger = LogManager.getLogger(ModulosApi.class.getName());
	
		@Autowired
		private IRolesBO objIRolesBO;
		
		@RequestMapping(method = RequestMethod.GET)
		public ResponseEntity<?> consultarModulos(
				@RequestHeader(	value = "Accept-Language", 	required = false) String strLanguage
				) throws BOException {
			
			try {
				
				UserDetails objUserDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				
				return new ResponseEntity<>(new ResponseOk(
						MensajesUtil.getMensaje("ven.response.ok", MensajesUtil.validateSupportedLocale(strLanguage)),
						objIRolesBO.consultarRoles(objUserDetails.getUsername())), HttpStatus.OK);
			} catch (BOException be) {
				logger.error(" ERROR => " + be.getTranslatedMessage(strLanguage));
				throw new CustomExceptionHandler(be.getTranslatedMessage(strLanguage), be.getData());
			}
		}
		
		@RequestMapping(value="/ruta", method = RequestMethod.GET)
		public ResponseEntity<?> consultarRolesRuta(
				@RequestHeader(	value = "Accept-Language", 	required = false) String strLanguage,
				@RequestParam(	value = "ruta", 	required = false) String strRuta
				) throws BOException {
			
			try {
				
				return new ResponseEntity<>(new ResponseOk(
						MensajesUtil.getMensaje("ven.response.ok", MensajesUtil.validateSupportedLocale(strLanguage)),
						objIRolesBO.consultarRolesRuta(strRuta)), HttpStatus.OK);
			} catch (BOException be) {
				logger.error(" ERROR => " + be.getTranslatedMessage(strLanguage));
				throw new CustomExceptionHandler(be.getTranslatedMessage(strLanguage), be.getData());
			}
		}
		
		@RequestMapping(value="/ruta/usuario/{secuenciaRol}", method = RequestMethod.GET)
		public ResponseEntity<?> consultarRolesRutaUsuario(
				@RequestHeader(	value = "Accept-Language", 	required = false) String strLanguage,
				@PathVariable(value="secuenciaRol", required = false)  Integer  intSecuenciaRol
				) throws BOException {
			
			try {
				
				UserDetails objUserDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

				return new ResponseEntity<>(new ResponseOk(
						MensajesUtil.getMensaje("ven.response.ok", MensajesUtil.validateSupportedLocale(strLanguage)),
						objIRolesBO.consultarRolesRutaUsuario(objUserDetails.getUsername(),intSecuenciaRol)), HttpStatus.OK);
			} catch (BOException be) {
				logger.error(" ERROR => " + be.getTranslatedMessage(strLanguage));
				throw new CustomExceptionHandler(be.getTranslatedMessage(strLanguage), be.getData());
			}
		}
		
}
