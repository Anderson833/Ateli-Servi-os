package br.edu.ifrn.atelie.Controller;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
	     Usuario us = repository.BuscaTodosDadosDoUsuarioPeloId(id);
	 System.out.println("O objeto é esse  "+us.getId());
	 
	 // Passando o objeto us de usuário para salva nos serviços
	 serv.setUsuario(us);
	 System.out.println(" o ID do objeto "+serv.getUsuario());
	  //  Passando o objeto para exibir na página html de cadastro de servicos
		model.addAttribute("tarefas", serv);
		return "view/tarefas";
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
	public String adicionarServicos(Servicos serv, ModelMap md, RedirectAttributes att,@RequestParam(name="buscaNome",required = false)String nome) {
		
		
		List<ClienteModel> cliente = clienteRp.listaClientePeloNome(nome);
		//condição para lista tudo
		if(cliente==null) {
			att.addFlashAttribute("msgsucesso","Não tem esse usuário cadastrado!");
			System.out.println("entrou fo if ");
			return "redirect:/servicos/atividades";
		
		}else {
			  // Convertendo a data de date para String e salva no banco de dados como varchar
			String dataConvertida=dataConvertida(serv.getData());
			// setando a data convertida
			serv.setData(dataConvertida);
			att.addFlashAttribute("msgsucesso","Operação Realizada Com Sucesso!");
			//já salvar no banco de dados
			repositoryServico.save(serv);
		}
		
		
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
		
		 // Pegando email do usuário 
		 String email= Usuario.getEmailUsuario();
		
		  System.out.println(" aqui o email "+Usuario.getEmailUsuario());
		  
		  // Pegando id do usuário pelo email informado no paramentro
		  int id = repository.BuscaIdPeloEmail(email);
			System.out.println("aqui  id do usuário é = "+id);
			
			// buscando todos dados do usuário pelo id informa no paramentro
		    Usuario  us = repository.BuscaTodosDadosDoUsuarioPeloId(id);
		 System.out.println("O objeto é esse  "+us.getId());
		 List<Servicos> servicos = repositoryServico.buscaServicos(nome);
	
		
		//condição para lista tudo
		if(nome.equals("lista") || nome==null || servicos==null) {
			// Passando o resultado para decimal
			DecimalFormat decimal = new DecimalFormat("#,##0.00");
			double totalVazio=0;// exibindo o resultado
			model.addAttribute("msgListaTotal", "R$ "+decimal.format(totalVazio));
			return "redirect:/servicos/listaTodos";
		}else if(repositoryServico.somandoPeloNomeDoCliente(us, nome)==null) {
			// Passando o resultado para decimal
			DecimalFormat decimal = new DecimalFormat("#,##0.00");
		 double totalZero=0;	// exibindo o resultado
			model.addAttribute("msgListaTotal", "R$ "+decimal.format(totalZero));
		
		}else {
			// Passando o resultado para decimal
						DecimalFormat decimal = new DecimalFormat("#,##0.00");
						double total =repositoryServico.somandoPeloNomeDoCliente(us, nome);
						// exibindo o resultado
						model.addAttribute("msgListaTotal", "R$ "+decimal.format(total));
					
						model.addAttribute("mostraServicos", servicos);
		}
		
		return "view/ListaServicos";
	}
    
	
	 
	
	//Método para filtrar, soma os servicos pelas datas de inicio e final, id do Usuário  
	@PostMapping("/buscaPorDatas")
	// @PreAuthorize("hasAuthority('admin')")
	public String listaServicosPorDatas(Servicos serv,@RequestParam(name="dataInicio" ,required = false )String dataI,@RequestParam(name="dataFinal",required = false)String dataF,ModelMap model) {
		 // Pegando email do usuário 
		 String email= Usuario.getEmailUsuario();
		
		  System.out.println(" aqui o email "+Usuario.getEmailUsuario());
		  
		  // Pegando id do usuário pelo email informado no paramentro
		  int id = repository.BuscaIdPeloEmail(email);
			System.out.println("aqui  id do usuário é = "+id);
			
			// buscando todos dados do usuário pelo id informa no paramentro
		    Usuario  us = repository.BuscaTodosDadosDoUsuarioPeloId(id);
		 System.out.println("O objeto é esse  "+us.getId());
		 
		 // Listando todos servicos pelo id do usuário  		 
			List<Servicos> todosServicos = repositoryServico.listaServicosPeloId(us);
			List<ClienteModel> clientes = clienteRp.listaClientesPeloIdUsuario(us);
			//List<Servicos> servicos = repositoryServico.listaServicosPelasDatas(dataI, dataF);
			// Convertendo sa datas de date para String para lista no banco de dados
			String dataInicio=dataConvertida(dataI);   String dataFinal=dataConvertida(dataF);
			List<Servicos> servicosPorDatas = repositoryServico.listaServicosPelasDatasIdUsuario(dataInicio, dataFinal,us);
			
			// condição para saber se as tabelas ClienteModel e Servicos estão vazias
			if(todosServicos.isEmpty()&& servicosPorDatas ==null) {
				// Passando o resultado para decimal
				DecimalFormat decimal = new DecimalFormat("#,##0.00");
				double total=0;                   // exibindo o resultado
				model.addAttribute("msgListaTotal", "R$ "+decimal.format(total));
				// retornando para página de lista servicos
				return  "view/ListaServicos";
			}else if(repositoryServico.somamdoPorDatasIdUsuario(dataInicio,dataFinal,us)==null) {
				
				// Passando o resultado para decimal
				DecimalFormat decimal = new DecimalFormat("#,##0.00");
				double total=0;                   // exibindo o resultado
				model.addAttribute("msgListaTotal", "R$ "+decimal.format(total));
				// retornando para página de lista servicos
				return  "view/ListaServicos";
				
				
			}else {
				
				model.addAttribute("mostraServicos", servicosPorDatas);
				//Pegando toda soma dos servicos pelo id do usuário
				serv.setValorTotal(repositoryServico.somamdoPorDatasIdUsuario(dataInicio,dataFinal,us)); // somando a qtd
				// Passando o resultado para decimal
				DecimalFormat decimal = new DecimalFormat("#,##0.00");
				String total =decimal.format(serv.getValorTotal());
				// Passando valor total para se exibida na página html
				model.addAttribute("msgListaTotal", "R$ "+total);
			   

			}
			return  "view/ListaServicos";
				
	}
	
	
	
		//Método para lista todos serviços
	@GetMapping("/listaTodos")
	//@Transactional(readOnly = true)
	public String listaServicos( ModelMap model,Servicos serv) {
		
		  // Pegando email do usuário 
		 String email= Usuario.getEmailUsuario();
		// String email=Usuario.listaEmail.toString(); 
		  // Pegando id do usuário pelo email informado no paramentro
		  int id = repository.BuscaIdPeloEmail(email);
			System.out.println("aqui  id do usuário é = "+id);
		
			
			// buscando todos dados do usuário pelo id informa no paramentro
		     Usuario us = repository.BuscaTodosDadosDoUsuarioPeloId(id);
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
		if(todosServicos.isEmpty() && clientes.isEmpty()|| todosServicos.isEmpty()) {
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
	
	
	 // Método que convert a data de date para String de  trás para frente
	public String dataConvertida(String data) {
		// variaveis do tipo String  para armazenar os caracteres unico e específicos
		String dataConvert="",caracteres="",p0="",p1="",p2="",p3="",p4="",p5="",p6="",p7="",p8="",p9="";
	   // variaveis do tipo char para armazenar cada caracter específicos
		char i0,i1,i2,i3,i4,i5,i6,i7,i8,i9;
	   //	  String test="2023/04/20";
		// Lista o tamanho do atributo data que vem do paramentro
		int tamanho=data.length();
		  // Uma for para percorrer todo tamanho do atributo e lista os caracteres específicos 
		  for(int i=0;i<tamanho;i++) {
			  char caracter=data.charAt(i);
			  // as condições para pegar cada caracter 
			  if(i==9) {
				  i9=caracter;
				  caracteres=String.valueOf(i9);
				  p9=caracteres;
				///  System.out.print(" index 9 "+p9);
				  
			  }
			  if(i==8) {
				  i8=caracter;
				  caracteres=String.valueOf(i8);
				  p8=caracteres;
				//  System.out.print(" index 8 "+p8);
			  }
			  if(i==7) {
				  i7=caracter;
				  caracteres=String.valueOf(i7);
				  p7=caracteres.replace("-", "/");
				//  System.out.print(" index 7 "+p7);
			  }
			  if(i==6) {
				  i6=caracter;
				  caracteres=String.valueOf(i6);
				  p6=caracteres;
				//  System.out.print(" index 6 "+p6);
			  }
			  if(i==5) {
				  i5=caracter;
				  caracteres=String.valueOf(i5);
				  p5=caracteres;
				//  System.out.print(" index 5 "+p5);
			  }
			  if(i==4) {
				  i4=caracter;
				  caracteres=String.valueOf(i4);
				  p4=caracteres.replace("-", "/");
				// System.out.print(" index 4 "+p4);
			  }
			  if(i==3) {
				  i3=caracter;
				  caracteres=String.valueOf(i3);
				  p3=caracteres;
				//  System.out.print(" index 3 "+p3);
			  }
			  if(i==2) {
				  i2=caracter;
				  caracteres=String.valueOf(i2);
				  p2=caracteres;
				//  System.out.print("index 2 "+p2);
			  }
			  if(i==1) {
				  i1=caracter;
				  caracteres=String.valueOf(i1);
				  p1=caracteres;
				//  System.out.print("  index 1 "+p1);
			  }
			  if(i==0) {
				  i0=caracter;
				  caracteres=String.valueOf(i0);
				  p0=caracteres;
				//  System.out.print("  index 0 "+p0);
			  }
			  if(i==9) {
				//  System.out.println(" "+test+" Data convertida "+p8+p9+p7+p5+p6+p4+p0+p1+p2+p3);
				  dataConvert=p8+p9+p7+p5+p6+p4+p0+p1+p2+p3;
				 // System.out.println(" "+dataConvert);
			  }
		  }
		  // Retornando uma variável com todos caracteres  invertidos do tipo de dado date
		return  dataConvert;
	}
}
