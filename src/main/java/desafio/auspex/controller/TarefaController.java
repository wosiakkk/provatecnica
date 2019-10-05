package desafio.auspex.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import desafio.auspex.model.Tarefa;
import desafio.auspex.repository.TarefaRepository;

@RestController
@RequestMapping(value = "/tarefa")
public class TarefaController {
	
	@Autowired
	private TarefaRepository tarefaRepository;
	
	/*Carregar tarefas não finalizadas*/
	@GetMapping(value = "/pendencias")
	private ResponseEntity<List<Tarefa>> tarefasAtivas(){
		List<Tarefa> busca = new ArrayList<Tarefa>();
		try {
			busca = tarefaRepository.buscarTarefasAtivas();
			if(busca.isEmpty())
				return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
			return new ResponseEntity<List<Tarefa>>(busca, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}
	
	/*Carregar tarefas finalizadas*/
	@GetMapping(value = "/finalizadas")
	private ResponseEntity<List<Tarefa>>finalizadas(){
		List<Tarefa> busca = new ArrayList<Tarefa>();
		try {
			busca = tarefaRepository.buscarTarefasFinalizadas();
			if(busca.isEmpty())
				return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
			return new ResponseEntity<List<Tarefa>>(busca, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}
	
	/*Criar uma nova tarefa*/
	@PostMapping(value = "/", produces = "application/json")
	private ResponseEntity<Tarefa> salvar(@RequestBody Tarefa novaTarefa){
		try {
			Tarefa salva = tarefaRepository.save(novaTarefa);
			return new ResponseEntity<Tarefa>(salva,HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}
	
	/*Atualizar uma tarefa*/
	@PutMapping(value = "/{id}", produces = "application/json")
	private ResponseEntity<Tarefa> atualizar(@PathVariable(value = "id")Long id,
											 @RequestBody Tarefa editado){
		try {
			Optional<Tarefa> busca = tarefaRepository.findById(id);
			if(busca.get()!=null) {
				editado.setId(busca.get().getId());
				try {
					editado = tarefaRepository.save(editado);
					return new ResponseEntity<Tarefa>(editado, HttpStatus.CREATED);
				} catch (Exception e) {
					e.printStackTrace();
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
				}
			}else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
			}
		} catch (NoSuchElementException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
	}
	
	/*Deletar uma tarefa*/
	@DeleteMapping(value = "/{id}", produces = "application/json")
	private ResponseEntity<Tarefa> excluir(@PathVariable(value = "id") Long id){
		try {
			Optional<Tarefa> busca = tarefaRepository.findById(id);
			if(busca.get()!=null) {
				try {
					tarefaRepository.delete(busca.get());
					return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
				} catch (Exception e) {
					e.printStackTrace();
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
				}
			}else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
			}
		} catch (NoSuchElementException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
	}
	
	/*Pesquisar uma tarefa*/
	@GetMapping(value = "/{id}", produces = "application/json")
	private ResponseEntity<Tarefa> pesquisar(@PathVariable(value = "id")Long id){
		
		try {
			Optional<Tarefa> busca = tarefaRepository.findById(id);
			if(busca.get()!=null)
				return new ResponseEntity<Tarefa>(busca.get(),HttpStatus.OK);
			else
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		} catch (NoSuchElementException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
	}
	
	/*Finalizar uma tarefa*/
	@PutMapping(value = "/finalizar/{id}")
	private ResponseEntity<Tarefa> finalizar(@PathVariable(value = "id")Long id){
		try {
			Optional<Tarefa> busca = tarefaRepository.findById(id);
			if(busca.get().isAtiva()) {
				busca.get().setAtiva(false);
				try {
					Tarefa retorno = tarefaRepository.save(busca.get());
					return new ResponseEntity<Tarefa>(retorno, HttpStatus.OK);
				} catch (Exception e) {
					e.printStackTrace();
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
				}
			}else {
				return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(null);
			}
		} catch (NoSuchElementException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
	}
	
	/*Reativar uma tarefa*/
	@PutMapping(value = "/ativar/{id}")
	private ResponseEntity<Tarefa> ativar(@PathVariable(value = "id")Long id){
		try {
			Optional<Tarefa> busca = tarefaRepository.findById(id);
			if(!busca.get().isAtiva()) {
				busca.get().setAtiva(true);
				try {
					Tarefa retorno = tarefaRepository.save(busca.get());
					return new ResponseEntity<Tarefa>(retorno, HttpStatus.OK);
				} catch (Exception e) {
					e.printStackTrace();
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
				}
			}else {
				return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(null);
			}
		} catch (NoSuchElementException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
	}
}
