package com.sistema.ventas.api;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.core.HttpHeaders;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sistema.ventas.bo.IAutenticacionBO;
import com.sistema.ventas.dto.AutenticacionDTO;
import com.sistema.ventas.dto.ResponseOk;
import com.sistema.ventas.exceptions.BOException;
import com.sistema.ventas.exceptions.CustomExceptionHandler;
import com.sistema.ventas.util.MensajesUtil;

@RestController
@RequestMapping("/autenticacion")
public class AutenticacionApi {

	@SuppressWarnings("unused")
	private static final Logger logger = LogManager.getLogger(AutenticacionApi.class.getName());

	@Autowired
	private IAutenticacionBO objIAutenticacionBO;
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResponseEntity<?> login(
			@HeaderParam(HttpHeaders.AUTHORIZATION) String strAuthorization, 
			@RequestHeader(	value = "Accept-Language", 	required = false) String strLanguage, 
			@RequestHeader(	value = "Application", 	required = false) String strApplication,
			@RequestHeader("Authorization") String strToken) throws BOException {
		
		try {

			AutenticacionDTO objLogin = objIAutenticacionBO.login(strToken,strApplication);

			return new ResponseEntity<>(new ResponseOk(
					MensajesUtil.getMensaje("ven.response.ok", MensajesUtil.validateSupportedLocale(strLanguage)),
					objLogin), HttpStatus.OK);
		} catch (BOException be) {
			throw new CustomExceptionHandler(be.getTranslatedMessage(strLanguage), be.getData());
		}
		
	}
}
