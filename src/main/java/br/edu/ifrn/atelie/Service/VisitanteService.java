package br.edu.ifrn.atelie.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.edu.ifrn.atelie.Modelo.Visitante;
import br.edu.ifrn.atelie.Repository.VisitanteRepository;
@Service
public class VisitanteService implements UserDetailsService {
   
	@Autowired
    private VisitanteRepository visitanteRp;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		// passando os dados do VisitanteRepository para objeto Visitante
		Visitante visitante = visitanteRp.findByEmail(username)
				                       // método para caso não encontre os dados do usuário
				 .orElseThrow(()-> new UsernameNotFoundException("Visitante não encontrado"));
		return new User(
				 //conseguindo os dados do visitante como email e senha
				visitante.getEmail(),
				visitante.getSenha(),
			        AuthorityUtils.createAuthorityList(visitante.getVisitantePerfil())
				
				);
	}

}
