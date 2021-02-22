package com.sistema.ventas.api;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
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
	
	@RequestMapping(value="/pais",method = RequestMethod.GET)
	public ResponseEntity<?> listarPais(
			@RequestHeader(	value = "Accept-Language", 	required = false) String strLanguage
			) throws BOException {
		
		try {

			return new ResponseEntity<>(new ResponseOk(
					MensajesUtil.getMensaje("ven.response.ok", MensajesUtil.validateSupportedLocale(strLanguage)),
					objIGeneralBO.findAllPais()), HttpStatus.OK);
		} catch (BOException be) {
			logger.error(" ERROR => " + be.getTranslatedMessage(strLanguage));
			throw new CustomExceptionHandler(be.getTranslatedMessage(strLanguage), be.getData());
		}
	}
	
	@RequestMapping(value="/provincia/{secuenciaPais}",method = RequestMethod.GET)
	public ResponseEntity<?> listarProvincia(
			@RequestHeader(	value = "Accept-Language", 	required = false) String strLanguage,
			@PathVariable(value="secuenciaPais", required = false)  Integer  intSecuenciaPais
			) throws BOException {
		
		try {

			return new ResponseEntity<>(new ResponseOk(
					MensajesUtil.getMensaje("ven.response.ok", MensajesUtil.validateSupportedLocale(strLanguage)),
					objIGeneralBO.findAllProvincia(intSecuenciaPais)), HttpStatus.OK);
		} catch (BOException be) {
			logger.error(" ERROR => " + be.getTranslatedMessage(strLanguage));
			throw new CustomExceptionHandler(be.getTranslatedMessage(strLanguage), be.getData());
		}
	}
	
	@RequestMapping(value="/ciudad/{secuenciaPais}/{secuenciaProvincia}",method = RequestMethod.GET)
	public ResponseEntity<?> listarCiudad(
			@RequestHeader(	value = "Accept-Language", 	required = false) String strLanguage,
			@PathVariable(value="secuenciaPais", required = false)  Integer  intSecuenciaPais,
			@PathVariable(value="secuenciaProvincia", required = false)  Integer  intSecuenciaProvincia
			) throws BOException {
		
		try {

			return new ResponseEntity<>(new ResponseOk(
					MensajesUtil.getMensaje("ven.response.ok", MensajesUtil.validateSupportedLocale(strLanguage)),
					objIGeneralBO.findAllCiudad(intSecuenciaPais,intSecuenciaProvincia)), HttpStatus.OK);
		} catch (BOException be) {
			logger.error(" ERROR => " + be.getTranslatedMessage(strLanguage));
			throw new CustomExceptionHandler(be.getTranslatedMessage(strLanguage), be.getData());
		}
	}
}
