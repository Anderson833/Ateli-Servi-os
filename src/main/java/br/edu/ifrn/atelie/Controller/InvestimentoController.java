package br.edu.ifrn.atelie.Controller;

import java.util.jar.Attributes.Name;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import br.edu.ifrn.atelie.Modelo.InvestimentoModel;
import br.edu.ifrn.atelie.Repository.InvestimentoRepository;

// ESSE CLASSE VAI CONTROLA AS REQUISIÇÕES DOS INVESTIMENTOS;
@Controller
@RequestMapping("/Investimento")
public class InvestimentoController {

	@Autowired
	private InvestimentoRepository repository;
	
	// método para abrir a página de investimento e passar os objetos 
	  @GetMapping("/investir")
	public String inicioInvestimento(ModelMap model) {
		model.addAttribute("invest", new InvestimentoModel()) ; 
		return "view/investimento";
	}
	  @GetMapping("/c")
	  public String inicio() {
		  return "view/calcular";
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
	  
	  @PostMapping("/adicionar")
	  public String adicionar(InvestimentoModel invest) {
		  double valorUnit=invest.getValorUnitario();
	      double qtd=invest.getQuantidade();
		  double valorInvestido=valorUnit*qtd;
		  double valorTotal=valorInvestido;
		   invest.setValorTotal(valorTotal);
		 // repository.save(invest);
		   return "view/investimento";
		 // return "redirect:investir";
	  }
}
