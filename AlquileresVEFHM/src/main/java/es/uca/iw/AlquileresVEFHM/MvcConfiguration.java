package es.uca.iw.AlquileresVEFHM;
/*
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
 
@Configuration
@EnableWebMvc
@ComponentScan
public class MvcConfiguration implements WebMvcConfigurer
{
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		return bCryptPasswordEncoder;
	}
	
    /*@Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/vistas/");
        resolver.setSuffix(".jsp");
        resolver.setViewClass(JstlView.class);
        registry.viewResolver(resolver);
    }*/
    /*@Autowired
    private ApplicationContext applicationContext;
    @Bean
    public SpringResourceTemplateResolver templateResolver() {
       SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
       templateResolver.setApplicationContext(applicationContext);
       templateResolver.setPrefix("/WEB-INF/vistas/");
       templateResolver.setSuffix(".html");
       return templateResolver;
    }*/
    /*@Bean
    public ClassLoaderTemplateResolver templateResolver() {
        
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        
        templateResolver.setPrefix("vistas/");
        templateResolver.setCacheable(false);
        templateResolver.setSuffix(".html");        
        templateResolver.setTemplateMode("HTML5");
        templateResolver.setCharacterEncoding("UTF-8");
        
        return templateResolver;
    }
    @Bean
    public ThymeleafViewResolver thymeleafViewResolver() {
        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
        resolver.setTemplateEngine(templateEngine());
        resolver.setCharacterEncoding("UTF-8");
        return resolver;
    }
    @Bean
    public SpringTemplateEngine templateEngine() {
       SpringTemplateEngine templateEngine = new SpringTemplateEngine();
       templateEngine.setTemplateResolver(templateResolver());
       templateEngine.setEnableSpringELCompiler(true);
       return templateEngine;
    }
    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
       ThymeleafViewResolver resolver = new ThymeleafViewResolver();
       resolver.setTemplateEngine(templateEngine());
       resolver.setCharacterEncoding("UTF-8");
       registry.viewResolver(resolver);
    }
}
*/