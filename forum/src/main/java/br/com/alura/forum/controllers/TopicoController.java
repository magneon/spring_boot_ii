package br.com.alura.forum.controllers;

import java.net.URI;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.alura.forum.controllers.dtos.DetalheTopicoDTO;
import br.com.alura.forum.controllers.dtos.TopicoDTO;
import br.com.alura.forum.controllers.forms.AtualizaTopicoForm;
import br.com.alura.forum.controllers.forms.TopicoForm;
import br.com.alura.forum.models.Topico;
import br.com.alura.forum.repositories.CursoRepository;
import br.com.alura.forum.repositories.TopicoRepository;

@RestController
@RequestMapping("/topicos")	
public class TopicoController {
	
	@Autowired
	private TopicoRepository repositoryTopico;
	
	@Autowired
	private CursoRepository repositoryCurso;
	
	@GetMapping
	@Cacheable(value = "topicos")
	//public Page<TopicoDTO> topicos(@RequestParam(required = false) String nomeCurso, @RequestParam int pagina, @RequestParam int quantidade, @RequestParam String ordenacao) {
	public Page<TopicoDTO> topicos(@RequestParam(required = false) String nomeCurso, @PageableDefault(page = 0, size = 10, direction = Sort.Direction.ASC, sort = "titulo") Pageable paginacao) {
		Page<Topico> topicos = null;
		
		if (nomeCurso == null) {
			topicos = repositoryTopico.findAll(paginacao);
		} else {
			topicos = repositoryTopico.findByCursoNome(nomeCurso, paginacao);
			//topicos = repositoryTopico.pegaPorCursoNome(nomeCurso);
		}
		return TopicoDTO.converter(topicos);
	}
	
	@Transactional
	@PostMapping
	@CacheEvict(value = "topicos", allEntries = true)
	public ResponseEntity<TopicoDTO> cadastrar(@RequestBody @Valid TopicoForm form, UriComponentsBuilder uriBuilder) {
		Topico topico = form.converter(repositoryCurso);
		
		repositoryTopico.save(topico);
		
		URI uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
		
		return ResponseEntity.created(uri).body(new TopicoDTO(topico));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<DetalheTopicoDTO> detalhar(@PathVariable Long id) {
		Optional<Topico> optional = repositoryTopico.findById(id);
		if (optional.isPresent()) {
			return ResponseEntity.ok(new DetalheTopicoDTO(optional.get()));			
		}
		
		return ResponseEntity.notFound().build();
		
	}
	
	@Transactional
	@PutMapping("/{id}")
	@CacheEvict(value = "topicos", allEntries = true)
	public ResponseEntity<TopicoDTO> atualizar(@PathVariable Long id, @RequestBody @Valid AtualizaTopicoForm form) {
		Optional<Topico> optional = repositoryTopico.findById(id);
		if (optional.isPresent()) {
			Topico topico = form.atualizar(id, repositoryTopico);
			
			return ResponseEntity.ok(new TopicoDTO(topico));
		}
		
		return ResponseEntity.notFound().build();
	}
	
	@Transactional
	@DeleteMapping("/{id}")
	@CacheEvict(value = "topicos", allEntries = true)
	public ResponseEntity<?> remover(@PathVariable Long id) {
		Optional<Topico> optional = repositoryTopico.findById(id);
		if (optional.isPresent()) {
			repositoryTopico.deleteById(id);
			
			return ResponseEntity.ok().build();
		}
		
		return ResponseEntity.notFound().build();
	}
	
}