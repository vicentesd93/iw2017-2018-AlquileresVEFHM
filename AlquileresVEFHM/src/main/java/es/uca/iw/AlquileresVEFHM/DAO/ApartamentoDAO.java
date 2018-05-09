package es.uca.iw.AlquileresVEFHM.DAO;

import java.util.ArrayList;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import es.uca.iw.AlquileresVEFHM.modelos.Apartamento;

@Transactional
public interface ApartamentoDAO extends CrudRepository<Apartamento, Integer>{
	public ArrayList<Apartamento> findByAnfitrion(Integer anfitrion);
}
