package br.edu.ifrn.atelie.Repository;

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
}
