package com.sistema.ventas.api;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sistema.ventas.bo.ITipoIdentificacionBO;
import com.sistema.ventas.dto.ResponseOk;
import com.sistema.ventas.exceptions.BOException;
import com.sistema.ventas.exceptions.CustomExceptionHandler;
import com.sistema.ventas.model.TiposIdentificacion;
import com.sistema.ventas.util.MensajesUtil;

@RestController
@RequestMapping("/tipoIdentificacion")
public class TipoIdentificacionAPI {

	@SuppressWarnings("unused")
	private static final Logger logger = LogManager.getLogger(TipoIdentificacionAPI.class.getName());
	
	@Autowired
	private ITipoIdentificacionBO objITipoIdentificacionBO;
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> listarTipoItentificacion(
			) throws BOException {
		
		try {

			List<TiposIdentificacion> lsTipo = objITipoIdentificacionBO.findAll();

			return new ResponseEntity<>(new ResponseOk(
					MensajesUtil.getMensaje("ven.response.ok", MensajesUtil.validateSupportedLocale(null)),
					lsTipo), HttpStatus.OK);
		} catch (BOException be) {
			logger.error(" ERROR => " + be.getTranslatedMessage(null));
			throw new CustomExceptionHandler(be.getTranslatedMessage(null), be.getData());
		}
	}
	

}
