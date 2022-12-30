package br.edu.ifrn.atelie.Controller;
//Essa classe vai controlar as requisições dos dados cliente

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.edu.ifrn.atelie.Modelo.ClienteModel;
import br.edu.ifrn.atelie.Modelo.Servicos;
import br.edu.ifrn.atelie.Modelo.Usuario;
import br.edu.ifrn.atelie.Repository.ClienteRepository;
import br.edu.ifrn.atelie.Repository.UsuarioRepository;

@Controller
@RequestMapping("/Clientes")
public class ClienteController {

	@Autowired
	private ClienteRepository repository;
	
	@Autowired
	private UsuarioRepository repositoryUsuario;
	
	// Método para abrir a página de cadastrar clientes e passar os objetos
	 @GetMapping("/home")
	public String home(ModelMap model, ClienteModel cli) {
		 
			  // Pegando email do usuário 
		 String email= Usuario.getEmailUsuario();
		  System.out.println(" aqui o email "+Usuario.getEmailUsuario());
		  
		  // Pegando id do usuário pelo email informado no paramentro
		  int ids = repositoryUsuario.BuscaIdPeloEmail(email);
			System.out.println("aqui  id do usuário é = "+ids);
			
			// buscando todos dados do usuário pelo id informa no paramentro
		     Usuario us = repositoryUsuario.BuscaTodosDadosPeloId(ids);
		 System.out.println("O objeto é esse  "+us.getId());
		 // Passando para objeto do tipo usuário para atributo no objeto ClienteModel
		  cli.setUsuario(us);
	     // Passando o objeto para página html 
		 model.addAttribute("pessoa",cli);
		return "view/Clientes";
	}
	 //Método para adicionar os dados dos clientes
	 @PostMapping("/Cadastro")
	 @Transactional(readOnly = false)
	 @PreAuthorize("hasAuthority('admin')")
	 public String adicionarClientes(ClienteModel cliente,RedirectAttributes At) {
		 repository.save(cliente);
		 
		 At.addFlashAttribute("msgSucesso","Cliente Cadastrado com sucesso!");
		 return "redirect:/Clientes/home";
	 }
	 
	//método que inicia a pagina de listagem 
	 @GetMapping("/listar")
	public String iniciolista() {
		return "view/listaClientes";
	}
    
		// Método para editar cliente
		@GetMapping("/edita/{id}")
		 @PreAuthorize("hasAuthority('admin')")
		public String editarServico(@PathVariable("id") Integer idCliente, ModelMap model) {
			 // buscando pelo id do tipo do cliente
			Optional<ClienteModel> clientes = repository.findById(idCliente);
			// deletando o tipo de cliente que tem esse tipo de id para adicionar outro novo
		    repository.deleteById(idCliente);
		     // Passando os objetos para a página de cadastro de clientes
			model.addAttribute("pessoa",clientes);
			// retornando a ação para página de Lista todos clientes
			return "view/Clientes";
		}
		
	 //método para lista todos clientes
	  @GetMapping("/listaTodosClientes")
	  @Transactional(readOnly = true)
	 public String listaTudo(ClienteModel cli ,ModelMap md) {
		  
		  // Pegando email do usuário 
			 String email= Usuario.getEmailUsuario();
			  System.out.println(" aqui o email "+Usuario.getEmailUsuario());
			  
			  // Pegando id do usuário pelo email informado no paramentro
			  int id = repositoryUsuario.BuscaIdPeloEmail(email);
				System.out.println("aqui  id do usuário é = "+id);
			
				
				// buscando todos dados do usuário pelo id informa no paramentro
			     Usuario us = repositoryUsuario.BuscaTodosDadosPeloId(id);
			 System.out.println("O objeto é esse  "+us.getId());
				// Passando todos clientes pelo id do usuário
		  List<ClienteModel> clientes = repository.listaClientesPeloIdUsuario(us);
	//	 List<ClienteModel> clientes = repository.findAll();
		 md.addAttribute("clientes",clientes);
		 return "view/listaTudo";
	  }
	 
	   // método para lista todos os clientes
	 @GetMapping("/Listagem")     //passando os dados como parâmetros para lista os dados dos clientes
		public String listarVagas(@RequestParam(name="nome" ,required = false) String nome,
					                     @RequestParam(name="telefone", required = false) String telefone,
					                     @RequestParam(name="endereco", required = false) String endereco,
					                     @RequestParam(name="mostrarClientes", required = false) Boolean mostrarClientes, HttpSession session, ModelMap model) {
                    // Passando uma lista  como parâmetros os dados que pertence ao cliente para criar um objeto
		    	List<ClienteModel> ClientesCadastrados= repository.findByNomeAndTelefoneAndEndereco(nome,telefone,endereco); 
		    	// atribuindo o objeto do tipo cliente
		        model.addAttribute("clientesCadastrados",ClientesCadastrados);    
		        // condição para saber se a lista  de clientes está sem dados 
		   if(mostrarClientes != null) {		   
			   //atribuindo a lista todos os clientes
			   model.addAttribute("mostrarClientes",true);
		   }
		   // redirecionado para página de lista de clientes
		    return "view/listaClientes";	
			}
	 
	  //método para excluir cliente
	 @GetMapping("/excluir/{id}") 
	 @Transactional(readOnly = false) 
	 @PreAuthorize("hasAuthority('admin')")//Passando o  e o objeto clinte como  parâmetros 
	 public String excluirClientes(@PathVariable("id")ClienteModel cliente, RedirectAttributes atr) {
		// comando que fazer deletar o cliente pelo código
		 repository.delete(cliente);
		 // atribuindo uma mensagem para a view de lista clientes
		 atr.addFlashAttribute("msgError", "Cliente deletado com Sucesso!");
		 // retornando a página para todos dados listados 
		 return "redirect:/Clientes/listaTodosClientes";
	 }
		
}
