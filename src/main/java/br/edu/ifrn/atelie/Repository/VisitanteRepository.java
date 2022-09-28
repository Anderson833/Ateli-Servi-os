package br.edu.ifrn.atelie.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.edu.ifrn.atelie.Modelo.Visitante;

public interface VisitanteRepository extends JpaRepository<Visitante, Integer> {

	// realizando o comando para buscar pelo email na tabela de visitante
			@Query("select u from Visitante u where  u.Email like %:Email%")
			 // Passando para o método findByEmail  como parâmento o email 
			Optional<Visitante> findByEmail(@Param("Email")String email);
}
