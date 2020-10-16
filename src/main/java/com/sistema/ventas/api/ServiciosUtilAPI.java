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

import com.sistema.ventas.bo.IGeneroBO;
import com.sistema.ventas.bo.IServiciosUtilBO;
import com.sistema.ventas.dto.ResponseOk;
import com.sistema.ventas.exceptions.BOException;
import com.sistema.ventas.exceptions.CustomExceptionHandler;
import com.sistema.ventas.model.Genero;
import com.sistema.ventas.model.TiposIdentificacion;
import com.sistema.ventas.util.MensajesUtil;

@RestController
public class ServiciosUtilAPI {

	@SuppressWarnings("unused")
	private static final Logger logger = LogManager.getLogger(ServiciosUtilAPI.class.getName());
	
	@Autowired
	private IServiciosUtilBO objITipoIdentificacionBO;
	@Autowired
	private IGeneroBO objIGeneroBO;
	
	@RequestMapping(value="/tipoIdentificacion",method = RequestMethod.GET)
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
	
	@RequestMapping(value="/genero",method = RequestMethod.GET)
	public ResponseEntity<?> listarGenero(
			) throws BOException {
		
		try {

			List<Genero> lsGenero = objIGeneroBO.findAll();

			return new ResponseEntity<>(new ResponseOk(
					MensajesUtil.getMensaje("ven.response.ok", MensajesUtil.validateSupportedLocale(null)),
					lsGenero), HttpStatus.OK);
		} catch (BOException be) {
			logger.error(" ERROR => " + be.getTranslatedMessage(null));
			throw new CustomExceptionHandler(be.getTranslatedMessage(null), be.getData());
		}
	}
	

}
