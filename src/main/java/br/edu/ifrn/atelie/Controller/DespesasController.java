package br.edu.ifrn.atelie.Controller;

import java.text.DecimalFormat;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.edu.ifrn.atelie.Modelo.ClienteModel;
import br.edu.ifrn.atelie.Modelo.Despesa;
import br.edu.ifrn.atelie.Modelo.Servicos;
import br.edu.ifrn.atelie.Modelo.Usuario;
import br.edu.ifrn.atelie.Repository.DespesaRepository;
import br.edu.ifrn.atelie.Repository.UsuarioRepository;

@Controller
@RequestMapping("/despesas")
public class DespesasController {

	@Autowired
	private DespesaRepository RepositoryDesp;

	@Autowired
	private UsuarioRepository repositoryUsuario;

	@GetMapping("/inicio")
	public String viewDespesa(ModelMap model, Despesa desp) {
		// Pegando email do usuário
		String email = Usuario.getEmailUsuario();
		System.out.println(" aqui o email " + Usuario.getEmailUsuario());
		// Pegando id do usuário pelo email informado no paramentro
		int id = repositoryUsuario.BuscaIdPeloEmail(email);
		System.out.println("aqui  id do usuário é = " + id);

		// buscando todos dados do usuário pelo id informa no paramentro
		Usuario us = repositoryUsuario.BuscaTodosDadosDoUsuarioPeloId(id);
		System.out.println("O objeto é esse  " + us.getId());

		// Passando o objeto us de usuário para salva nos serviços
		desp.setUsuario(us);
		System.out.println(" o ID do objeto " + desp.getUsuario());
		// Passando o objeto para exibir na página html de cadastro de servicos
		model.addAttribute("despesa", desp);
		// model.addAttribute("despesa",new Despesa());
		return "view/despesa";
	}

	@PostMapping("/salvar")
	public String salvarDespesas(Despesa desp, RedirectAttributes rd) {
        
		// Convertendo as datas
		String datainicio = dataConvertida(desp.getData());
		String datafinal = dataConvertida(desp.getData());
		desp.setData(datainicio);
		desp.setData(datafinal);
		RepositoryDesp.save(desp);
		rd.addFlashAttribute("msgDespesa", "Dados salvo com sucesso!");
		return "redirect:/despesas/inicio";

	}

	// Método para deletar uma despesa
	@GetMapping("/excluir/{id}")
	@Transactional(readOnly = false)
	@PreAuthorize("hasAuthority('admin')")
	public String excluirDespesa(@PathVariable("id") Integer id, Despesa despesa, RedirectAttributes att) {
		RepositoryDesp.delete(despesa);

		att.addFlashAttribute("msgDeletaDespesa", "Despesa excluída com Sucesso!");

		return "redirect:/despesas/lista";
	}

	// Método para lista e soma as despesas pelo id do usuário
	@GetMapping("/lista")
	public String listaDespesasUsuario(ModelMap md, Despesa desp) {
		// Pegando email do usuário
		String email = Usuario.getEmailUsuario();
		System.out.println(" aqui o email " + Usuario.getEmailUsuario());
		// Pegando id do usuário pelo email informado no paramentro
		int id = repositoryUsuario.BuscaIdPeloEmail(email);
		System.out.println("aqui  id do usuário é = " + id);

		// buscando todos dados do usuário pelo id informa no paramentro
		Usuario us = repositoryUsuario.BuscaTodosDadosDoUsuarioPeloId(id);
		System.out.println("O objeto é esse  " + us.getId());

		// Listando todas despesas pelo id do usuário
		List<Despesa> todasDespesas = RepositoryDesp.listaDespesasPeloIdUsuario(us);

		// condição para saber se a tabela de despesa esta vazia
		if (todasDespesas.isEmpty() || todasDespesas == null) {
			// Passando o resultado para decimal
			DecimalFormat decimal = new DecimalFormat("#,##0.00");
			double total = 0; // exibindo o resultado
			md.addAttribute("TotalDespesas", "R$ " + decimal.format(total));
			md.addAttribute("despesa", RepositoryDesp.listaDespesasPeloIdUsuario(us));
			// retornando para página de lista despesas
			return "view/ListaDespesas";
		} else {
			// Pegando toda soma das despesas pelo id do usuário
			desp.setValorTotal(RepositoryDesp.somaDespesasPorIdUsuario(us)); // somando a qtd
			// Passando o resultado para decimal
			DecimalFormat decimal = new DecimalFormat("#,##0.00");
			String total = decimal.format(desp.getValorTotal());
			// Passando valor total para se exibida na página html
			md.addAttribute("TotalDespesas", "R$ " + total);
			// Passando todos os objetos para a página de lista despesas
			md.addAttribute("despesa", RepositoryDesp.listaDespesasPeloIdUsuario(us));
			return "view/listaDespesas";
		}

	}

	// Método para editar despesa
	@GetMapping("/editar/{id}")
	@PreAuthorize("hasAuthority('admin')")
	public String editarDespesa(@PathVariable("id") int idDespesa, ModelMap md, RedirectAttributes att) {
		// buscando pelo id da despesa
		// Optional<Servicos> findById = RepositoryDesp.findById(idDespesa);
		// deletando o tipo de serviço que tem esse tipo de id para adicionar outro novo
		// Passando os objetos para a página de serviços
		md.addAttribute("despesa", RepositoryDesp.findById(idDespesa));
		RepositoryDesp.deleteById(idDespesa);
		// retornando a ação para página de seviços
		return "view/despesa";
	}

	// Método para lista e soma as despesas por id do usuário e descrição
	public String listaSomaDespesasDescricao(ModelMap md, Despesa desp, String descricao) {
		// Pegando email do usuário
		String email = Usuario.getEmailUsuario();
		System.out.println(" aqui o email " + Usuario.getEmailUsuario());
		// Pegando id do usuário pelo email informado no paramentro
		int id = repositoryUsuario.BuscaIdPeloEmail(email);
		System.out.println("aqui  id do usuário é = " + id);

		// buscando todos dados do usuário pelo id informa no paramentro
		Usuario us = repositoryUsuario.BuscaTodosDadosDoUsuarioPeloId(id);
		System.out.println("O objeto é esse  " + us.getId());

		// Listando todas despesas pelo id do usuário e a descrição
		List<Despesa> listaDespesaDescricao = RepositoryDesp.listaDespesasPeloIdUsuarioDescricao(us, descricao);

		if (descricao == null) {
			// Passando o resultado para decimal
			DecimalFormat decimal = new DecimalFormat("#,##0.00");
			double total = 0; // exibindo o resultado
			md.addAttribute("TotalDespesas", "R$ " + decimal.format(total));
			// retornando para página de lista despesas
			return "view/ListaDespesas";
		}else
		// condição para saber se a tabela de despesa esta vazia
		if (listaDespesaDescricao == null) {
			// Passando o resultado para decimal
			DecimalFormat decimal = new DecimalFormat("#,##0.00");
			double total = 0; // exibindo o resultado
			md.addAttribute("TotalDespesas", "R$ " + decimal.format(total));
			// retornando para página de lista despesas
			return "view/ListaDespesas";
		} else if (RepositoryDesp.somaDespesasPorIdUsuarioDescricao(us, descricao) == null) {
			// Passando o resultado para decimal
			DecimalFormat decimal = new DecimalFormat("#,##0.00");
			double total = 0; // exibindo o resultado
			md.addAttribute("TotalDespesas", "R$ " + decimal.format(total));
			md.addAttribute("despesa", listaDespesaDescricao);
			// retornando para página de lista despesas
			return "view/ListaDespesas";
		} else {
			// Pegando toda soma das despesas pelo id do usuário
			desp.setValorTotal(RepositoryDesp.somaDespesasPorIdUsuarioDescricao(us, descricao)); // somando a qtd
			// Passando o resultado para decimal
			DecimalFormat decimal = new DecimalFormat("#,##0.00");
			String total = decimal.format(desp.getValorTotal());
			// Passando valor total para se exibida na página html
			md.addAttribute("TotalDespesas", "R$ " + total);
			// Passando todos os objetos para a página de lista despesas
			md.addAttribute("despesa", listaDespesaDescricao);
		}
		return "view/ListaDespesas";
	}

	// Método para lista e soma as despesas pelas datas
	public String listaSomaDespesasPelasDatas(ModelMap md, Despesa desp,String datai,String dataf) {

		// Pegando email do usuário
		String email = Usuario.getEmailUsuario();
		System.out.println(" aqui o email " + Usuario.getEmailUsuario());
		// Pegando id do usuário pelo email informado no paramentro
		int id = repositoryUsuario.BuscaIdPeloEmail(email);
		System.out.println("aqui  id do usuário é = " + id);

		// buscando todos dados do usuário pelo id informa no paramentro
		Usuario us = repositoryUsuario.BuscaTodosDadosDoUsuarioPeloId(id);
		System.out.println("O objeto é esse  " + us.getId());
		// Convertendo as datas
		String datainicio = dataConvertida(datai);
		String datafinal = dataConvertida(dataf);
		// Listando todas despesas pelo id do usuário e as datas
		List<Despesa> listaDespesaPorDatas = RepositoryDesp.listaDespesasPeloIdUsuarioEntreDatas(us, datainicio, datafinal);

		// condição para saber se a tabela de despesa esta vazia
		if (listaDespesaPorDatas == null) {
			// Passando o resultado para decimal
			DecimalFormat decimal = new DecimalFormat("#,##0.00");
			double total = 0; // exibindo o resultado
			md.addAttribute("TotalDespesas", "R$ " + decimal.format(total));
			md.addAttribute("despesa", listaDespesaPorDatas);
			// retornando para página de lista despesas
			return "view/ListaDespesas";
		} else if (RepositoryDesp.somaDespesasPorIdUsuarioEntreDatas(us, datainicio, datafinal) == null) {
			// Passando o resultado para decimal
			DecimalFormat decimal = new DecimalFormat("#,##0.00");
			double total = 0; // exibindo o resultado
			md.addAttribute("TotalDespesas", "R$ " + decimal.format(total));
			md.addAttribute("despesa", listaDespesaPorDatas);
			// retornando para página de lista despesas
			return "view/ListaDespesas";
		} else {
			// Pegando toda soma das despesas pelo id do usuário e as datas
			desp.setValorTotal(RepositoryDesp.somaDespesasPorIdUsuarioEntreDatas(us, datainicio, datafinal)); // somando a qtd
			// Passando o resultado para decimal
			DecimalFormat decimal = new DecimalFormat("#,##0.00");
			String total = decimal.format(desp.getValorTotal());
			// Passando valor total para se exibida na página html
			md.addAttribute("TotalDespesas", "R$ " + total);
			// Passando todos os objetos para a página de lista despesas
			md.addAttribute("despesa", listaDespesaPorDatas);
		}

		return "view/ListaDespesas";
	}

	// Método para lista e soma as despesas por varias opções, descrição, entre as datas
	@PostMapping("/descricao/datas")
	public String listaSomaDespesasPorOpcoes(ModelMap md, Despesa desp, @RequestParam(name="descricao",required = false) String descricao,
			@RequestParam(name="datai",required = false) String datai, @RequestParam(name="dataf",required = false) String dataf) {
	   
		if (!descricao.isEmpty()&& datai.isEmpty() && dataf.isEmpty()) {
			listaSomaDespesasDescricao(md, desp, descricao);
		} else if (!datai.isEmpty() && !dataf.isEmpty() && descricao.isEmpty()) {
			listaSomaDespesasPelasDatas(md, desp, datai, dataf);
		}else {
	    listaSomaDespesasPorTodasOpcaos(md, desp, descricao, datai, dataf);
		}
		return "view/ListaDespesas";
		
	}

	 //método para lista e soma as despesas por todas as opções
	public String listaSomaDespesasPorTodasOpcaos(ModelMap md, Despesa desp,
			 String descricao,String datai,String dataf) {

		// Pegando email do usuário
		String email = Usuario.getEmailUsuario();
		System.out.println(" aqui o email " + Usuario.getEmailUsuario());
		// Pegando id do usuário pelo email informado no paramentro
		int id = repositoryUsuario.BuscaIdPeloEmail(email);
		System.out.println("aqui  id do usuário é = " + id);

		// buscando todos dados do usuário pelo id informa no paramentro
		Usuario us = repositoryUsuario.BuscaTodosDadosDoUsuarioPeloId(id);
		System.out.println("O objeto é esse  " + us.getId());
		// Convertendo as datas
		String datainicio = dataConvertida(datai);
		String datafinal = dataConvertida(dataf);
		
		// Listando todas despesas pelo id do usuário e as datas
		List<Despesa> listaDespesaPorCompleto = RepositoryDesp.listaDespesasPeloIdUsuarioDescricaoDatas(us, descricao,
				datainicio, datafinal);

		// condição para saber se a tabela de despesa esta vazia
		if (listaDespesaPorCompleto == null) {
			// Passando o resultado para decimal
			DecimalFormat decimal = new DecimalFormat("#,##0.00");
			double total = 0; // exibindo o resultado
			md.addAttribute("TotalDespesas", "R$ " + decimal.format(total));
			md.addAttribute("despesa", listaDespesaPorCompleto);
			// retornando para página de lista despesas
			return "view/ListaDespesas";
		} else if (RepositoryDesp.somaDespesasPorIdUsuarioDescricaoDatas(us, descricao, datainicio, datafinal) == null) {
			// Passando o resultado para decimal
			DecimalFormat decimal = new DecimalFormat("#,##0.00");
			double total = 0; // exibindo o resultado
			md.addAttribute("TotalDespesas", "R$ " + decimal.format(total));
			md.addAttribute("despesa", listaDespesaPorCompleto);
			// retornando para página de lista despesas
			return "view/ListaDespesas";
		} else {
			// Pegando toda soma das despesas pelo id do usuário
			desp.setValorTotal(RepositoryDesp.somaDespesasPorIdUsuarioDescricaoDatas(us, descricao, datainicio, datafinal)); // somando																								// qtd
			// Passando o resultado para decimal
			DecimalFormat decimal = new DecimalFormat("#,##0.00");
			String total = decimal.format(desp.getValorTotal());
			// Passando valor total para se exibida na página html
			md.addAttribute("TotalDespesas", "R$ " + total);
			// Passando todos os objetos para a página de lista despesas
			md.addAttribute("despesa", listaDespesaPorCompleto);
		}

		return "view/ListaDespesas";
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
}
