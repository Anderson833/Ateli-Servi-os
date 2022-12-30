package br.edu.ifrn.atelie.Controller;

import java.text.DecimalFormat;
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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.edu.ifrn.atelie.Modelo.Calculor;
import br.edu.ifrn.atelie.Modelo.ClienteModel;
import br.edu.ifrn.atelie.Modelo.Servicos;
import br.edu.ifrn.atelie.Modelo.Usuario;
import br.edu.ifrn.atelie.Repository.CalculorRepository;
import br.edu.ifrn.atelie.Repository.ClienteRepository;
import br.edu.ifrn.atelie.Repository.ServicosRepository;
import br.edu.ifrn.atelie.Repository.UsuarioRepository;

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
	
	@Autowired
	private UsuarioRepository repository;
	
	//método para abrir a página de serviços e passa os objetos de serviços
	@GetMapping("/atividades")
	public String tarefas(ModelMap model,Servicos serv) {
	
      // Pegando email do usuário 
	 String email= Usuario.getEmailUsuario();
	  System.out.println(" aqui o email "+Usuario.getEmailUsuario());
	  
	  // Pegando id do usuário pelo email informado no paramentro
	  int id = repository.BuscaIdPeloEmail(email);
		System.out.println("aqui  id do usuário é = "+id);
		
		// buscando todos dados do usuário pelo id informa no paramentro
	     Usuario us = repository.BuscaTodosDadosPeloId(id);
	 System.out.println("O objeto é esse  "+us.getId());
	 
	  
	 // Passando o objeto us de usuário para salva nos serviços
	 serv.setUsuario(us);
	 System.out.println(" o ID do objeto "+serv.getUsuario());
	  //  Passando o objeto para exibir na página html de cadastro de servicos
		model.addAttribute("tarefas", serv);
		
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
	
	/*@GetMapping("/calculo")
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
	}*/
	
	  // Aqui esta para mostra a contagem dos servicos e a soma de todos servicos
/*	@GetMapping("/conta")
	public String mostraQtdServicos(ModelMap model, Servicos sv) {
		// repositoryServico.conta();
	  	sv.setValorTotal(repositoryServico.soma()); // somando a qtd
		sv.setQuantidade(repositoryServico.conta()); // contando toda quantidade
		sv.setNome("");
		model.addAttribute("msgListaTotal",sv.getValorTotal());
		model.addAttribute("mostraServicos",sv );
		return "view/ListaServicos";
	}
    */
	
	//Método para filtrar os servicos pelo nome do cliente
	@PostMapping("/buscasCliente")
	// @PreAuthorize("hasAuthority('admin')")
	public String buscaServicos(@RequestParam("pesquisa")String nome,ModelMap model) {
	//repositoryServico.buscaServicos(nome);
		/*ModelAndView  mdv = new ModelAndView ("view/ListaServicos");
		mdv.addObject("mostraServicos",repositoryServico.buscaServicos(nome));
		System.out.println(repositoryServico.buscaServicos(nome));
     */ 
		//condição para lista tudo
		if(nome.equals("lista") || nome==null) {
			return "redirect:/servicos/listaTodos";
		}
		
		List<Servicos> servicos = repositoryServico.buscaServicos(nome);
		model.addAttribute("mostraServicos", servicos);
		return "view/ListaServicos";
	}
	
	
		//Método para lista todos serviços
	@GetMapping("/listaTodos")
	//@Transactional(readOnly = true)
	public String listaServicos( ModelMap model,Servicos serv) {
		
		  // Pegando email do usuário 
		 String email= Usuario.getEmailUsuario();
		  System.out.println(" aqui o email "+Usuario.getEmailUsuario());
		  
		  // Pegando id do usuário pelo email informado no paramentro
		  int id = repository.BuscaIdPeloEmail(email);
			System.out.println("aqui  id do usuário é = "+id);
		
			
			// buscando todos dados do usuário pelo id informa no paramentro
		     Usuario us = repository.BuscaTodosDadosPeloId(id);
		 System.out.println("O objeto é esse  "+us.getId());
			
			 // Passando o objeto us de usuário para salva nos serviços
			 serv.setUsuario(us);
			 System.out.println(" o ID do objeto "+serv.getUsuario());
			
		  // buscando por todos registros de serviços
		//List<Servicos> todosServicos = repositoryServico.findAll();
			 
	    // Listando todos servicos pelo id do usuário  		 
		List<Servicos> todosServicos = repositoryServico.listaServicosPeloId(us);
		List<ClienteModel> clientes = clienteRp.listaClientesPeloIdUsuario(us);
		  
		// condição para saber se as tabelas ClienteModel e Servicos estão vazias
		if(todosServicos.isEmpty() && clientes.isEmpty()) {
			// Passando o resultado para decimal
			DecimalFormat decimal = new DecimalFormat("#,##0.00");
			double total=0;                   // exibindo o resultado
			model.addAttribute("msgListaTotal", "R$ "+decimal.format(total));
			// retornando para página de lista servicos
			return  "view/ListaServicos";
		}
		 //Pegando toda soma dos servicos pelo id do usuário
		serv.setValorTotal(repositoryServico.soma(us)); // somando a qtd
		// Passando o resultado para decimal
		DecimalFormat decimal = new DecimalFormat("#,##0.00");
		String total =decimal.format(serv.getValorTotal());
		// Passando valor total para se exibida na página html
		model.addAttribute("msgListaTotal", "R$ "+total);
		// Passando todos os objetos para a página de lista de serviços
		model.addAttribute("mostraServicos",todosServicos);
		// retornando para página de lista servicos
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
