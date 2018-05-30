package es.uca.iw.AlquileresVEFHM.DAO;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import es.uca.iw.AlquileresVEFHM.modelos.Metodo_pago;

public interface Metodo_pagoDAO extends CrudRepository<Metodo_pago, Integer> {
	List<Metodo_pago> findAll();
}
