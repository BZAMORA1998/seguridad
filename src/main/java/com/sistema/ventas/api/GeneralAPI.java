package com.sistema.ventas.api;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sistema.ventas.bo.IGeneralBO;
import com.sistema.ventas.dto.ResponseOk;
import com.sistema.ventas.exceptions.BOException;
import com.sistema.ventas.exceptions.CustomExceptionHandler;
import com.sistema.ventas.model.Generos;
import com.sistema.ventas.model.TiposIdentificacion;
import com.sistema.ventas.util.MensajesUtil;

@RestController
@RequestMapping("/general")
public class GeneralAPI {

	@SuppressWarnings("unused")
	private static final Logger logger = LogManager.getLogger(GeneralAPI.class.getName());
	
	@Autowired
	private IGeneralBO objIGeneralBO;
	
	@RequestMapping(value="/tipoIdentificacion",method = RequestMethod.GET)
	public ResponseEntity<?> listarTipoItentificacion(
			@RequestHeader(	value = "Accept-Language", 	required = false) String strLanguage
			) throws BOException {
		
		try {

			List<TiposIdentificacion> lsTipo = objIGeneralBO.findAllTiposIdentificacion();

			return new ResponseEntity<>(new ResponseOk(
					MensajesUtil.getMensaje("ven.response.ok", MensajesUtil.validateSupportedLocale(strLanguage)),
					lsTipo), HttpStatus.OK);
		} catch (BOException be) {
			logger.error(" ERROR => " + be.getTranslatedMessage(strLanguage));
			throw new CustomExceptionHandler(be.getTranslatedMessage(strLanguage), be.getData());
		}
	}
	
	@RequestMapping(value="/genero",method = RequestMethod.GET)
	public ResponseEntity<?> listarGenero(
			@RequestHeader(	value = "Accept-Language", 	required = false) String strLanguage
			) throws BOException {
		
		try {

			List<Generos> lsGenero = objIGeneralBO.findAllGeneros();

			return new ResponseEntity<>(new ResponseOk(
					MensajesUtil.getMensaje("ven.response.ok", MensajesUtil.validateSupportedLocale(strLanguage)),
					lsGenero), HttpStatus.OK);
		} catch (BOException be) {
			logger.error(" ERROR => " + be.getTranslatedMessage(strLanguage));
			throw new CustomExceptionHandler(be.getTranslatedMessage(strLanguage), be.getData());
		}
	}
}
