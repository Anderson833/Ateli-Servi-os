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
    
	
	  /**
	   * Método para lista todas despesas pelo id do usuário
	   * @param us
	   * @return
	   */
		@Query("SELECT x FROM Despesa x WHERE x.usuario = ?1")
	 	List<Despesa> listaDespesasPeloIdUsuario(@Param("usuario")Usuario us);
	
    /**
     * Método para soma todo total das despesas pelo id od usuário
     * @param usu
     * @return Total de todas despesas
     */
 	@Query("SELECT SUM(x.valorTotal) FROM Despesa x WHERE x.usuario =?1")
       Double somaDespesasPorIdUsuario(@Param("usuario")Usuario usu);
 	
 	/**
 	 * Método para lista as despesas por id usuário e descrição
 	 * @param us
 	 * @param descricao
 	 * @return Lista todas despesas conforme os dados passados nos parâmetros
 	 */
 	@Query("SELECT x FROM Despesa x WHERE x.usuario =?1 and x.descricao like %?2%")
 	List<Despesa> listaDespesasPeloIdUsuarioDescricao(@Param("usuario")Usuario us,@Param("descricao")String descricao);
    
 	/**
 	 * Método para soma as despesas por id do usuário e descrição
 	 * @param usu
 	 * @param descricao
 	 * @return Total das despesas conforme o dados passados nos parâmetros
 	 */
 	@Query("SELECT SUM(x.valorTotal) FROM Despesa x WHERE x.usuario =?1 and x.descricao like %?2%")
    Double somaDespesasPorIdUsuarioDescricao(@Param("usuario")Usuario usu,@Param("descricao")String descricao);
	
 	/**
 	 * Método para lista as despesas pelo id do usuário, entre as datas
 	 * @param us
 	 * @param datai
 	 * @param dataf
 	 * @return Lista dos dados das despesas
 	 */
	@Query("SELECT x FROM Despesa x WHERE x.usuario =?1 and x.data >=?2 and x.data <=?3")
 	List<Despesa> listaDespesasPeloIdUsuarioEntreDatas(@Param("usuario")Usuario us,@Param("data")String datai,@Param("data")String dataf);
    
	/**
	 * Método para soma as despesas por id do usuário e entre as datas
	 * @param usu
	 * @param datai
	 * @param dataf
	 * @return Total das despesas conforme os dados passados nos parametros
	 */
	@Query("SELECT SUM(x.valorTotal) FROM Despesa x WHERE x.usuario =?1 and x.data>=?2 and x.data<=?3")
    Double somaDespesasPorIdUsuarioEntreDatas(@Param("usuario")Usuario usu,@Param("data")String datai,@Param("data")String dataf);

 	/**
 	 * Método para lista as despesas por id do usuário, descrição e entre as datas
 	 * @param us
 	 * @param descricao
 	 * @param datai
 	 * @param dataf
 	 * @return Lista as despesas conforme os dados passados nos parâmetros
 	 */
 	@Query("SELECT x FROM Despesa x WHERE x.usuario =?1 and x.descricao like %?2% and x.data>=?3 and x.data<=?4")
 	List<Despesa> listaDespesasPeloIdUsuarioDescricaoDatas(@Param("usuario")Usuario us,@Param("descricao")String descricao,@Param("data")String datai,@Param("data")String dataf);
     
 	/**
 	 * Método para soma as despesas por id do usuário,descrição e entre as datas
 	 * @param usu
 	 * @param descricao
 	 * @param datai
 	 * @param dataf
 	 * @return Total das despesas conforme os dados passados nos parâmetros
 	 */
 	@Query("SELECT SUM(x.valorTotal) FROM Despesa x WHERE x.usuario =?1 and x.descricao like %?2% and x.data>=?3 and x.data<=?4")
    Double somaDespesasPorIdUsuarioDescricaoDatas(@Param("usuario")Usuario usu,@Param("descricao")String descricao,@Param("data")String datai,@Param("data")String dataf);

}
