package es.uca.iw.AlquileresVEFHM.DAO;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import es.uca.iw.AlquileresVEFHM.modelos.Apartamento;

public interface ApartamentoDAO extends CrudRepository<Apartamento, Integer> {
	//@Query(value = "SELECT DISTINCT apartamento.* FROM apartamento INNER JOIN oferta ON apartamento._id = oferta.apartamento AND oferta.fecha >= DATE(NOW()) AND reserva IS NULL", nativeQuery = true)
	//@Query(value = "SELECT DISTINCT apartamento.* FROM apartamento, oferta, reserva WHERE (apartamento._id = oferta.apartamento AND oferta.fecha >= DATE(NOW()) AND oferta.reserva IS NULL) OR (reserva._id = oferta.reserva AND apartamento._id = oferta.apartamento AND reserva.rechazada = 1)", nativeQuery = true)
	@Query(value = "SELECT DISTINCT apartamento.* FROM apartamento, oferta WHERE (apartamento._id = oferta.apartamento AND oferta.fecha >= DATE(NOW()) AND oferta._id NOT IN (SELECT oferta FROM reservaoferta)) OR (apartamento._id = oferta.apartamento AND oferta._id IN (SELECT oferta FROM reservaoferta, reserva WHERE reservaoferta.reserva = reserva._id AND reserva.rechazada = 1))", nativeQuery = true)
	List<Apartamento> apartamentosOfertados();
	
	//@Query(value = "SELECT DISTINCT apartamento.* FROM apartamento INNER JOIN oferta ON apartamento._id = oferta.apartamento AND oferta.fecha >= DATE(NOW()) AND reserva IS NULL AND apartamento._id = :id", nativeQuery = true)
	//@Query(value = "SELECT DISTINCT apartamento.* FROM apartamento, oferta, reserva WHERE (apartamento._id = oferta.apartamento AND oferta.fecha >= DATE(NOW()) AND oferta.reserva IS NULL AND apartamento._id = :id) OR (reserva._id = oferta.reserva AND apartamento._id = oferta.apartamento AND reserva.rechazada = 1 AND apartamento._id = :id)", nativeQuery = true)
	@Query(value = "SELECT DISTINCT apartamento.* FROM apartamento, oferta WHERE (apartamento._id = oferta.apartamento AND oferta.fecha >= DATE(NOW()) AND oferta._id NOT IN (SELECT oferta FROM reservaoferta) AND apartamento._id = :id) OR (apartamento._id = oferta.apartamento AND oferta._id IN (SELECT oferta FROM reservaoferta, reserva WHERE reservaoferta.reserva = reserva._id AND reserva.rechazada = 1) AND apartamento._id = :id)", nativeQuery = true)
	Apartamento apartamentoOfertado(@Param("id") Integer Id);
	
	Set<Apartamento> findAll();
}
