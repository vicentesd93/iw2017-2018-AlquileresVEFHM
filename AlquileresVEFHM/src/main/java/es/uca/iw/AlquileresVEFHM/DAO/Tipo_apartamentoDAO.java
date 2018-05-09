package es.uca.iw.AlquileresVEFHM.DAO;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import es.uca.iw.AlquileresVEFHM.modelos.Tipo_apartamento;

@Transactional
public interface Tipo_apartamentoDAO extends CrudRepository<Tipo_apartamento, Integer>{
	
}