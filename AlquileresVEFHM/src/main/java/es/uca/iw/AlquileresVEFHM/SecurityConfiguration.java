package es.uca.iw.AlquileresVEFHM;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private DataSource dataSource;
	private String ConsultaUsuario = "select login, clave, activo from usuario where login=?";
	private String ConsultaRol = "select usuario.login, rol.nombre from usuario inner join rol on(usuario.rol=rol._id) where usuario.login=?";

	@Override
	protected void configure(AuthenticationManagerBuilder auth)
			throws Exception {
		auth.jdbcAuthentication()
				.usersByUsernameQuery(ConsultaUsuario)
				.authoritiesByUsernameQuery(ConsultaRol)
				.dataSource(dataSource)
				.passwordEncoder(bCryptPasswordEncoder);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.authorizeRequests()
				.antMatchers("/", "/login", "/registro", "/fotos/**").permitAll()
				//.antMatchers("/usuario").hasAuthority("Huesped")
				.antMatchers("/apartamento/registro", "/apartamento/ver").hasAuthority("Anfitrion")
				.antMatchers("/usuario").hasAnyAuthority("Huesped","Anfitrion")
				.anyRequest().authenticated()
				.and().csrf().disable().formLogin()
				.loginPage("/login").failureUrl("/login?error=true")
				.defaultSuccessUrl("/")
				.usernameParameter("login")
				.passwordParameter("clave")
				.and().logout()
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
				.logoutSuccessUrl("/").and().exceptionHandling()
				.accessDeniedPage("/acceso-denegado");
	}
	
	@Override
	public void configure(WebSecurity web) throws Exception {
	    web.ignoring().antMatchers();
	}
}
