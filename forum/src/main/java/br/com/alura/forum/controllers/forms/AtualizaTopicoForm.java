package br.com.alura.forum.controllers.forms;

import br.com.alura.forum.models.Topico;
import br.com.alura.forum.repositories.TopicoRepository;

public class AtualizaTopicoForm {

	private String titulo;
	private String mensagem;

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public Topico atualizar(Long id, TopicoRepository repositoryTopico) {
		Topico topico = repositoryTopico.getOne(id);
		
		topico.setTitulo(this.getTitulo());
		topico.setMensagem(this.getMensagem());
		
		return topico;
	}

}