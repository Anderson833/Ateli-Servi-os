package br.edu.ifrn.atelie.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.edu.ifrn.atelie.Modelo.Calculor;
import br.edu.ifrn.atelie.Repository.CalculorRepository;

@Controller
@RequestMapping("/servicos") 
public class CalculorController {
	
	@Autowired
	private CalculorRepository repositoryCal;
	
	@GetMapping("/servicos/atividades")
	public String objetoCalc(ModelMap model, Calculor cal){
		
		model.addAttribute("tarefas", new Calculor ());
		return"view/tarefas";
	}
	
	
	@GetMapping("/calc")
	public String calculo(@RequestParam(name="qtd",required = false) double quantidade,
			@RequestParam(name="valorUnit",required = false) double unitario,
			@RequestParam(name="valorTotal",required = false) double total,Calculor cal, ModelMap md) {
	//	double Qtd=Double.parseDouble(qtd);
	//	double unit=Double.parseDouble(unitario);
		
		Calculor c = new Calculor();
		c.setQuantidade(quantidade);
		c.setValorUnitario(unitario);
		c.setValorTotal(total);
		
		repositoryCal.save(cal);
	/*	cal.setQuantidade(quantidade);
		cal.setValorUnitario(unitario);
		double multiplicacao=cal.getQuantidade()*cal.getValorUnitario();
		cal.setValorTotal(multiplicacao);
		md.addAttribute("cal", new Calculor ());
		*/
		return "redirect:/servicos/atividades";
	}


	public String calcular(Calculor cal) {
		
		repositoryCal.save(cal);
		
		
		return "";
	}

}
