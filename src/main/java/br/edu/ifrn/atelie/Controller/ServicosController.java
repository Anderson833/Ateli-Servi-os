package br.edu.ifrn.atelie.Controller;

import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.edu.ifrn.atelie.Modelo.Calculor;
import br.edu.ifrn.atelie.Modelo.ClienteModel;
import br.edu.ifrn.atelie.Modelo.Servicos;
import br.edu.ifrn.atelie.Repository.CalculorRepository;
import br.edu.ifrn.atelie.Repository.ClienteRepository;
import br.edu.ifrn.atelie.Repository.ServicosRepository;

//Essa classe vai controlar as requisições feitas para serviços 

@Controller
@RequestMapping("/servicos")   // Url de inicio para unir com as específicas
public class ServicosController {
 
	 // realizando a instância da classe Servicos
	@Autowired
	private ServicosRepository  repositoryServico;
	
	@Autowired
	private ClienteRepository clienteRp;
	

	@Autowired
	private CalculorRepository repositoryCal;
	
	//método para abrir a página de serviços e passa os objetos de serviços
	@GetMapping("/atividades")
	public String tarefas(ModelMap model) {
		model.addAttribute("tarefas", new Servicos());
		return "view/tarefas";
	}
	
	//Método para adicionar os dados de serviços
	@GetMapping("/adicionar")
	 @PreAuthorize("hasAuthority('admin')")
	public String salvarServicos(Integer idserv,Servicos serv, ModelMap md) {
		// salvando no banco de dados
		repositoryServico.saveAndFlush(serv);
		//retonando para a página de cadastrar serviços
		return "redirect:/servicos/atividades";
	}
	
	// Método para editar servico
	@GetMapping("/editar/{id}")
	 @PreAuthorize("hasAuthority('admin')")
	public String editarServico(@PathVariable("id") Integer idServico, ModelMap md) {
		 // buscando pelo id do tipo de serviço 
		Optional<Servicos> findById = repositoryServico.findById(idServico);
		// deletando o tipo de serviço que tem esse tipo de id para adicionar outro novo
	     repositoryServico.deleteById(idServico);
	     // Passando os objetos para a página de serviços
		md.addAttribute("tarefas",findById);
		// retornando a ação para página de seviços
		return "view/tarefas";
	}
	
	//método para adicionar um serviço ou mais para os clientes
	@PostMapping("/adicionar")
	 @PreAuthorize("hasAuthority('admin')")
	public String adicionarServicos(Servicos serv, ModelMap md, RedirectAttributes att) {
		
		att.addFlashAttribute("msgsucesso","Operação Realizada Com Sucesso!");
		
		//já salvar no banco de dados
		repositoryServico.save(serv);
		
		return "redirect:/servicos/atividades";
	}
	
	//método para deleta todos os registro
	 @GetMapping("/deletaTudo")
	 @PreAuthorize("hasAuthority('admin')")
	public String deletaTodosServicos(Servicos sv) {
		 
	        repositoryServico.deleteAllInBatch();
		 
		return "view/ListaServicos";
	}
	
	//Método para deletar um serviços
	@GetMapping("/excluir/{id}")
	@Transactional(readOnly = false)
	 @PreAuthorize("hasAuthority('admin')")
	public String excluirServicos(@PathVariable("id") Integer id, Servicos sv,RedirectAttributes att) {
		repositoryServico.delete(sv);
		
		att.addFlashAttribute("msgError","Tipo de serviço excluído com Sucesso!");
		
		return "redirect:/servicos/listaTodos";
	}
	/*
	@GetMapping("/calculo")
	public String calculo(@RequestParam(name="qtd",required = false) double quantidade,
			@RequestParam(name="unit",required = false) double unitario,
			@RequestParam(name="total",required = false) double total,Calculor cal, ModelMap md) {
	//	double Qtd=Double.parseDouble(qtd);
	//	double unit=Double.parseDouble(unitario);
		
		
		cal.setQuantidade(quantidade);
		cal.setValorUnitario(unitario);
		double multiplicacao=cal.getQuantidade()*cal.getValorUnitario();
		cal.setValorTotal(multiplicacao);
		md.addAttribute("cal", new Calculor ());
		
		return "view/tarefas";
	}
	*/
		//Método para lista todos serviços
	@GetMapping("/listaTodos")
	//@Transactional(readOnly = true)
	public String listaServicos( ModelMap model) {
		  // buscando por todos registros de serviços
		List<Servicos> todosServicos = repositoryServico.findAll();
		// Passando todos os objetos para a página de lista de serviços
		model.addAttribute("mostraServicos",todosServicos);
		return "view/ListaServicos";
	}
	
	//método para gerar um auto complete dos nomes dos clientes
	@GetMapping("/autocompleteClientes")
	@Transactional(readOnly = true)
	@ResponseBody
	public List<br.edu.ifrn.atelie.DTO.AutocompleteDTO>  autocompleteVagas(
			@RequestParam("term")String termo){
		
		 List<ClienteModel> cliente =clienteRp.findByNome(termo);
		 
		 List<br.edu.ifrn.atelie.DTO.AutocompleteDTO> eficacia = new ArrayList<>();
		 cliente.forEach(v -> eficacia.add(new br.edu.ifrn.atelie.DTO.AutocompleteDTO(v.getNome(),v.getId())));
		 
		 return eficacia;
	}
}
