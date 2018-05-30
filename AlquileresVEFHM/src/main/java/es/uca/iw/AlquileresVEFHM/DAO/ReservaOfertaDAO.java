package es.uca.iw.AlquileresVEFHM.DAO;

import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import es.uca.iw.AlquileresVEFHM.modelos.Reserva;
import es.uca.iw.AlquileresVEFHM.modelos.ReservaOferta;

public interface ReservaOfertaDAO  extends CrudRepository<ReservaOferta, Integer>{
	@Query(value = "SELECT DISTINCT reserva.* FROM reserva, reservaoferta WHERE reserva._id = reservaoferta.reserva AND reservaoferta.oferta = :oferta", nativeQuery = true)
	Set<Reserva> reservasporoferta(@Param("oferta") int IdOferta);
}
