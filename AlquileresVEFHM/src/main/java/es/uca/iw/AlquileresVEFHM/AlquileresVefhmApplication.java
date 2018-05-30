package es.uca.iw.AlquileresVEFHM;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import es.uca.iw.AlquileresVEFHM.DAO.Metodo_pagoDAO;
import es.uca.iw.AlquileresVEFHM.DAO.RolDAO;
import es.uca.iw.AlquileresVEFHM.DAO.Tipo_apartamentoDAO;
import es.uca.iw.AlquileresVEFHM.modelos.Metodo_pago;
import es.uca.iw.AlquileresVEFHM.modelos.Rol;
import es.uca.iw.AlquileresVEFHM.modelos.Tipo_apartamento;
import es.uca.iw.AlquileresVEFHM.seguridad.VaadinSessionSecurityContextHolderStrategy;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class AlquileresVefhmApplication {	
	public static void main(String[] args) {
		SpringApplication.run(AlquileresVefhmApplication.class, args);
	}
	
	@Bean
	public CommandLineRunner loadData(RolDAO rolDao, Tipo_apartamentoDAO t_aparDao, Metodo_pagoDAO m_pagoDao) {
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
	    	if(!m_pagoDao.findAll().iterator().hasNext()) {
	    		m_pagoDao.save(new Metodo_pago("Tarjeta", 4));
	    		m_pagoDao.save(new Metodo_pago("Transferencia", 0));
	    		m_pagoDao.save(new Metodo_pago("PayPal", 10));
	    	}
	    };
	}
	
	@Configuration
	@EnableGlobalMethodSecurity(securedEnabled = true)
	public static class ConfiguracionSeguridad extends GlobalMethodSecurityConfiguration {
		@Autowired
		private UserDetailsService uds;
		
		@Bean
		public PasswordEncoder codificador() {
			return new BCryptPasswordEncoder(11);
		}
		
		@Bean
		public DaoAuthenticationProvider authenticationProvider() {
			DaoAuthenticationProvider aP = new DaoAuthenticationProvider();
			aP.setUserDetailsService(uds);
			aP.setPasswordEncoder(codificador());
			return aP;
		}
		
		@Override
		protected void configure(AuthenticationManagerBuilder auth) throws Exception {
			auth.authenticationProvider(authenticationProvider());
		}
		
		@Bean
		public AuthenticationManager authenticationManagerBean() throws Exception {
			return authenticationManager();
		}
		
		static { SecurityContextHolder.setStrategyName(VaadinSessionSecurityContextHolderStrategy.class.getName()); }
	}
}