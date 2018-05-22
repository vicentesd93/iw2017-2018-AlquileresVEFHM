package es.uca.iw.AlquileresVEFHM.DAO;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import es.uca.iw.AlquileresVEFHM.modelos.Reserva;

@Transactional
public interface ReservaDAO extends CrudRepository<Reserva, Integer> {

}
