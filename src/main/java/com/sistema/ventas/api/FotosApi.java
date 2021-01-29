package com.sistema.ventas.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sistema.ventas.bo.IFotosBO;
import com.sistema.ventas.dto.ResponseOk;
import com.sistema.ventas.exceptions.BOException;
import com.sistema.ventas.exceptions.CustomExceptionHandler;
import com.sistema.ventas.util.MensajesUtil;

@RestController
@RequestMapping("/fotos")
public class FotosApi {
	
	@SuppressWarnings("unused")
	private static final Logger logger = LogManager.getLogger(FotosApi.class.getName());
	
	@Autowired
	private IFotosBO objFotosBO;

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> consultarFotos(@RequestHeader(	
			value = "Accept-Language", 	required = false) String strLanguage,
			@RequestParam(name = "nemonico", required = false) String strNemonico,
			@RequestParam(name = "nombre", required = false) String strNombre,
			@RequestParam(value="secuenciaEmpresa", required = false)  Integer  intCodigoEmpresa
			) throws BOException {
		
		try {
			return new ResponseEntity<>(new ResponseOk(MensajesUtil.getMensaje("ven.response.ok", 
										MensajesUtil.validateSupportedLocale(strLanguage)),objFotosBO.consultarFotos(intCodigoEmpresa,strNemonico,strNombre))
										, HttpStatus.OK);
		} catch (BOException be) {
			logger.error(" ERROR => " + be.getTranslatedMessage(strLanguage));
			throw new CustomExceptionHandler(be.getTranslatedMessage(strLanguage), be.getData());
		}
		
	}
}

