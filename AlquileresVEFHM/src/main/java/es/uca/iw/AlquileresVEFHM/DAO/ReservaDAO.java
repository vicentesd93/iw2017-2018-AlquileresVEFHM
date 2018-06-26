package es.uca.iw.AlquileresVEFHM.DAO;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import es.uca.iw.AlquileresVEFHM.modelos.Reserva;
import es.uca.iw.AlquileresVEFHM.modelos.User;

public interface ReservaDAO extends CrudRepository<Reserva, Integer> {
	List<Reserva> findByHuesped(User huesped);
	
	@Query(value = "SELECT DISTINCT reserva.* FROM usuario, reserva, apartamento, oferta, reservaoferta WHERE :anfi = apartamento.anfitrion AND apartamento._id = oferta.apartamento AND oferta._id = reservaoferta.oferta AND reservaoferta.reserva = reserva._id", nativeQuery = true)
	List<Reserva> buscarporAnfitrion(@Param("anfi") User anfitrion);
}