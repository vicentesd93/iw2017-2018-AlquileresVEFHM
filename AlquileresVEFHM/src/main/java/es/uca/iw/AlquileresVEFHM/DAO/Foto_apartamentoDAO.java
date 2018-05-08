package es.uca.iw.AlquileresVEFHM.DAO;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import es.uca.iw.AlquileresVEFHM.modelos.Foto_apartamento;

@Transactional
public interface Foto_apartamentoDAO extends CrudRepository<Foto_apartamento, Integer>{

}
