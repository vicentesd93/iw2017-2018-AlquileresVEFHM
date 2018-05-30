package es.uca.iw.AlquileresVEFHM.DAO;

import java.time.LocalDate;
import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import es.uca.iw.AlquileresVEFHM.modelos.Oferta;

public interface OfertaDAO extends CrudRepository<Oferta, Integer> {
	@Query(value = "SELECT * FROM oferta WHERE apartamento = :apartamento AND fecha >= :inicio AND fecha <= :fin ORDER BY fecha", nativeQuery = true)
	Set<Oferta> ofertaIntervalo(@Param("apartamento") Integer Idapartamento, @Param("inicio") LocalDate f_inicio, @Param("fin") LocalDate f_fin);
}
