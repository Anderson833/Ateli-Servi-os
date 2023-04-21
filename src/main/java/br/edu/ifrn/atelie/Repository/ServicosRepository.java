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
   
	    // somando todo total dos serviços
     	@Query("SELECT SUM(x.valorTotal) FROM Servicos x WHERE x.usuario =?1")
           Double soma(@Param("usuario")Usuario usu);
     	 // contando toda quantidade de servicos registrados
     	@Query("SELECT COUNT(x) FROM Servicos x")
           Double conta();
     	
     	// lista todos serviços pelo nome do cliente
     	@Query("SELECT x FROM Servicos x WHERE x.nome like %?1%")
     	List<Servicos> buscaServicos(@Param("nome")String nome);
     	
        /**
         * Método para somar os serviços pelo id do usuário e nome do cliente
         * @param usuario
         * @param nome
         * @return O total de toda coluna valorTotal no banco de dados 
         */
     	@Query("SELECT SUM(x.valorTotal) FROM Servicos x WHERE x.usuario =?1 and x.nome like %?2%")
     	Double somandoPeloNomeDoCliente(@Param("usuario")Usuario usuario,@Param("nome")String nome);
     	
     	
     // lista todos serviços pela datas de inicio e final
     /*	@Query("SELECT x FROM Servicos x WHERE x.data > ?1 and x.data <?2")
     	List<Servicos> listaServicosPelasDatas(@Param("dataTermino")String dataI,@Param("dataTermino")String data);
     	*/
     // lista todos serviços pela datas de inicio e final
     	@Query("SELECT x FROM Servicos x WHERE x.data >= ?1 and x.data <=?2 and x.usuario = ?3")
     	List<Servicos> listaServicosPelasDatasIdUsuario(@Param("dataTermino")String dataI,@Param("dataTermino")String data,
     			@Param("usuario")Usuario us);
     	
     	
     	 // Soma o total de todos  serviços pela datas de inicio e final e id do usuário
     	@Query("SELECT SUM(x.valorTotal) FROM Servicos x WHERE x.data >= ?1 and x.data <=?2 and x.usuario = ?3")
     	Double  somamdoPorDatasIdUsuario(@Param("dataTermino")String dataI,@Param("dataTermino")String dataf,
     			@Param("usuario")Usuario us);
     	
     	 // Soma o total de todos  serviços pela datas de inicio e final e id do usuário
     /*	@Query("SELECT SUM(x.valorTotal) FROM Servicos x WHERE x.dataTermino > ?1 and x.dataTermino <?2")
     	Double  somamdoPorDatas(@Param("dataTermino")String dataI,@Param("dataTermino")String dataf);
     			*/
     			
     	
     	// listando todos servicos pelo id do usuário
    	@Query("SELECT x FROM Servicos x WHERE x.usuario = ?1")
     	List<Servicos> listaServicosPeloId(@Param("usuario")Usuario us);
    	
    	// Deletar todos servicos pelo id do usuário
    	@Query("DELETE FROM Servicos o WHERE o.usuario = ?1")
     	int deletaPeloIdUsuario(@Param("usuario")Usuario us);
    
}
