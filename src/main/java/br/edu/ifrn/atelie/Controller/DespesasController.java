package br.edu.ifrn.atelie.Controller;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.edu.ifrn.atelie.Modelo.ClienteModel;
import br.edu.ifrn.atelie.Modelo.Despesa;
import br.edu.ifrn.atelie.Modelo.Servicos;
import br.edu.ifrn.atelie.Modelo.Usuario;
import br.edu.ifrn.atelie.Repository.DespesaRepository;
import br.edu.ifrn.atelie.Repository.UsuarioRepository;

@Controller
@RequestMapping("/despesas")
public class DespesasController {
   
	@Autowired
	private DespesaRepository RepositoryDesp;
	
	@Autowired
	private UsuarioRepository repositoryUsuario;
	@GetMapping("/inicio")
	public String viewDespesa(ModelMap model,Despesa desp) {
		 // Pegando email do usuário 
		 String email= Usuario.getEmailUsuario();
		  System.out.println(" aqui o email "+Usuario.getEmailUsuario());
		  // Pegando id do usuário pelo email informado no paramentro
		  int id = repositoryUsuario.BuscaIdPeloEmail(email);
			System.out.println("aqui  id do usuário é = "+id);
		
			// buscando todos dados do usuário pelo id informa no paramentro
		     Usuario us = repositoryUsuario.BuscaTodosDadosPeloId(id);
		 System.out.println("O objeto é esse  "+us.getId());
		
		 // Passando o objeto us de usuário para salva nos serviços
		 desp.setUsuario(us);
		 System.out.println(" o ID do objeto "+desp.getUsuario());
		  //  Passando o objeto para exibir na página html de cadastro de servicos
			model.addAttribute("despesa", desp);
	    //model.addAttribute("despesa",new Despesa());
		return "view/despesa";
	}
	
	@PostMapping("/salvar")
	public String salvarDespesas(Despesa desp,RedirectAttributes rd) {
		
		RepositoryDesp.save(desp);
		 rd.addFlashAttribute("msgDespesa","Dados salvo com sucesso!");
		return "redirect:/despesas/inicio";
		
	}
	
	//Método para deletar uma despesa
	@GetMapping("/excluir/{id}")
	@Transactional(readOnly = false)
	 @PreAuthorize("hasAuthority('admin')")
	public String excluirDespesa(@PathVariable("id") Integer id, Despesa despesa,RedirectAttributes att) {
		RepositoryDesp.delete(despesa);
		
	
		att.addFlashAttribute("msgDeletaDespesa","Despesa excluída com Sucesso!");
		
		return "redirect:/despesas/lista";
	}
	@GetMapping("/lista")
	public String listaDespesas(ModelMap md,Despesa desp) {
		 // Pegando email do usuário 
		 String email= Usuario.getEmailUsuario();
		  System.out.println(" aqui o email "+Usuario.getEmailUsuario());
		  // Pegando id do usuário pelo email informado no paramentro
		  int id = repositoryUsuario.BuscaIdPeloEmail(email);
			System.out.println("aqui  id do usuário é = "+id);
		
			// buscando todos dados do usuário pelo id informa no paramentro
		     Usuario us = repositoryUsuario.BuscaTodosDadosPeloId(id);
		 System.out.println("O objeto é esse  "+us.getId());
		
		 // Listando todas despesas pelo id do usuário  		 
		List<Despesa> todasDespesas = RepositoryDesp.listaDespesasPeloId(us);
	
		  
		// condição para saber se a tabela de despesa esta vazia
		if(todasDespesas.isEmpty()|| todasDespesas==null) {
			// Passando o resultado para decimal
			DecimalFormat decimal = new DecimalFormat("#,##0.00");
			double total=0;                   // exibindo o resultado
			md.addAttribute("TotalDespesas", "R$ "+decimal.format(total));
			md.addAttribute("despesa",RepositoryDesp.listaDespesasPeloId(us));
			// retornando para página de lista despesas
			return  "view/ListaDespesas";
		}else {
			//Pegando toda soma das despesas pelo id do usuário
			desp.setValorTotal(RepositoryDesp.soma(us)); // somando a qtd
			// Passando o resultado para decimal
			DecimalFormat decimal = new DecimalFormat("#,##0.00");
			String total =decimal.format(desp.getValorTotal());
			// Passando valor total para se exibida na página html
			md.addAttribute("TotalDespesas", "R$ "+total);
			// Passando todos os objetos para a página de lista despesas
			md.addAttribute("despesa",RepositoryDesp.listaDespesasPeloId(us));
			return "view/listaDespesas";
		}
		 	
	}
	
	// Método para editar despesa
		@GetMapping("/editar/{id}")
		 @PreAuthorize("hasAuthority('admin')")
		public String editarDespesa(@PathVariable("id") int idDespesa, ModelMap md,RedirectAttributes att) {
			 // buscando pelo id da despesa 
			//Optional<Servicos> findById = RepositoryDesp.findById(idDespesa);
			// deletando o tipo de serviço que tem esse tipo de id para adicionar outro novo
		     // Passando os objetos para a página de serviços
			md.addAttribute("despesa", RepositoryDesp.findById(idDespesa));
			 RepositoryDesp.deleteById(idDespesa);
			// retornando a ação para página de seviços
			return "view/despesa";
		}
}
