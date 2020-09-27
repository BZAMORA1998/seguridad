package com.sistema.ventas.util;

import java.io.IOException;
import java.nio.charset.Charset;
import org.apache.commons.lang3.StringUtils;
import org.jboss.resteasy.util.Base64;
import com.sistema.ventas.exceptions.BOException;

public class GeneralUtil {
		/**
	 *
	 * Decodifica Base64
	 * 
	 * @author Iván Marriott
	 * @param stringBase64
	 * @return
	 * @throws BOException
	 */
	public static String decodificaBase64(String stringBase64) throws BOException{
		String decodeString = null;
		try {
			// VALIDA Y DECODIFICA EL STRING BASE64
			if (!StringUtils.isBlank(stringBase64)) {
				decodeString = new String(Base64.decode(stringBase64.getBytes()), Charset.forName("UTF-8"));
			}
		} catch (IOException e) {
			throw new BOException("ven.error.errorDecodeAuth");
		}
		// RETORNA EL STRING stringBase64
		return decodeString;
	}
}
