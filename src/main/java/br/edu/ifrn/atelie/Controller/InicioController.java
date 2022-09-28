package br.edu.ifrn.atelie.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InicioController {
         
	// método para abrir a página de login 
	@GetMapping("/login")
	public String inicio() {
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
	 public String telaPrincipal() {
		 return "view/Principal";
	 }
    
	  //método para abrir a tela de opção de cadastro
		 @GetMapping("/opcaoCadastro")
		 public String telaDeOpcao() {
			 return "view/opcaoCadastro";
		 }
}
