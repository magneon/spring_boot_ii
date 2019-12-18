package br.com.alura.forum.controllers.dtos;

public class TokenDTO {

	private String tipo;
	private String token;

	public TokenDTO(String tipo, String token) {
		this.tipo = tipo;
		this.token = token;
	}

	public String getTipo() {
		return tipo;
	}

	public String getToken() {
		return token;
	}

}