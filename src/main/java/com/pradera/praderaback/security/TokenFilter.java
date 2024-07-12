package com.pradera.praderaback.security;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class TokenFilter extends OncePerRequestFilter {

    @Autowired
    private CustomUserDetailsService service;
    @Autowired
    private TokenUtil util;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        //Obteniendo cabecera Authorization del request
        final String tokenHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        //Validar que existe el token y cumple con el formato
        if(tokenHeader != null && tokenHeader.startsWith("Bearer ")) {
            //Obtener el token de la cabecera
            token = tokenHeader.substring(7);
            try {
                //Se obtiene el usuario del token
                username = util.getUsernameFromToken(token);
            } catch (IllegalArgumentException e) {
                logger.warn("Token inválido");
            } catch (ExpiredJwtException e) {
                logger.warn("Token expirado");
            }
        }else {
            logger.warn("Token no es válido");
        }

        //Se valida que se haya obtenido un usuario del token
        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            //Se busca al usuario obtenido del token en la base de datos
            UserDetails user = this.service.loadUserByUsername(username);
            //Se valida que el usuario del token sea el mismo del de base de datos
            if(util.validateToken(token, user)) {
                UsernamePasswordAuthenticationToken usernameToken =
                        new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                usernameToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernameToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
