package br.edu.ifrn.atelie.Controller;

import java.text.DecimalFormat;
import java.util.List;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.edu.ifrn.atelie.Modelo.Despesa;
import br.edu.ifrn.atelie.Modelo.InvestimentoModel;
import br.edu.ifrn.atelie.Modelo.Usuario;
import br.edu.ifrn.atelie.Repository.InvestimentoRepository;
import br.edu.ifrn.atelie.Repository.UsuarioRepository;
import ch.qos.logback.core.joran.action.IADataForComplexProperty;

// ESSE CLASSE VAI CONTROLA AS REQUISIÇÕES DOS INVESTIMENTOS;
@Controller
@RequestMapping("/Investimento")
public class InvestimentoController {

	@Autowired
	private InvestimentoRepository repository;
	@Autowired
	private UsuarioRepository repositoryUsuario;
	// método para abrir a página de investimento e passar os objetos 
	  @GetMapping("/investir")
	public String inicioInvestimento(ModelMap model,InvestimentoModel invest) {
		  // Pegando email do usuário 
			 String email= Usuario.getEmailUsuario();
			  System.out.println(" aqui o email "+Usuario.getEmailUsuario());
			  // Pegando id do usuário pelo email informado no paramentro
			  int id = repositoryUsuario.BuscaIdPeloEmail(email);
				System.out.println("aqui  id do usuário é = "+id);
			
				// buscando todos dados do usuário pelo id informa no paramentro
			     Usuario us = repositoryUsuario.BuscaTodosDadosDoUsuarioPeloId(id);
			 System.out.println("O objeto é esse  "+us.getId());
			 // Passando o objeto us de usuário para salva nos serviços
			 invest.setUsuario(us);
			 System.out.println(" o ID do objeto "+invest.getUsuario());
		     model.addAttribute("invest", invest) ; 
		return "view/investimento";
	}
	  
	//Método para excluir algum investimento
		@GetMapping("/excluir/{id}")
		@Transactional(readOnly = false)
		 @PreAuthorize("hasAuthority('admin')")
		public String excluirInvestimento(@PathVariable("id") Integer id, InvestimentoModel invest,RedirectAttributes att) {
			repository.deleteById(id);
			
		
			att.addFlashAttribute("msgExcluirInvestimento","Investimento excluído com Sucesso!");
			
			return "redirect:/Investimento/lista";
		}
	  
	  @GetMapping("/lista")
	  public String listaInvestimento(ModelMap md, InvestimentoModel invest) {
		  
		  // Pegando email do usuário 
			 String email= Usuario.getEmailUsuario();
			  System.out.println(" aqui o email "+Usuario.getEmailUsuario());
			  // Pegando id do usuário pelo email informado no paramentro
			  int id = repositoryUsuario.BuscaIdPeloEmail(email);
				System.out.println("aqui  id do usuário é = "+id);
			
				// buscando todos dados do usuário pelo id informa no paramentro
			     Usuario us = repositoryUsuario.BuscaTodosDadosDoUsuarioPeloId(id);
			 System.out.println("O objeto é esse  "+us.getId());
			
			 // Listando todos servicos pelo id do usuário  		 
			List<InvestimentoModel> todosInvestimentos = repository.listaInvestimentoPeloId(us);
		
		  
			// condição para saber se a tabela de investimento esta vazia
			if(todosInvestimentos.isEmpty()|| todosInvestimentos==null) {
				// Passando o resultado para decimal
				DecimalFormat decimal = new DecimalFormat("#,##0.00");
				double total=0;                   // exibindo o resultado
				md.addAttribute("TotalInvestimento", "R$ "+decimal.format(total));
				md.addAttribute("invest",repository.listaInvestimentoPeloId(us));
				// retornando para página de lista investimentos
				return  "view/ListaInvestimento";
			}else {
				//Pegando toda soma dos investimentos pelo id do usuário
				invest.setValorTotal(repository.soma(us)); 
				// Passando o resultado para decimal
				DecimalFormat decimal = new DecimalFormat("#,##0.00");
				String total =decimal.format(invest.getValorTotal());
				// Passando valor total para se exibida na página html
				md.addAttribute("TotalInvestimento", "R$ "+total);
				// Passando todos os objetos para a página de lista despesas
				md.addAttribute("invest",repository.listaInvestimentoPeloId(us));
				return "view/listaInvestimento";
			}

	  }
	  

	  public String listaSomaInvestimentoIdUsuarioProduto(ModelMap md, InvestimentoModel invest,String produto) {
		  
		  // Pegando email do usuário 
			 String email= Usuario.getEmailUsuario();
			  System.out.println(" aqui o email "+Usuario.getEmailUsuario());
			  // Pegando id do usuário pelo email informado no paramentro
			  int id = repositoryUsuario.BuscaIdPeloEmail(email);
				System.out.println("aqui  id do usuário é = "+id);
			
				// buscando todos dados do usuário pelo id informa no paramentro
			     Usuario us = repositoryUsuario.BuscaTodosDadosDoUsuarioPeloId(id);
			 System.out.println("O objeto é esse  "+us.getId());
			
			 // Listando todos servicos pelo id do usuário  		 
			List<InvestimentoModel> listaSomaInvestimentosUsuarioCliente = repository.listaInvestimentoPeloIdUsuarioNomeProduto(us,produto);
		
			if(listaSomaInvestimentosUsuarioCliente==null) {
				// Passando o resultado para decimal
				DecimalFormat decimal = new DecimalFormat("#,##0.00");
				double total=0;  // exibindo o resultado
				md.addAttribute("TotalInvestimento", "R$ "+decimal.format(total));
				md.addAttribute("invest",listaSomaInvestimentosUsuarioCliente);
				// retornando para página de lista investimentos
				return  "view/LstaInvestimento";
		
			}else if(produto.isEmpty()) {
				// Passando o resultado para decimal
				DecimalFormat decimal = new DecimalFormat("#,##0.00");
				double total=0;  // exibindo o resultado
				md.addAttribute("TotalInvestimento", "R$ "+decimal.format(total));
				
			}else if(produto.equalsIgnoreCase("lista")) {	
				  return "redirect:/Investimento/lista";
				  
			}else if(repository.somaPorIdUsuarioNomeProduto(us, produto)==null) {
				// Passando o resultado para decimal
				DecimalFormat decimal = new DecimalFormat("#,##0.00");
				double total=0;// exibindo o resultado
				md.addAttribute("TotalInvestimento", "R$ "+decimal.format(total));
				md.addAttribute("invest",repository.listaInvestimentoPeloIdUsuarioNomeProduto(us,produto));
				// retornando para página de lista investimentos
				return  "view/ListaInvestimento";
			}else {
				//Pegando toda soma dos investimentos pelo id do usuário
				invest.setValorTotal(repository.somaPorIdUsuarioNomeProduto(us,produto)); 
				// Passando o resultado para decimal
				DecimalFormat decimal = new DecimalFormat("#,##0.00");
				String total =decimal.format(invest.getValorTotal());
				// Passando valor total para se exibida na página html
				md.addAttribute("TotalInvestimento", "R$ "+total);
				// Passando todos os objetos para a página de lista despesas
				md.addAttribute("invest",repository.listaInvestimentoPeloIdUsuarioNomeProduto(us,produto));
			
			}
			return "view/listaInvestimento";
	  }
	  
	  
	  //Método para lista e soma por id do usuário e as datas
	  public String listaSomaInvestimentoIdUsuarioDatas(ModelMap md, InvestimentoModel invest,@RequestParam(name="dataInicio",required = false)String datai,@RequestParam(name="dataFinal",required = false)String dataf) {
		  
		  // Pegando email do usuário 
			 String email= Usuario.getEmailUsuario();
			  System.out.println(" aqui o email "+Usuario.getEmailUsuario());
			  // Pegando id do usuário pelo email informado no paramentro
			  int id = repositoryUsuario.BuscaIdPeloEmail(email);
				System.out.println("aqui  id do usuário é = "+id);
			
				// buscando todos dados do usuário pelo id informa no paramentro
			     Usuario us = repositoryUsuario.BuscaTodosDadosDoUsuarioPeloId(id);
			 System.out.println("O objeto é esse  "+us.getId());
			 // Convertendo as datas
			  String dataComenco= dataConvertida(datai);
			  String dataFim =dataConvertida(dataf); 
			 // Listando todos servicos pelo id do usuário  e as datas 		 
			List<InvestimentoModel> listaInvestimentosUsuarioDatas = repository.listaInvestimentoPeloIdUsuarioDatas(us,dataComenco,dataFim);
		
			if(listaInvestimentosUsuarioDatas==null) {
				// Passando o resultado para decimal
				DecimalFormat decimal = new DecimalFormat("#,##0.00");
				double total=0;  // exibindo o resultado
				md.addAttribute("TotalInvestimento", "R$ "+decimal.format(total));
				md.addAttribute("invest",listaInvestimentosUsuarioDatas);
				// retornando para página de lista investimentos
				return  "view/LstaInvestimento";
		
			}else if(dataComenco.isEmpty()&&dataFim.isEmpty()) {
				// Passando o resultado para decimal
				DecimalFormat decimal = new DecimalFormat("#,##0.00");
				double total=0;  // exibindo o resultado
				md.addAttribute("TotalInvestimento", "R$ "+decimal.format(total));
				
			}else if(repository.somaInvestimentoPeloIdUsuarioDatas(us, dataComenco,dataFim)==null) {
				// Passando o resultado para decimal
				DecimalFormat decimal = new DecimalFormat("#,##0.00");
				double total=0;// exibindo o resultado
				md.addAttribute("TotalInvestimento", "R$ "+decimal.format(total));
				md.addAttribute("invest",listaInvestimentosUsuarioDatas);
				// retornando para página de lista investimentos
				return  "view/ListaInvestimento";
			}else {
				//Pegando toda soma dos investimentos pelo id do usuário
				invest.setValorTotal(repository.somaInvestimentoPeloIdUsuarioDatas(us, dataComenco, dataFim)); 
				// Passando o resultado para decimal
				DecimalFormat decimal = new DecimalFormat("#,##0.00");
				String total =decimal.format(invest.getValorTotal());
				// Passando valor total para se exibida na página html
				md.addAttribute("TotalInvestimento", "R$ "+total);
				// Passando todos os objetos para a página de lista despesas
				md.addAttribute("invest",listaInvestimentosUsuarioDatas);
			
			}
			return "view/listaInvestimento";
	  }
	  
      //Método para lista os investimentos pelo id do usuario, nome do produto, e as datas
	  public String listaSomaInvestimentoIdUsuarioProdutoDatas(ModelMap md, InvestimentoModel invest,String produto,String datai,String dataf) {
		  
		  // Pegando email do usuário 
			 String email= Usuario.getEmailUsuario();
			  System.out.println(" aqui o email "+Usuario.getEmailUsuario());
			  // Pegando id do usuário pelo email informado no paramentro
			  int id = repositoryUsuario.BuscaIdPeloEmail(email);
				System.out.println("aqui  id do usuário é = "+id);
			
				// buscando todos dados do usuário pelo id informa no paramentro
			     Usuario us = repositoryUsuario.BuscaTodosDadosDoUsuarioPeloId(id);
			 System.out.println("O objeto é esse  "+us.getId());
			 //Convertendo as datas de date para String 
			  String datainicio=dataConvertida(datai);
			  String datafinal=dataConvertida(dataf);
			 // Listando todos servicos pelo id do usuário  		 
			List<InvestimentoModel> listaInvestimentoIdUsuarioProdutoDatas = repository.listaInvestimentoPeloIdUsuarioProdutoDatas(us,produto,datainicio,datafinal);
		
			if(listaInvestimentoIdUsuarioProdutoDatas==null) {
				// Passando o resultado para decimal
				DecimalFormat decimal = new DecimalFormat("#,##0.00");
				double total=0;  // exibindo o resultado
				md.addAttribute("TotalInvestimento", "R$ "+decimal.format(total));
				md.addAttribute("invest",listaInvestimentoIdUsuarioProdutoDatas);
				// retornando para página de lista investimentos
				return  "view/LstaInvestimento";
		
			}else if(produto.isEmpty()&& datainicio.isEmpty()&&datafinal.isEmpty()) {
				// Passando o resultado para decimal
				DecimalFormat decimal = new DecimalFormat("#,##0.00");
				double total=0;  // exibindo o resultado
				md.addAttribute("TotalInvestimento", "R$ "+decimal.format(total));
				  
			}else if(repository.somaInvestimentoPeloIdUsuarioProdutoDatas(us, produto,datainicio,datafinal)==null) {
				// Passando o resultado para decimal
				DecimalFormat decimal = new DecimalFormat("#,##0.00");
				double total=0;// exibindo o resultado
				md.addAttribute("TotalInvestimento", "R$ "+decimal.format(total));
				md.addAttribute("invest",listaInvestimentoIdUsuarioProdutoDatas);
				// retornando para página de lista investimentos
				return  "view/ListaInvestimento";
			}else {
				//Pegando toda soma dos investimentos pelo id do usuário
				invest.setValorTotal(repository.somaInvestimentoPeloIdUsuarioProdutoDatas(us, produto, datainicio, datafinal)); 
				// Passando o resultado para decimal
				DecimalFormat decimal = new DecimalFormat("#,##0.00");
				String total =decimal.format(invest.getValorTotal());
				// Passando valor total para se exibida na página html
				md.addAttribute("TotalInvestimento", "R$ "+total);
				// Passando todos os objetos para a página de lista despesas
				md.addAttribute("invest",listaInvestimentoIdUsuarioProdutoDatas);
			
			}
			return "view/listaInvestimento";
	  }
	  
	  @PostMapping("/lista/Produto/Datas")
	  public String listaSomaInvestimentoPorOpcoes(ModelMap md, InvestimentoModel invest,@RequestParam(name="listaprodutos",required = false)String produto,@RequestParam(name="dataInicio",required = false)String datai,
			  @RequestParam(name="dataFinal",required = false)String dataf) {
		   
		  if(!produto.isEmpty()) {
		  listaSomaInvestimentoIdUsuarioProduto(md, invest, produto);
		  }else if(produto.isEmpty() && !datai.isEmpty()&& !dataf.isEmpty()) {
            listaSomaInvestimentoIdUsuarioDatas(md, invest, datai, dataf);
		  }else {
			  listaSomaInvestimentoIdUsuarioProdutoDatas(md, invest, produto, datai, dataf);  
		  }
		  
	    return "view/listaInvestimento";
	  }
	  
	  @PostMapping("/inserir")
	  public String inserirInvestimento(InvestimentoModel invest,RedirectAttributes att) {
		   repository.save(invest);
		   att.addFlashAttribute("msgInvestimento","Investimento inserido com sucesso!");
		 //  return "view/investimento";
		  return "redirect:/Investimento/investir"; 
	  }
	  
	// Método que convert a data de date para String de trás para frente
		public String dataConvertida(String data) {
			// variaveis do tipo String para armazenar os caracteres unico e específicos
			String dataConvert = "", caracteres = "", p0 = "", p1 = "", p2 = "", p3 = "", p4 = "", p5 = "", p6 = "",
					p7 = "", p8 = "", p9 = "";
			// variaveis do tipo char para armazenar cada caracter específicos
			char i0, i1, i2, i3, i4, i5, i6, i7, i8, i9;
			// String test="2023/04/20";
			// Lista o tamanho do atributo data que vem do paramentro
			int tamanho = data.length();
			// Uma for para percorrer todo tamanho do atributo e lista os caracteres
			// específicos
			for (int i = 0; i < tamanho; i++) {
				char caracter = data.charAt(i);
				// as condições para pegar cada caracter
				if (i == 9) {
					i9 = caracter;
					caracteres = String.valueOf(i9);
					p9 = caracteres;
					/// System.out.print(" index 9 "+p9);

				}
				if (i == 8) {
					i8 = caracter;
					caracteres = String.valueOf(i8);
					p8 = caracteres;
					// System.out.print(" index 8 "+p8);
				}
				if (i == 7) {
					i7 = caracter;
					caracteres = String.valueOf(i7);
					p7 = caracteres.replace("-", "/");
					// System.out.print(" index 7 "+p7);
				}
				if (i == 6) {
					i6 = caracter;
					caracteres = String.valueOf(i6);
					p6 = caracteres;
					// System.out.print(" index 6 "+p6);
				}
				if (i == 5) {
					i5 = caracter;
					caracteres = String.valueOf(i5);
					p5 = caracteres;
					// System.out.print(" index 5 "+p5);
				}
				if (i == 4) {
					i4 = caracter;
					caracteres = String.valueOf(i4);
					p4 = caracteres.replace("-", "/");
					// System.out.print(" index 4 "+p4);
				}
				if (i == 3) {
					i3 = caracter;
					caracteres = String.valueOf(i3);
					p3 = caracteres;
					// System.out.print(" index 3 "+p3);
				}
				if (i == 2) {
					i2 = caracter;
					caracteres = String.valueOf(i2);
					p2 = caracteres;
					// System.out.print("index 2 "+p2);
				}
				if (i == 1) {
					i1 = caracter;
					caracteres = String.valueOf(i1);
					p1 = caracteres;
					// System.out.print(" index 1 "+p1);
				}
				if (i == 0) {
					i0 = caracter;
					caracteres = String.valueOf(i0);
					p0 = caracteres;
					// System.out.print(" index 0 "+p0);
				}
				if (i == 9) {
					// System.out.println(" "+test+" Data convertida
					// "+p8+p9+p7+p5+p6+p4+p0+p1+p2+p3);
					dataConvert = p8 + p9 + p7 + p5 + p6 + p4 + p0 + p1 + p2 + p3;
					// System.out.println(" "+dataConvert);
				}
			}
			// Retornando uma variável com todos caracteres invertidos do tipo de dado date
			return dataConvert;
		}
	  
	// Método para altera ou editar os dados 
			@GetMapping("/editar/{id}")
			 @PreAuthorize("hasAuthority('admin')")
			public String editarInvestimento(@PathVariable("id") int idinvest, ModelMap md) {
				
				// Passando os objetos para a página de investimento
				md.addAttribute("invest", repository.findById(idinvest));
				// deletando um investimento
				 repository.deleteById(idinvest);
				// retornando a ação para página de  investimento
				return "view/Investimento";
				
			}
}
