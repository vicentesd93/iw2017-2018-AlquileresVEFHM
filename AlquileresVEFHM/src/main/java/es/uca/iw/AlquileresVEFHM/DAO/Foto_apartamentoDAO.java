package es.uca.iw.AlquileresVEFHM.DAO;

import java.util.ArrayList;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import es.uca.iw.AlquileresVEFHM.modelos.Foto_apartamento;

@Transactional
public interface Foto_apartamentoDAO extends CrudRepository<Foto_apartamento, Integer>{
	public ArrayList<Foto_apartamento> findByApartamento(Integer apartamento);
	public Foto_apartamento findByNombre(String nombre);
}
