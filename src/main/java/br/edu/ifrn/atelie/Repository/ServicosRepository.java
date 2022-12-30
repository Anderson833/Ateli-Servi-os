package br.edu.ifrn.atelie.Repository;



import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.edu.ifrn.atelie.Modelo.Servicos;
import br.edu.ifrn.atelie.Modelo.Usuario;

@Repository
public interface ServicosRepository extends JpaRepository<Servicos, Integer>{
   
	    // somando toda quantidade 
     	@Query("SELECT SUM(x.Quantidade) FROM Servicos x")
           Double soma();
     	 // contando toda quantidade 
     	@Query("SELECT COUNT(x) FROM Servicos x")
           Double conta();
     	
     	@Query("SELECT x FROM Servicos x WHERE x.nome like %?1%")
     	List<Servicos> buscaServicos(@Param("nome")String nome);
     	
     	// listando todos servicos pelo id do usuário
    	@Query("SELECT x FROM Servicos x WHERE x.usuario = ?1")
     	List<Servicos> listaServicosPeloId(@Param("usuario")Usuario us);
    	
    	// Deletar todos servicos pelo id do usuário
    	@Query("DELETE FROM Servicos o WHERE o.usuario = ?1")
     	int deletaPeloIdUsuario(@Param("usuario")Usuario us);
    
}
