package com.sistema.ventas.util;


import java.io.IOException;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.jboss.resteasy.util.Base64;

import com.sistema.ventas.enumm.FormatoFecha;
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

			// VALIDA Y DECODIFICA EL STRING BASE64
			if (!StringUtils.isBlank(stringBase64)) {
				try {
					decodeString = new String(Base64.decode(stringBase64.getBytes()), Charset.forName("UTF-8"));
				} catch (IOException e) {
					throw new BOException("ven.error.errorDecodeAuth");
				}
			}
	
		// RETORNA EL STRING stringBase64
		return decodeString;
	}
	

	
	/**
	 * Valida si el formato de una fecha en String es correcto.
	 * 
	 * @author Brian Torres
	 * @param strDate      Fecha en String
	 * @param formatoFecha Tipo de formato de fecha a evaluar.
	 * @return
	 */
	public static boolean formatoFechaValido(String strDate, FormatoFecha formatoFecha) {
		if (strDate.trim().equals("")) {
			return false;
		}
		SimpleDateFormat sdfrmt = new SimpleDateFormat(formatoFecha.getName());
		sdfrmt.setLenient(false);
		try {
			sdfrmt.parse(strDate);
		} catch (ParseException e) {
			return false;
		}
		return true;
	}
	
	/**
	 * Convierte una fecha string a tipo de dato java.util.Date
	 * 
	 * @author Ivan Marriott
	 * @param strFecha
	 * @return
	 */
	public static Date stringToDate(String strFecha, FormatoFecha formatoFecha) {
		if (!formatoFechaValido(strFecha, formatoFecha)) {
			throw new RuntimeException("La fecha no cumple con el formato indicado.");
		}
		try {
			SimpleDateFormat formatter = new SimpleDateFormat(formatoFecha.getName());
			return formatter.parse(strFecha);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
