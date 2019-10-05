package desafio.auspex.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import desafio.auspex.model.Tarefa;

public interface TarefaRepository extends CrudRepository<Tarefa, Long>{
	
	@Query(value = "select t from Tarefa t where t.ativa = true")
	List<Tarefa> buscarTarefasAtivas();
	
	@Query(value = "select t from Tarefa t where t.ativa = false")
	List<Tarefa> buscarTarefasFinalizadas();
}
