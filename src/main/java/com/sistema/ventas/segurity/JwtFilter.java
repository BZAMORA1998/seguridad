package com.sistema.ventas.segurity;

import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtFilter extends OncePerRequestFilter {

	private static final Logger logger = Logger.getLogger(JwtFilter.class.getName());
	
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private CustomUserDetailsService service;


    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException{
	    try {
	        String authorizationHeader = httpServletRequest.getHeader("Authorization");
	        String[] arrServiciosSinSeguridad = { "/ventas/v1/general/tipoIdentificacion",
	        		"/ventas/v1/general/genero",
	        		"/ventas/v1/usuarios/crearUsuario"
	        		,"/ventas/v1/usuarios/photo"
	        		,"/ventas/v1/empresas/datosEmpresa"
	        		,"/ventas/v1/fotos"
	        		,"/ventas/v1/empresas"
	        		,"/ventas/v1/general/opciones_menu"};	
	        
	        String token = null;
	        String userName = null;
	
	        System.out.print("=>"+httpServletRequest.getRequestURI());
	        
	        if (!(httpServletRequest.getRequestURI()!=null && 
					Arrays.stream(arrServiciosSinSeguridad).anyMatch(StringUtils.upperCase(httpServletRequest.getRequestURI())::equalsIgnoreCase))) {
		        	
		        	 if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
				            token = authorizationHeader.substring(7);
							userName = jwtUtil.extractUsername(token);
				        }
				
				        if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				
				            UserDetails userDetails = service.loadUserByUsername(userName);
				
				            if (jwtUtil.validateToken(token, userDetails)) {
				
				                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
				                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
				                usernamePasswordAuthenticationToken
				                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
				                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
				            }
				        }
		        }else {
		        	 UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
		                        new UsernamePasswordAuthenticationToken(null, null,null);
		                usernamePasswordAuthenticationToken
		                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
		                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
		        }
		       
		        
			} catch (Exception e) {
				e.printStackTrace();
				httpServletResponse.setStatus(Status.UNAUTHORIZED.getStatusCode());
				httpServletResponse.setHeader("msgAutorizadorException", e.getMessage());
				logger.log(Level.SEVERE, null, e);
				throw new ClientErrorException(e.getMessage()!=null?e.getMessage():"Exception SecurityRequestFilter: ", Status.UNAUTHORIZED ,e);
			}
	    filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
