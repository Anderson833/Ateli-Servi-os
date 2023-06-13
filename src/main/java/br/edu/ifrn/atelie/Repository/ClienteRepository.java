package br.edu.ifrn.atelie.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.edu.ifrn.atelie.Modelo.ClienteModel;
import br.edu.ifrn.atelie.Modelo.Servicos;
import br.edu.ifrn.atelie.Modelo.Usuario;
// Essa interface ira manipular os dados no banco de dados
@Repository
public interface ClienteRepository extends JpaRepository<ClienteModel, Integer>{

	// BUSCANDO POR NOME, TELEFONE,ENDERECO 
		@Query("select u from ClienteModel u where u.nome like %:nome%"+
				" and u.telefone like %:telefone% and u.endereco like %:endereco%")
		List<ClienteModel> findByNomeAndTelefoneAndEndereco(@Param("nome")String nome,
				                         @Param("telefone")String telefone,
				                         @Param("endereco")String endereco);
		
		
		/**
		 * Método para lista os clientes que foram registrados por cada usuário específico 
		 * @param us
		 * @param nome
		 * @return Os clientes de um usuário específico
		 */
		@Query("select v from ClienteModel v where v.usuario =?1 and v.nome like %?2%")
		 List<ClienteModel> findByNome(@Param("usuario")Usuario us,@Param("nome")String nome);
		
		// listando todos clientes pelo id do usuário
    	@Query("SELECT x FROM ClienteModel x WHERE x.usuario = ?1")
     	List<ClienteModel> listaClientesPeloIdUsuario(@Param("usuario")Usuario us);
    
    	/**
    	 * Método para Lista nome do cliente pelo id do usuário 
    	 * @param nomeCliente
    	 * @param idUsuario
    	 * @return Nome do cliente
    	 */
     	@Query("SELECT x FROM ClienteModel x WHERE x.nome =?1 and x.usuario =?2")
     	List<ClienteModel> listaClientePeloNome(@Param("nome")String nomeCliente,@Param("usuario")Usuario idUsuario);
     
}
