package br.edu.ifrn.atelie.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.edu.ifrn.atelie.Modelo.Despesa;
import br.edu.ifrn.atelie.Modelo.InvestimentoModel;
import br.edu.ifrn.atelie.Modelo.Usuario;

@Repository
public interface InvestimentoRepository extends JpaRepository<InvestimentoModel, Integer>{

 	 /**
 	  * Método para  somar  todo total dos investimentos pelo id od usuário
 	  * @param usu
 	  * @return Total de todo investimento
 	  */
 	@Query("SELECT SUM(x.valorTotal) FROM InvestimentoModel x WHERE x.usuario =?1")
       Double soma(@Param("usuario")Usuario usu);
 	
	/**
	 * Método para lista todo investimento pelo id do usuário
	 * @param us
	 * @return Uma lista de dados dos investimentos salvo no banco de dados
	 */
	@Query("SELECT x FROM InvestimentoModel x WHERE x.usuario = ?1")
 	List<InvestimentoModel> listaInvestimentoPeloId(@Param("usuario")Usuario us);
	
	 /**
	  * Método somando todo total dos investimentos pelo id do usuário e nome do produto
	  * @param usu
	  * @param Produto
	  * @return Total dos investimentos conforme o nome do produto passado nos parâmetro
	  */
	@Query("SELECT SUM(x.valorTotal) FROM InvestimentoModel x WHERE x.usuario =?1 and x.nomeProduto like %?2%")
       Double somaPorIdUsuarioNomeProduto(@Param("usuario")Usuario usu,@Param("nomeProduto")String Produto);
 	
	/**
	 * Método listando todo investimento pelo id do usuário e nome do produto
	 * @param us
	 * @param produto
	 * @return Uma lista de dados do investimento
	 */
	@Query("SELECT x FROM InvestimentoModel x WHERE x.usuario = ?1 and x.nomeProduto like %?2%")
 	List<InvestimentoModel> listaInvestimentoPeloIdUsuarioNomeProduto(@Param("usuario")Usuario us,@Param("nomeProduto")String produto);
	
	/**
	 * Método para lista os investimentos por id do usuário e entre as datas
	 * @param us
	 * @param datai
	 * @param dataf
	 * @return Todos dados pelo id do usuário e as datas passadas nos parâmetros
	 */
	@Query("SELECT x FROM InvestimentoModel x WHERE x.usuario = ?1 and x.data>=?2 and x.data<=?3")
 	List<InvestimentoModel> listaInvestimentoPeloIdUsuarioDatas(@Param("usuario")Usuario us,@Param("data")String datai,@Param("data")String dataf);
	
	/**
	 * Método para soma os investimentos pelo id do usuário e entre as datas
	 * @param us
	 * @param datai
	 * @param dataf
	 * @return Total dos investimentos conforme o id do usuário e as datas passadas nos parâmetros
	 */
	@Query("SELECT SUM(x.valorTotal) FROM InvestimentoModel x WHERE x.usuario = ?1 and x.data>=?2 and x.data<=?3")
 	Double somaInvestimentoPeloIdUsuarioDatas(@Param("usuario")Usuario us,@Param("data")String datai,@Param("data")String dataf);
	
	    /**
	     * Método para lista os investimentos, por id do usuário,nome do produto e entre as datas
	     * @param us
	     * @param produto
	     * @param datai
	     * @param dataf
	     * @return Todos os dados de investimentos comforme os dados passados nos parâmetros
	     */
		@Query("SELECT x FROM InvestimentoModel x WHERE x.usuario = ?1 and x.nomeProduto like %?2% and x.data>=?3 and x.data<=?4")
	 	List<InvestimentoModel> listaInvestimentoPeloIdUsuarioProdutoDatas(@Param("usuario")Usuario us,@Param("Produto")String produto,@Param("data")String datai,@Param("data")String dataf);
		
		/**
		 * Método para soma todos investimentos por id do usuário, nome do produto e entre as datas
		 * @param us
		 * @param produto
		 * @param datai
		 * @param dataf
		 * @return Total dos investimentos conforme os dados passados nos parâmentos
		 */
		@Query("SELECT SUM(x.valorTotal) FROM InvestimentoModel x WHERE x.usuario = ?1 and x.nomeProduto like %?2% and x.data>=?3 and x.data<=?4")
	 	Double  somaInvestimentoPeloIdUsuarioProdutoDatas(@Param("usuario")Usuario us,@Param("Produto")String produto,@Param("data")String datai,@Param("data")String dataf);
		
}
