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

	 // somando todo total 
 	@Query("SELECT SUM(valorTotal) FROM InvestimentoModel")
       Double soma();
 	
 	 // somando todo total dos investimentos pelo id od usuário
 	@Query("SELECT SUM(x.valorTotal) FROM InvestimentoModel x WHERE x.usuario =?1")
       Double soma(@Param("usuario")Usuario usu);
 	
	// listando todo investimento pelo id do usuário
	@Query("SELECT x FROM InvestimentoModel x WHERE x.usuario = ?1")
 	List<InvestimentoModel> listaInvestimentoPeloId(@Param("usuario")Usuario us);
}
