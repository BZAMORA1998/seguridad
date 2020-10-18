package com.sistema.ventas.enumm;


public enum AuthenticationScheme {
	
	BEARER {
		@Override
		public String toString() {
			return "Bearer";
		}
	}, 
	BASIC {
		@Override
		public String toString() {
			return "Basic";
		}
	};
	
}