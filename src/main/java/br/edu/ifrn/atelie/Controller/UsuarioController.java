package br.edu.ifrn.atelie.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
	 @GetMapping("/usuario")
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
	 public String salvarUsuario(Usuario usuario, RedirectAttributes at) {
		 
		 // criptografia para senha Segura do usuário
			String senhaSegura = new BCryptPasswordEncoder().encode(usuario.getSenha());
			usuario.setSenha(senhaSegura);
		 
		 // salvando os dados do usuário
		 repository.save(usuario);
		 // método para exibir uma mensagem para o usuário
		 at.addFlashAttribute("msgUsuario","Usuário salvo com sucesso!");
		 
		 return"view/usuario";
	 }
}
