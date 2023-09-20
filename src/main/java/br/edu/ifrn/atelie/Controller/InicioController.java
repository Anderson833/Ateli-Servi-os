package br.edu.ifrn.atelie.Controller;

import java.util.ArrayList;
import java.util.List;

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
import br.edu.ifrn.atelie.Service.Ajustes;
import br.edu.ifrn.atelie.Service.UsuarioService;

@Controller
public class InicioController extends Ajustes{
    
	@Autowired
	private UsuarioRepository repository;
	
	int contador=0;
	
	//Endereço da URL para abrir a página de início http://localhost:8080/login
	
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
		 public String telaPrincipalComAutorizacao(RedirectAttributes att) {
		
			Ajustes.idUsuarioAoLogar(repository);
			//System.out.println(" id do usuario "+controle.idUsuariologin());
	   /*
			 ArrayList<Integer> acesso = new ArrayList<>();
			 ArrayList<Integer> restricao = new ArrayList<>();
			// Pegando o id do usuário pelo email passado como parametros 
			 
			
			int id = repository.BuscaIdPeloEmail(email);
		 				 System.out.println("id desse usuário é o antigo  = "+id);
		 
			acesso.add(id);
		    restricao.add(0);
		   
	    	  if(repository.existsById(id)) {
			    	
			    }else {
					att.addFlashAttribute("msgUsuarioNaoExiste","Não existe usuário cadastrado com esse email!");
			        return "view/login";
			    }
		    
			//restricao.clear();
			if(acesso.contains(0)) {
				contador++;
				System.out.println("vezes "+contador);
                  if(contador==3){
                		System.out.println("Entrou no if id do usuário="+id);
        				att.addFlashAttribute("msgPermicao", "Indesponível por ausência de pagamento!");
                          return "redirect:/login";  
                  }
			}else {
				
			}
			System.out.println("Entrou no else ");*/
			return "view/Principal";
		 }
	    
	 
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
