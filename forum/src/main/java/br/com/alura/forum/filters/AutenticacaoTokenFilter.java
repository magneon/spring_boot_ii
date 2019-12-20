package br.com.alura.forum.filters;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

import br.com.alura.forum.services.TokenService;

/*
 * TODO Testar a autenticação utilizando CDI do Spring
 */
public class AutenticacaoTokenFilter extends OncePerRequestFilter {
	
	private TokenService serviceToken;
	
	public AutenticacaoTokenFilter(TokenService serviceToken) {
		this.serviceToken = serviceToken;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		
		String token = recuperaTokenDa(request);
		boolean isValido = serviceToken.isTokenValido(token);
		
		System.out.println(isValido);
		
		filterChain.doFilter(request, response);
		
	}

	private String recuperaTokenDa(HttpServletRequest request) {
		String authorizationHeader = request.getHeader("Authorization");
		if (authorizationHeader == null || authorizationHeader.isEmpty() || !(authorizationHeader.startsWith("Bearer"))) {
			return null;
		}
		
		return authorizationHeader.substring(7);		
	}

}
