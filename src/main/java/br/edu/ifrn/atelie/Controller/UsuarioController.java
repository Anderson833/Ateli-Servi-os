package br.edu.ifrn.atelie.Controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.edu.ifrn.atelie.Modelo.Usuario;
import br.edu.ifrn.atelie.Modelo.Visitante;
import br.edu.ifrn.atelie.Repository.UsuarioRepository;
import br.edu.ifrn.atelie.Repository.VisitanteRepository;

@Controller
public class UsuarioController {
    
	@Autowired
	private UsuarioRepository repository;
	
	@Autowired
	private VisitanteRepository visitanteRp;
	 //Método para abrir a página de de cadastro de usuário e passar os atributos do tipo usuário  
	 @GetMapping("/Cadastra-se")
	 public String cadastraUsuario(ModelMap model) {
		 model.addAttribute("usuario", new Usuario());
		 return "view/usuario";
	 }
	 
	 //Método para abrir a página de de cadastro de visitante e passar os atributos do tipo visitante
	 @GetMapping("/visitante")
	 public String cadastraVisitante(ModelMap model) {
		 model.addAttribute("visitante", new Visitante());
		 return "view/visitante";
	 }
	 //Método para salvar usuário
	 @PostMapping("/adicionar") //Passando como parâmentro objeto visitante 
	 public String salvarVisitante(Visitante visitante, RedirectAttributes at) {
		 
		 // criptografia para senha Segura do visitante
			String senhaSegura = new BCryptPasswordEncoder().encode(visitante.getSenha());
			visitante.setSenha(senhaSegura);
		 
		 // salvando os dados do visitante
		  visitanteRp.save(visitante);
		 // método para exibir uma mensagem para o visitante
		 at.addFlashAttribute("msgVisitante","Operação realizada com sucesso!");
		 //retornando para a página de visitante
		 return"view/visitante";
	 }
	 
	 //Método para salvar usuário
	 @PostMapping("/Salvar")
	 public String salvarUsuario(Usuario usuario, RedirectAttributes at,@RequestParam("Email") String email) {
		 
		 // criptografia para senha Segura do usuário
			String senhaSegura = new BCryptPasswordEncoder().encode(usuario.getSenha());
			usuario.setSenha(senhaSegura);
		    // Pegando email igual caso tenha cadastrado no banco de dados
		      String usu =repository.buscaTodosEmail(email);
		
		      // condição para saber se tem esse email salvo no banco de dados 
		if(email.equals(usu)) {
		//	System.out.println("já tem esse email"+email);
			
			// Passando uma mensagem para se exibida caso os dados estejam incorretos
			 at.addFlashAttribute("msgErrorSalvar","Já contém Usuário com esses dados, tente outro diferente!");
			 return "redirect:/usuario";
		}
			 // salvando os dados do usuário
			  repository.save(usuario);
			 // método para exibir uma mensagem para o usuário 
			  at.addFlashAttribute("msgCadastrado","Usuário salvo com sucesso!");
			// retornando para a página de cadastro de usuario
			  return "redirect:/Cadastra-se";
			 
		}
		
	 
	  //método para excluir um Usuário
		 @GetMapping("/Conta-Excluida") 
		 @Transactional(readOnly = false) 
		 @PreAuthorize("hasAuthority('admin')")//Passando o  e o objeto usuário como  parâmetros 
		 public String excluirUsuario(Usuario usuario) {
			 String email =Usuario.getEmailUsuario();
					// Pegando o id do usuário pelo email passado como parametros 
					int id = repository.BuscaIdPeloEmail(email);
					
					System.out.println("id desse usuário é = "+id);
			// comando que fazer deletar o usuário pelo código
			 repository.deleteById(id);
             System.out.println(" id deletado "+id);
			 // retornando para página  de login 
			 return "redirect:/login";
		 }
	 
	 }
	 
	 

