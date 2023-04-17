package br.edu.ifrn.atelie.Repository;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.edu.ifrn.atelie.Modelo.Usuario;


@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer>{

	// realizando o comando para buscar pelo email na tabela de Usuario
		@Query("select u from Usuario u where  u.Email like %:Email%")
		 // Passando para o método findByEmail  como parâmento o email 
		Optional<Usuario> findByEmail(@Param("Email")String email);
		
		// realizando o comando para buscar todos email na tabela de Usuario
				@Query("select u.Email from Usuario u where u.Email = ?1")
				 // Passando para o método findByEmail  como parâmento o email 
				String buscaTodosEmail(@Param("Email")String email);
				
		
		// realizando o comando para buscar todos dados do usuário pelo id na tabela de Usuario
		@Query("select u from Usuario u where  u.id = ?1") 
		  Usuario BuscaTodosDadosDoUsuarioPeloId(@Param("id")int id);
		
	
		// realizando o comando para buscar id pelo email senha na tabela de Usuario
				@Query("select u.id from Usuario u where  u.Email = ?1")
				 // Passando para o método buscarIdDoUsuario para pegar o id do usuário 
				int BuscaIdPeloEmail(@Param("Email")String email);
		
		
				// realizando o comando para buscar a senha pelo email na tabela de Usuario
				@Query("select u.senha from Usuario u where  u.Email = ?1")
				 // Passando para o método buscar senha pelo email do usuário 
				String BuscaSenhaPeloEmail(@Param("Email")String email);
		
				// realizando o comando para buscar a senha pelo email e id na tabela de Usuario
				@Query("select x.senha from Usuario x where  x.Email = ?1 and x.id =?2")
				 // Passando para o método buscar senha pelo email do usuário 
				String BuscaSenhaPeloEmaileId(@Param("Email")String email,
					                        	@Param("id")String id);
}
