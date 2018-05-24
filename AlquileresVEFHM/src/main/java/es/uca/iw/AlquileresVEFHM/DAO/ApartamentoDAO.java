package es.uca.iw.AlquileresVEFHM.DAO;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import es.uca.iw.AlquileresVEFHM.modelos.Apartamento;

public interface ApartamentoDAO extends CrudRepository<Apartamento, Integer> {
}
