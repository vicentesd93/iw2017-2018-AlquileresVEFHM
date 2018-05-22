package es.uca.iw.AlquileresVEFHM.DAO;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import es.uca.iw.AlquileresVEFHM.modelos.Factura;

@Transactional
public interface FacturaDAO extends CrudRepository<Factura, Integer> {

}
