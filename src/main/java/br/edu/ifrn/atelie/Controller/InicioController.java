package br.edu.ifrn.atelie.Controller;

import javax.swing.JOptionPane;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.edu.ifrn.atelie.Modelo.Servicos;
import br.edu.ifrn.atelie.Modelo.Usuario;
import br.edu.ifrn.atelie.Repository.UsuarioRepository;

@Controller
public class InicioController {
    
	@Autowired
	private UsuarioRepository repository;
	
	// método para abrir a página de login 
	@GetMapping("/login")
	public String inicio(){
		return "view/login";
	}

	// método para caso de error ao fazer login exibir uma mensagem
    @GetMapping("/login-Error")
	public String ErrorDeLogin(ModelMap model) {
   
    	model.addAttribute("msgErrorLogin"," Email ou  senha estão incorretos. Tente novamente!");
    	return "view/login";
	}
    
	
    //método para abrir a tela principal
	 @GetMapping("/")
	 public String telaPrincipal(RedirectAttributes att) {
		// Usuario user = new Usuario()
		 
		 // Pegando o email do usuário 
	/*	String email =Usuario.getEmailUsuario();
		System.out.println(" Esse é o email do Usuário "+email);
		
		// Pegando o id do usuário pelo email passado como parametros 
		int id = repository.BuscaIdPeloEmail(email);
		System.out.println("id desse usuário é = "+id);
	//	String senha = repository.BuscaSenhaPeloEmaileId(email, id);
	 	// String senha = repository.BuscaSenhaPeloEmail(email);
	//	System.out.println("senha do Usuário é = "+senha);
	    */
		 return "view/Principal";	 
	 }
    
	/* @PostMapping("/login")
		public String buscaId(@RequestParam("username") String email,
	             ModelMap att) {
	  String id= repository.BuscaIdPeloEmail(email);
	  att.addAttribute("msglk", id);
	  System.out.println("id do usuário"+id);
	           return "view/Principal";
		}*/
	 
	  //método para abrir a tela de opção de cadastro
		 @GetMapping("/opcaoCadastro")
		 public String telaDeOpcao() {
			 return "view/opcaoCadastro";
		 }
		
		 
	/*	 @RequestMapping("/test")
		 public String bot() {
			 return "view/bot";
		 }
		 */
		 
}
