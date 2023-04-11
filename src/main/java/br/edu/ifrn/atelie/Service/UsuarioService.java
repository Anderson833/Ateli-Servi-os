package br.edu.ifrn.atelie.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.edu.ifrn.atelie.Modelo.Calculor;
import br.edu.ifrn.atelie.Modelo.Usuario;
import br.edu.ifrn.atelie.Repository.UsuarioRepository;

@Service
public class UsuarioService implements UserDetailsService {

	@Autowired
	private UsuarioRepository repository;
	
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// Pegando  email do usuário para depois busca pelo id no controller de inicio         
	    // Usuario.listaEmail.add(username);
	     Usuario.setEmailUsuario(username);
		// passando os dados do UsuarioRepository para objeto usuario 
				Usuario usuario = repository.findByEmail(username)
				 
					  // método para caso não encontre os dados do usuário
						 .orElseThrow(()-> new UsernameNotFoundException("Usuário não encontrado"));
				return new User(
						 //conseguindo os dados do usuario como email e senha
						usuario.getEmail(),
						usuario.getSenha(),
					        AuthorityUtils.createAuthorityList(usuario.getPerfilUsuario())
					      
						);
		
	}
    
}
