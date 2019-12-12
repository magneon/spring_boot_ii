package br.com.alura.forum.controllers.advice;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.com.alura.forum.controllers.dtos.ErroDTO;

@RestControllerAdvice
public class ControllerAdvice {
	
	@Autowired
	private MessageSource messageSource;
	
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	@ExceptionHandler(value = MethodArgumentNotValidException.class)
	public List<ErroDTO> handle(MethodArgumentNotValidException exception) {
		List<ErroDTO> erros = new ArrayList<>();
		
		List<FieldError> errors = exception.getBindingResult().getFieldErrors();
		errors.stream().forEach(error -> {
			erros.add(new ErroDTO(error.getField(), messageSource.getMessage(error, LocaleContextHolder.getLocale())));
		});
		
		return erros;
		
	}

}
