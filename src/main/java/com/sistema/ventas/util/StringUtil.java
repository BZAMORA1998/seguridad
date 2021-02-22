package com.sistema.ventas.util;

import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.Base64;

/**
 * Helper de validaciones de cadenas.
 * 
 * @author Brian Torres
 *
 */
public class StringUtil {
	
	/**
	 * Codifica un string a Base64 seguro de usar en URL.
	 * 
	 * @author Brian Torres
	 * @param input
	 * @return
	 */
	public static String base64UrlEncode(String input) {
		return Base64.encodeBase64URLSafeString(input.getBytes(Charset.forName("UTF-8")));
	}
	
	/**
	 * Decodifica un string que se encuentra codificado en Base64Url. 
	 * 
	 * @author Brian Torres
	 * @param input
	 * @return
	 */
	public static String base64UrlDecode(String input) {
		return new String(Base64.decodeBase64(input), Charset.forName("UTF-8"));
	}
	
	/**
	 * Concatena apellidos y nombres.
	 * 
	 * @author Brian Torres
	 * @param primerApellido
	 * @param segundoApellido
	 * @param primerNombre
	 * @param segundoNombre
	 * @return
	 */
	public static String concatenarApellidosNombres(String primerApellido, String segundoApellido, String primerNombre,
			String segundoNombre) {
		String strNombreCompleto = "";
		strNombreCompleto = primerApellido != null && !primerApellido.trim().equals("") ? primerApellido : "";
		strNombreCompleto = strNombreCompleto + (segundoApellido != null && !segundoApellido.trim().equals("") ? " " + segundoApellido : "");
		strNombreCompleto = strNombreCompleto + (primerNombre != null && !primerNombre.trim().equals("") ? " " + primerNombre : "");
		strNombreCompleto = strNombreCompleto + (segundoNombre != null && !segundoNombre.trim().equals("") ? " " + segundoNombre : "");
		return strNombreCompleto.trim().toUpperCase();
	}
	
	
	/**
	 * Valida cantidad de digitos de una cadeana.
	 * 
	 * @author Brian Torres
	 * @param cadena
	 * @param cantidadDigitos
	 * @return
	 */
	public static boolean cantidadDigitosValida(String cadena, int cantidadDigitos) {
		if (cadena != null && !cadena.trim().equals("")) {
			final String VALID = ("^\\d{"+cantidadDigitos+"}");
			return cadena.matches(VALID);
		}
		return false;
	}

	/**
	 * Valida si solamente existen letras en una cadena.
	 * 
	 * @author Brian Torres
	 * @param input
	 * @return
	 */
	public static boolean soloLetras(String input) {
		String regx = "^[\\p{L}]+$";
		Pattern pattern = Pattern.compile(regx, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(input);
		return matcher.find();
	}
	
	/**
	 * Valida si solamente existen letras y espacios en blanco en una cadena.
	 * 
	 * @author Brian Torres
	 * @param input
	 * @return
	 */
	public static boolean soloLetrasYEspacio(String input) {
		String regx = "^[\\p{L} ]+$";
		Pattern pattern = Pattern.compile(regx, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(input);
		return matcher.find();
	}
	
	/**
	 * Valida si solamente existen numeros en una cadena.
	 * 
	 * @author Brian Torres
	 * @param input
	 * @return
	 */
	public static boolean soloNumeros(String input) {
		String regx = "\\d+";
		return input.matches(regx);
	}
	
	/**
	 * Valida si solamente existen letras y numeros en una cadena.
	 * 
	 * @author Brian Torres
	 * @param input
	 * @return
	 */
	public static boolean soloLetrasYNumeros(String input) {
		String regx = "^[a-zA-Z0-9]+$";
		return input.matches(regx);
	}
	
	/**
	 * Elimina acentos de una cadena.
	 * 
	 * @author Brian Torres
	 * @param input
	 * @return
	 */
	public static String eliminarAcentos(String input) {

		final String ORIGINAL = "ÁáÉéÍíÓóÚúÜü";
		final String REEMPLAZO = "AaEeIiOoUuUu";

		if (input == null) {
			return null;
		}
		char[] array = input.toCharArray();
		for (int indice = 0; indice < array.length; indice++) {
			int pos = ORIGINAL.indexOf(array[indice]);
			if (pos > -1) {
				array[indice] = REEMPLAZO.charAt(pos);
			}
		}
		return new String(array);
	}
	
}
