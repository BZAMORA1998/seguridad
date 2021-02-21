package com.sistema.ventas.util;


import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.jboss.resteasy.util.Base64;

import com.sistema.ventas.enums.AlgoritmosIdentificacion;
import com.sistema.ventas.enums.FormatoFecha;
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
		 * @throws IOException 
	 */
	public static String decodificaBase64(String stringBase64) throws BOException{
		String decodeString = null;

			// VALIDA Y DECODIFICA EL STRING BASE64
			if (!StringUtils.isBlank(stringBase64)) {
				try {
					decodeString = new String(Base64.decode(stringBase64.getBytes()));
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
	
	/**
	 * Evalua si una identificacion es correcta aplicando el respectivo algoritmo de
	 * validacion.
	 * 
	 * @author Brian Torres
	 * @param identificacion          Identificacion a validar.
	 * @param algoritmoIdentificacion Tipo de algoritmo a validar.
	 * @return
	 */
	public static boolean identificacionValida(String identificacion,
			AlgoritmosIdentificacion algoritmoIdentificacion) {
		return validaAlgoritmoIdentificacion(identificacion, algoritmoIdentificacion.getName());
	}

	/**
	 * Evalua si una identificacion es correcta aplicando el respectivo algoritmo de
	 * validacion.
	 * 
	 * @author Brian Torres
	 * @param identificacion          Identificacion a validar.
	 * @param algoritmoIdentificacion Tipo de algoritmo a validar.
	 * @return
	 */
	public static boolean identificacionValida(String identificacion, String algoritmoIdentificacion) {
		String strAlgoritmoFound = null;
		for (AlgoritmosIdentificacion alg : AlgoritmosIdentificacion.values()) {
			if (alg.getName().equals(algoritmoIdentificacion)) {
				strAlgoritmoFound = alg.getName();
				break;
			}
		}
		if (strAlgoritmoFound == null) {
			throw new RuntimeException("El algoritmo de identificacion no es valido.");
		}
		return validaAlgoritmoIdentificacion(identificacion, strAlgoritmoFound);
	}

	/**
	 * Ejecuta el algoritmo de validacion respectivo segun el tipo indicado.
	 * 
	 * @author Brian Torres
	 * @param identificacion
	 * @param algoritmoIdentificacion
	 * @return
	 */
	public static boolean validaAlgoritmoIdentificacion(String identificacion, String algoritmoIdentificacion) {
		if (algoritmoIdentificacion.equals(AlgoritmosIdentificacion.CEDULA_IDENTIDAD_EC.getName())) {
			return validaCedulaIdentidadEc(identificacion);
		} else if (algoritmoIdentificacion.equals(AlgoritmosIdentificacion.REGISTRO_UNICO_CONTRIBUYENTE_EC.getName())) {
			return validaRegistroUnicoContribuyenteEc(identificacion);
		} else if (algoritmoIdentificacion.equals(AlgoritmosIdentificacion.PASAPORTE.getName())) {
			return validaPasaporte(identificacion);
		}
		return false;
	}

	/**
	 * Validacion de Cedula de ciudadania de Ecuador
	 *  - Se complementa Funcion validando que tenga 10 digitos
	 * @author Brian Torres
	 * @param identificacion Identificacion a validar.
	 * @return
	 */
	private static boolean validaCedulaIdentidadEc(String identificacion) {
		if (identificacion!=null && identificacion.trim().length()!=10) {
			return false;
		}
		return validadorTipoIdentificacion(identificacion);
	}

	/**
	 * Validacion de Registro unico de contribuyente (RUC) de Ecuador.
	 *  - Se complementa Funcion validando que tenga 13 digitos
	 * @author Brian Torres
	 * @param identificacion Identificacion a validar.
	 * @return
	 */
	private static boolean validaRegistroUnicoContribuyenteEc(String identificacion) {
		if (identificacion!=null && identificacion.trim().length()!=13) {
			return false;
		}
		return validadorTipoIdentificacion(identificacion);
	}
	
	/**
	 * Validacion basica de un pasaporte
	 * 
	 * @author Brian Torres
	 * @param identificacion
	 * @return
	 */
	private static boolean validaPasaporte(String identificacion) {
		if (identificacion == null || identificacion.trim().equals("")) {
			return false;
		}
		//Valida que solo contenga letras y numeros
		if (!soloLetrasYNumeros(identificacion)) {
			return false;
		}
		//Valida que tenga un maximo de 20 digitos
		if (identificacion.length() > 20) {
			return false;
		}
		return true;
	}
	
	
	/**
     * Link de SRI y Registro Civil:
     * https://servicios.registrocivil.gob.ec/cdd/
     * http://www.sri.gob.ec/web/guest/RUC
     * https://declaraciones.sri.gob.ec/sri-en-linea/#/SriRucWeb/ConsultaRuc/Consultas/consultaRuc
     * https://medium.com/@bryansuarez/c%C3%B3mo-validar-c%C3%A9dula-y-ruc-en-ecuador-b62c5666186f
     * http://telesjimenez.blogspot.com/2011/05/algoritmo-de-verificacion-de-ruc_21.html
     */
	
    public static boolean validadorTipoIdentificacion(String strNumeroIdentificacion) {
        return validaNumeroIdentificacion(strNumeroIdentificacion);
    }
    
    public static boolean validaNumeroIdentificacion(String numeroIdentificacion) {
        int numeroProvincias = 24;

        if (numeroIdentificacion != null && numeroIdentificacion.trim().length() >= 10) {
            numeroIdentificacion = numeroIdentificacion.trim();

            if (numeroIdentificacion.matches("[0-9]*")) {
                int provincia = Integer.parseInt(numeroIdentificacion.charAt(0) + "" + numeroIdentificacion.charAt(1));
                int digitoTres = Integer.parseInt(numeroIdentificacion.charAt(2) + "");

                if (numeroIdentificacion.length() == 10) {
                    if (((provincia > 0 && provincia <= numeroProvincias) || provincia == 30) && digitoTres <= 6) {
                        if (validaCedulaRucPersonasNaturales(numeroIdentificacion)) {
                            return true;
                        }
                    }
                } else if (numeroIdentificacion.length() == 13) {
                    if (numeroIdentificacion.substring(10).equals("001")) {
                        if ((provincia > 0 && provincia <= numeroProvincias) && digitoTres < 6) {
                            if (validaCedulaRucPersonasNaturales(numeroIdentificacion)) {
                                return true;
                            }
                        } else if ((provincia > 0 && provincia <= numeroProvincias) && digitoTres == 9) {
                            if (validaRucInstitucionesPrivadas(numeroIdentificacion)) {
                                return true;
                            }
                        } else if ((provincia > 0 && provincia <= numeroProvincias) && digitoTres == 6) {
                            if (validaRucInstitucionesPublicas(numeroIdentificacion)) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public static boolean validaCedulaRucPersonasNaturales(String numeroIdentificacion) {
        int total = 0;
        int[] coeficientes = {2, 1, 2, 1, 2, 1, 2, 1, 2};
        int numeroProvincias = 24;
        int tercerDigito = 6;

        if (numeroIdentificacion.matches("[0-9]*") & ((numeroIdentificacion.length() == 10) || (numeroIdentificacion.length() == 13))) {
            int provincia = Integer.parseInt(numeroIdentificacion.charAt(0) + "" + numeroIdentificacion.charAt(1));
            int digitoTres = Integer.parseInt(numeroIdentificacion.charAt(2) + "");

            // Valida cedula y ruc personas naturales
            if (((provincia > 0 && provincia <= numeroProvincias) || provincia == 30) && (digitoTres <= tercerDigito)) {
                int digitoVerificadorRecibido = Integer.parseInt(numeroIdentificacion.charAt(9) + "");

                for (int i = 0; i < coeficientes.length; i++) {
                    int valor = Integer.parseInt(coeficientes[i] + "") * Integer.parseInt(numeroIdentificacion.charAt(i) + "");
                    total = valor >= 10 ? total + (valor - 9) : total + valor;
                }
                int digitoVerificadorObtenido = (total % 10) != 0 ? 10 - (total % 10) : (total % 10);

                if (digitoVerificadorObtenido == digitoVerificadorRecibido) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    public static boolean validaRucInstitucionesPrivadas(String ruc) {
        int total = 0;
        int[] coeficientes = {4, 3, 2, 7, 6, 5, 4, 3, 2};
        int numeroProvincias = 24;
        int tercerDigito = 9;

        if (ruc.matches("[0-9]*") && ruc.length() == 13) {
            int provincia = Integer.parseInt(ruc.charAt(0) + "" + ruc.charAt(1));
            int digitoTres = Integer.parseInt(ruc.charAt(2) + "");

            // Valida RUC instituciones privadas
            if ((provincia > 0 && provincia <= numeroProvincias) && (digitoTres == tercerDigito)) {
                int digitoVerificadorRecibido = Integer.parseInt(ruc.charAt(9) + "");

                for (int i = 0; i < coeficientes.length; i++) {
                    int valor = Integer.parseInt(coeficientes[i] + "") * Integer.parseInt(ruc.charAt(i) + "");
                    total = total + valor;
                }
                int digitoVerificadorObtenido = (total % 11) != 0 ? 11 - (total % 11) : (total % 11);

                if (digitoVerificadorObtenido == digitoVerificadorRecibido) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    public static boolean validaRucInstitucionesPublicas(String ruc) {
        int total = 0;
        int[] coeficientes = {3, 2, 7, 6, 5, 4, 3, 2};
        int numeroProvincias = 24;
        int tercerDigito = 6;

        if (ruc.matches("[0-9]*") && ruc.length() == 13) {
            int provincia = Integer.parseInt(ruc.charAt(0) + "" + ruc.charAt(1));
            int digitoTres = Integer.parseInt(ruc.charAt(2) + "");

            // Valida RUC instituciones publicas
            if ((provincia > 0 && provincia <= numeroProvincias) && (digitoTres == tercerDigito)) {
                int digitoVerificadorRecibido = Integer.parseInt(ruc.charAt(8) + "");

                for (int i = 0; i < coeficientes.length; i++) {
                    int valor = Integer.parseInt(coeficientes[i] + "") * Integer.parseInt(ruc.charAt(i) + "");
                    total = total + valor;
                }
                int digitoVerificadorObtenido = (total % 11) != 0 ? 11 - (total % 11) : (total % 11);

                if (digitoVerificadorObtenido == digitoVerificadorRecibido) {
                    return true;
                }
            }
            return false;
        }
        return false;
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
	 * Convierte una fecha Date a String
	 * 
	 * @author Brian Torres
	 * @param datFecha
	 * @param formatoFecha
	 * @return
	 */
	public static String dateToString(Date datFecha, FormatoFecha formatoFecha) {
		SimpleDateFormat formatter = new SimpleDateFormat(formatoFecha.getName());
		return formatter.format(datFecha);
	}
	
	/**
	 * Concatena apellidos y nombres.
	 * 
	 * @author Bryan Zamora
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
	
}
