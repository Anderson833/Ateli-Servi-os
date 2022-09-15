package br.edu.ifrn.atelie.Controller;
//Essa classe vai controlar as requisições dos dados cliente

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.edu.ifrn.atelie.Modelo.ClienteModel;
import br.edu.ifrn.atelie.Repository.ClienteRepository;

@Controller
@RequestMapping("/Clientes")
public class ClienteController {

	@Autowired
	private ClienteRepository repository;
	
	// Método para abrir a página de cadastrar clientes e passar os objetos
	 @GetMapping("/home")
	public String home(ModelMap model) {
		 model.addAttribute("pessoa",new ClienteModel());
		return "view/Clientes";
	}
	 //Método para adicionar os dados dos clientes
	 @PostMapping("/Cadastro")
	 public String adicionarClientes(ClienteModel cliente) {
		 repository.save(cliente);
		 return "redirect:/Clientes/home";
	 }
	 
	 //método para abrir a tela principal
	 @GetMapping("/principal")
	 public String telaPrincipal() {
		 return "view/Principal";
	 }

	//método que inicia a pagina de listagem 
	 @GetMapping("/listar")
	public String iniciolista() {
		return "view/ListaClientes";
	}

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
		    return "rederict:/Clientes/listar";
				
				
			}
		
}
