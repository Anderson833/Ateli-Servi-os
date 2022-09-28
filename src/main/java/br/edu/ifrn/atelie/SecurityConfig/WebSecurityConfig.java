package br.edu.ifrn.atelie.SecurityConfig;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import br.edu.ifrn.atelie.Modelo.Usuario;
import br.edu.ifrn.atelie.Modelo.Visitante;
import br.edu.ifrn.atelie.Service.UsuarioService;
import br.edu.ifrn.atelie.Service.VisitanteService;

@SuppressWarnings("deprecation")
@EnableGlobalMethodSecurity(prePostEnabled =true )
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{
       
	
	

	@Autowired
	 private UsuarioService serviceUsuario;
	
	@Autowired
	 private VisitanteService visitanteSc;
	
	 @Override
	protected void configure(HttpSecurity http) throws Exception {
			// http.authorizeRequests().anyRequest().permitAll();
		
			http.authorizeRequests()
			.antMatchers("/opcaoCadastro","/usuario","/visitante","/adicionar","/Salvar").permitAll() //as urls que tem acessor sem autenticação
		
			
	// configuração das url para os usuários em comum
	//	.antMatchers("/").hasAuthority(Visitante.Usuariocomun)
			
			//configuração das url para quem tem o perfil de admin 
	//	.antMatchers("/").hasAuthority(Usuario.admin)
			
    .anyRequest().authenticated() 
			//Apenas Permiter o acesso as demais urls só depois que tive feito o login  
			.and()  
		.formLogin()
			.loginPage("/login")  
			.defaultSuccessUrl("/",true)
			.failureUrl("/login-Error")  // caso de error ao fazer login o  sistema retorna para essa página de error
			.permitAll()
			.and()
		.logout()
		.logoutSuccessUrl("/") //url para caso os dados do login estejão corretos
		.and()
		.rememberMe(); // isso serve com objetivo de capturar os dados de login salvo ao ter acessado a primeira vez
		
	}
	 
	 @Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		//Método para fazer a buscar dos dados do usuario como email e senha na classe UsuarioService
		auth.userDetailsService(serviceUsuario).passwordEncoder(new BCryptPasswordEncoder());
		
		//Método para fazer a buscar dos dados do visitante como email e senha na classe VisitanteService
				auth.userDetailsService(visitanteSc).passwordEncoder(new BCryptPasswordEncoder());
	}
}
