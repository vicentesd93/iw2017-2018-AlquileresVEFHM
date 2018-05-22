package es.uca.iw.AlquileresVEFHM.DAO;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import es.uca.iw.AlquileresVEFHM.modelos.Oferta;

@Transactional
public interface OfertaDAO extends CrudRepository<Oferta, Integer> {

}
