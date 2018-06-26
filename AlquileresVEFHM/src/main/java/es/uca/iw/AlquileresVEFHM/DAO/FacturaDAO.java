package es.uca.iw.AlquileresVEFHM.DAO;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import es.uca.iw.AlquileresVEFHM.modelos.Factura;
import es.uca.iw.AlquileresVEFHM.modelos.User;

@Transactional
public interface FacturaDAO extends CrudRepository<Factura, Integer> {
	List<Factura> findAll();
	
	@Query(value = "SELECT DISTINCT factura.* FROM factura, reserva, reservaoferta, oferta, apartamento WHERE factura._id = reserva.factura AND reserva._id = reservaoferta.reserva AND reservaoferta.oferta = oferta._id AND oferta.apartamento = apartamento._id AND apartamento.anfitrion = :user", nativeQuery = true)
	List<Factura> buscarAnfitrion(@Param("user") User user);
}
