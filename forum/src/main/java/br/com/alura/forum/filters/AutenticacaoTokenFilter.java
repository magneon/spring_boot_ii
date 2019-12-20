package br.com.alura.forum.filters;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.alura.forum.models.Usuario;
import br.com.alura.forum.repositories.UsuarioRepository;
import br.com.alura.forum.services.TokenService;

/*
 * TODO Testar a autenticação utilizando CDI do Spring
 */
public class AutenticacaoTokenFilter extends OncePerRequestFilter {
	
	private TokenService serviceToken;
	private UsuarioRepository repositoryUsuario;
	
	public AutenticacaoTokenFilter(TokenService serviceToken, UsuarioRepository repositoryUsuario) {
		this.serviceToken = serviceToken;
		this.repositoryUsuario = repositoryUsuario;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		
		String token = recuperaTokenDa(request);
		boolean isValido = serviceToken.isTokenValido(token);
		
		if (isValido) {
			autenticaClienteCom(token);
		}
		
		filterChain.doFilter(request, response);
		
	}

	private String recuperaTokenDa(HttpServletRequest request) {
		String authorizationHeader = request.getHeader("Authorization");
		if (authorizationHeader == null || authorizationHeader.isEmpty() || !(authorizationHeader.startsWith("Bearer"))) {
			return null;
		}
		
		return authorizationHeader.substring(7);
	}
	
	private void autenticaClienteCom(String token) {
		Long idUsuario = serviceToken.getIdUsuario(token);
		Usuario usuario = repositoryUsuario.findById(idUsuario).get();
		
		Authentication auth = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
		
		SecurityContext securityContext = SecurityContextHolder.getContext();
		securityContext.setAuthentication(auth);
	}

}
