package br.edu.ifrn.atelie.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.edu.ifrn.atelie.Modelo.Despesa;
import br.edu.ifrn.atelie.Modelo.Servicos;
import br.edu.ifrn.atelie.Modelo.Usuario;

@Repository
public interface DespesaRepository extends JpaRepository<Despesa,Integer> {
  
	  // somando toda quantidade 
 	@Query("SELECT SUM(valorTotal) FROM Despesa")
       Double soma();
    // somando todo total das despesas pelo id od usuário
 	@Query("SELECT SUM(x.valorTotal) FROM Despesa x WHERE x.usuario =?1")
       Double soma(@Param("usuario")Usuario usu);
 	 /* // somando toda quantidade 
 	@Query("SELECT SUM(x.valorTotal) FROM Servicos x WHERE x.usuario =?1")
       Double somaPeloId(@Param("usuario")Usuario usu);*/
 	
	// listando todas despesas pelo id do usuário
	@Query("SELECT x FROM Despesa x WHERE x.usuario = ?1")
 	List<Despesa> listaDespesasPeloId(@Param("usuario")Usuario us);
}
