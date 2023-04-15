package br.edu.ifrn.atelie.Controller;

import java.text.DecimalFormat;
import java.util.List;

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

import br.edu.ifrn.atelie.Modelo.Despesa;
import br.edu.ifrn.atelie.Modelo.InvestimentoModel;
import br.edu.ifrn.atelie.Modelo.Usuario;
import br.edu.ifrn.atelie.Repository.InvestimentoRepository;
import br.edu.ifrn.atelie.Repository.UsuarioRepository;

// ESSE CLASSE VAI CONTROLA AS REQUISIÇÕES DOS INVESTIMENTOS;
@Controller
@RequestMapping("/Investimento")
public class InvestimentoController {

	@Autowired
	private InvestimentoRepository repository;
	@Autowired
	private UsuarioRepository repositoryUsuario;
	// método para abrir a página de investimento e passar os objetos 
	  @GetMapping("/investir")
	public String inicioInvestimento(ModelMap model,InvestimentoModel invest) {
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
			 invest.setUsuario(us);
			 System.out.println(" o ID do objeto "+invest.getUsuario());
		     model.addAttribute("invest", invest) ; 
		return "view/investimento";
	}
	  
	//Método para excluir algum investimento
		@GetMapping("/excluir/{id}")
		@Transactional(readOnly = false)
		 @PreAuthorize("hasAuthority('admin')")
		public String excluirInvestimento(@PathVariable("id") Integer id, InvestimentoModel invest,RedirectAttributes att) {
			repository.deleteById(id);
			
		
			att.addFlashAttribute("msgExcluirInvestimento","Investimento excluído com Sucesso!");
			
			return "redirect:/Investimento/lista";
		}
	  
	  @GetMapping("/lista")
	  public String listaInvestimento(ModelMap md, InvestimentoModel invest) {
		  
		  // Pegando email do usuário 
			 String email= Usuario.getEmailUsuario();
			  System.out.println(" aqui o email "+Usuario.getEmailUsuario());
			  // Pegando id do usuário pelo email informado no paramentro
			  int id = repositoryUsuario.BuscaIdPeloEmail(email);
				System.out.println("aqui  id do usuário é = "+id);
			
				// buscando todos dados do usuário pelo id informa no paramentro
			     Usuario us = repositoryUsuario.BuscaTodosDadosPeloId(id);
			 System.out.println("O objeto é esse  "+us.getId());
			
			 // Listando todos servicos pelo id do usuário  		 
			List<InvestimentoModel> todosInvestimentos = repository.listaInvestimentoPeloId(us);
		
		  
			// condição para saber se a tabela de investimento esta vazia
			if(todosInvestimentos.isEmpty()|| todosInvestimentos==null) {
				// Passando o resultado para decimal
				DecimalFormat decimal = new DecimalFormat("#,##0.00");
				double total=0;                   // exibindo o resultado
				md.addAttribute("TotalInvestimento", "R$ "+decimal.format(total));
				md.addAttribute("invest",repository.listaInvestimentoPeloId(us));
				// retornando para página de lista investimentos
				return  "view/ListaInvestimento";
			}else {
				//Pegando toda soma dos investimentos pelo id do usuário
				invest.setValorTotal(repository.soma(us)); 
				// Passando o resultado para decimal
				DecimalFormat decimal = new DecimalFormat("#,##0.00");
				String total =decimal.format(invest.getValorTotal());
				// Passando valor total para se exibida na página html
				md.addAttribute("TotalInvestimento", "R$ "+total);
				// Passando todos os objetos para a página de lista despesas
				md.addAttribute("invest",repository.listaInvestimentoPeloId(us));
				return "view/listaInvestimento";
			}

	  }
	  
	
	  @GetMapping("/calc")
	  public String calcular(ModelMap mdl) {
		  InvestimentoModel in = new InvestimentoModel();
	      double valorUnit=in.getValorUnitario();
	      double qtd=in.getQuantidade();
		  double valorInvestido=valorUnit*qtd;
		  double valorTotal=valorInvestido;
		   in.setValorTotal(valorTotal);
		// mdl.addAttribute("total",valorInvestido);
		  return "view/calcular";
	  }
	  
	  
	  @PostMapping("/inserir")
	  public String inserirInvestimento(InvestimentoModel invest,RedirectAttributes att) {
		   repository.save(invest);
		   att.addFlashAttribute("msgInvestimento","Investimento inserido com sucesso!");
		 //  return "view/investimento";
		  return "redirect:/Investimento/investir"; 
	  }
	  
	// Método para altera ou editar os dados 
			@GetMapping("/editar/{id}")
			 @PreAuthorize("hasAuthority('admin')")
			public String editarInvestimento(@PathVariable("id") int idinvest, ModelMap md) {
				
				// Passando os objetos para a página de investimento
				md.addAttribute("invest", repository.findById(idinvest));
				// deletando um investimento
				 repository.deleteById(idinvest);
				// retornando a ação para página de  investimento
				return "view/Investimento";
				
			}
}
