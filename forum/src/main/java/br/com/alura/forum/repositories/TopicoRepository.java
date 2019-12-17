package br.com.alura.forum.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.alura.forum.models.Topico;

public interface TopicoRepository extends JpaRepository<Topico, Long> {

	Page<Topico> findByCursoNome(String nomeCurso, Pageable paginacao);
	
	@Query("SELECT t FROM topico t WHERE t.curso.nome = :nomeCurso")
	List<Topico> pegaPorCursoNome(@Param("nomeCurso") String nomeCurso);

}
