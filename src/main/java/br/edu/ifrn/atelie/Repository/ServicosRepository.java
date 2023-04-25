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
     	 * Método para lista os servicos pelo id do usuário e descrição
     	 * @param usuario
     	 * @param descricao
     	 * @return Todos dados conforme  id, descrição passados nos parâmetros
     	 */
     	@Query("SELECT x FROM Servicos x WHERE x.usuario =?1 and x.descricao like %?2%")
     	List<Servicos> listaServicosPeloIdUsuarioDescricao(@Param("usuario")Usuario usuario,@Param("descricao")String descricao);
     	
     	/**
     	 * Método para soma todos servicos pelo id do usuário e descrição 
     	 * @param usuario
     	 * @param descricao
     	 * @return O total de toda descrição
     	 */
     	@Query("SELECT SUM(x.valorTotal) FROM Servicos x WHERE x.usuario =?1 and x.descricao like %?2%")
     	Double somaServicosPeloIdUsuarioDescricao(@Param("usuario")Usuario usuario,@Param("descricao")String descricao);
     	
    	/**
    	 * Método para lista os serviços pelo id do usuário,descrição e nome do cliente
    	 * @param usuario
    	 * @param descricao
    	 * @param nome
    	 * @return Todos dados conforme o id do usuário, descrição e nome do cliente passados nos parâmetro
    	 */
     	@Query("SELECT x FROM Servicos x WHERE x.usuario =?1 and x.descricao like %?2% and x.nome like %?3%")
     	List<Servicos> listaServicosPeloIdUsuarioDescricaoCliente(@Param("usuario")Usuario usuario,@Param("descricao")String descricao,@Param("nome")String nome);
     	
     	/**
     	 * Método para soma os serviços pelo id do usuário,descrição e nome do cliente
     	 * @param usuario
     	 * @param descricao
     	 * @param nome
     	 * @return O total dos serviços conforme os dados passados nos parâmetros
     	 */
     	@Query("SELECT SUM(x.valorTotal) FROM Servicos x WHERE x.usuario =?1 and x.descricao like %?2% and x.nome like %?3%")
     	Double somaServicosPeloIdUsuarioDescricaoCliente(@Param("usuario")Usuario usuario,@Param("descricao")String descricao,@Param("nome")String nome);
     	
     	/**
     	 * Método para lista os serviços pelo id usuário,descricao, nome do cliente e entre as datas
     	 * @param usuario
     	 * @param descricao
     	 * @param nome
     	 * @param dataInicio
     	 * @param datafinal
     	 * @return Todos dados conforme os dados passados nos parâmetros
     	 */
     	@Query("SELECT x FROM Servicos x WHERE x.usuario =?1 and x.descricao like %?2% and x.nome like %?3% and x.data >=?4 and x.data<=?5")
     	List<Servicos> listaServicosPeloIdUsuarioDescricaoClienteDatas(@Param("usuario")Usuario usuario,@Param("descricao")String descricao,@Param("nome")String nome,
     			@Param("dataInicio")String dataInicio,@Param("datafinal")String datafinal);
     	
     	/**
     	 * Método para soma todos serviços por id do usuário,descrição,cliente e entre as datas
     	 * @param usuario
     	 * @param descricao
     	 * @param nome
     	 * @param dataInicio
     	 * @param datafinal
     	 * @return Total dos serviços conforme os dados passados nos parâmetros
     	 */
    	@Query("SELECT SUM(x.valorTotal) FROM Servicos x WHERE x.usuario =?1 and x.descricao like %?2% and x.nome like %?3% and x.data >=?4 and x.data<=?5")
     	Double somaServicosPeloIdUsuarioDescricaoClienteDatas(@Param("usuario")Usuario usuario,@Param("descricao")String descricao,@Param("nome")String nome,
     			@Param("dataInicio")String dataInicio,@Param("datafinal")String datafinal);
     	
     	
       /**
        * Método para lista todos serviços pelo id do usuário, nome do cliente
        * @param nome
        * @param usuario
        * @return Todos dados conforme os dados que foram passados nos parâmetros
        */
     	@Query("SELECT x FROM Servicos x WHERE x.usuario =?1 and x.nome like %?2%")
     	List<Servicos> listaServicosPeloIdUsuarioNomeCliente(@Param("usuario")Usuario usuario,@Param("nome")String nome);
     	
        /**
         * Método para somar os serviços pelo id do usuário e nome do cliente
         * @param usuario
         * @param nome
         * @return O total de toda coluna valorTotal no banco de dados 
         */
     	@Query("SELECT SUM(x.valorTotal) FROM Servicos x WHERE x.usuario =?1 and x.nome like %?2%")
     	Double somandoServicosPeloIdUsuarioNomeDoCliente(@Param("usuario")Usuario usuario,@Param("nome")String nome);
     	
     	
     	/**
     	 * Método para Lista todos serviços com id do usuário, nome do cliente e entre as datas
     	 * @param us
     	 * @param nome
     	 * @param dataI
     	 * @param dataF
     	 * @return Uma lista de registros
     	 */
    	@Query("SELECT x FROM Servicos x WHERE x.usuario =?1 and x.nome like %?2% and x.data >= ?3 and x.data <=?4")
     	List<Servicos> listaServicosPeloIdUsuarioNomeClienteDatas(@Param("usuario")Usuario us,@Param("nome")String nome,@Param("data")String dataI,@Param("data")String dataF);
     	
     	
     /**
      * Método para soma todos serviços pelo id do usuário, nome do cliente e entre as datas
      * @param us
      * @param nome
      * @param dataI
      * @param dataF
      * @return A soma de toda coluna valorTotal 
      */
     	@Query("SELECT SUM(x.valorTotal) FROM Servicos x WHERE x.usuario =?1 and x.nome like %?2% and x.data >= ?3 and x.data <=?4")
     	Double SomaServicosPeloIdUsuarioNomeClienteDatas(@Param("usuario")Usuario us,@Param("nome")String nome,@Param("data")String dataI,@Param("data")String dataF);
     	

        /**
         * Método para lista todos serviços entre as datas e id do usuário
         * @param dataI
         * @param data
         * @param us
         * @return Todos os dados conforme os dados passado nos parametros
         */
     	@Query("SELECT x FROM Servicos x WHERE x.data >= ?1 and x.data <=?2 and x.usuario = ?3")
     	List<Servicos> listaServicosPelasDatasIdUsuario(@Param("data")String dataI,@Param("data")String data,
     			@Param("usuario")Usuario us);
     	
     	
     	 /**
     	  * Método para soma o total de todos  serviços pela datas de inicio e final e id do usuário
     	  * @param dataI
     	  * @param dataf
     	  * @param us
     	  * @return Total de todos serviços conforme os dados passados nos parâmetros
     	  */
     	@Query("SELECT SUM(x.valorTotal) FROM Servicos x WHERE x.data >= ?1 and x.data <=?2 and x.usuario = ?3")
     	Double  somamdoPorDatasIdUsuario(@Param("data")String dataI,@Param("data")String dataf,
     			@Param("usuario")Usuario us);
     			
     	/**
     	 * Método para lista todos serviços pelo id do usuário 
     	 * @param us
     	 * @return Todos os dados dos serviços
     	 */
    	@Query("SELECT x FROM Servicos x WHERE x.usuario = ?1")
     	List<Servicos> listaServicosPeloId(@Param("usuario")Usuario us);
    	
    	// Deletar todos servicos pelo id do usuário
    	@Query("DELETE FROM Servicos o WHERE o.usuario = ?1")
     	int deletaPeloIdUsuario(@Param("usuario")Usuario us);
    
}
