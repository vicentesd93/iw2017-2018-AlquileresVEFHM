package es.uca.iw.AlquileresVEFHM;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import es.uca.iw.AlquileresVEFHM.DAO.RolDAO;
import es.uca.iw.AlquileresVEFHM.DAO.Tipo_apartamentoDAO;
import es.uca.iw.AlquileresVEFHM.modelos.Rol;
import es.uca.iw.AlquileresVEFHM.modelos.Tipo_apartamento;

@SpringBootApplication
public class AlquileresVefhmApplication {	
	public static void main(String[] args) {
		SpringApplication.run(AlquileresVefhmApplication.class, args);
	}
	
	@Bean
	public CommandLineRunner loadData(RolDAO rolDao, Tipo_apartamentoDAO t_aparDao) {
	    return (args) -> {
	    	if(!rolDao.findAll().iterator().hasNext()) {
	    		rolDao.save(new Rol("Huesped"));
	    		rolDao.save(new Rol("Anfitrion"));
	    	}
	    	if(!t_aparDao.findAll().iterator().hasNext()) {
	    		t_aparDao.save(new Tipo_apartamento("Casa"));
	    		t_aparDao.save(new Tipo_apartamento("Piso"));
	    		t_aparDao.save(new Tipo_apartamento("Chalet"));
	    		t_aparDao.save(new Tipo_apartamento("Solar"));
	    		t_aparDao.save(new Tipo_apartamento("Otro"));
	    	}
	    };
	}
}
