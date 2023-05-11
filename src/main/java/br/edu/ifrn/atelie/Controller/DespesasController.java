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
import br.edu.ifrn.atelie.Service.Ajustes;

@Controller
@RequestMapping("/despesas")
public class DespesasController {

	@Autowired
	private DespesaRepository RepositoryDesp;

	@Autowired
	private UsuarioRepository repositoryUsuario;

	@GetMapping("/inicio")
	public String viewDespesa(ModelMap model, Despesa desp) {
		
		// Passando um método que contém o id do usuário para se usado nos outros métodos
		Usuario us = Ajustes.idUsuarioAoLogar(repositoryUsuario);
		// Passando o objeto us de usuário para salva os serviços
		desp.setUsuario(us);
		// Passando o objeto para exibir na página html de cadastro de servicos
		model.addAttribute("despesa", desp);
		return "view/despesa";
	}

	@PostMapping("/salvar")
	public String salvarDespesas(Despesa desp, RedirectAttributes rd) {
        
		// Convertendo as datas
		String datainicio = Ajustes.dataConvertida(desp.getData());
		String datafinal = Ajustes.dataConvertida(desp.getData());
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
		

		// Passando um método que contém o id do usuário para se usado nos parâmetros
		Usuario us = Ajustes.idUsuarioAoLogar(repositoryUsuario);

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
		
		Usuario us = Ajustes.idUsuarioAoLogar(repositoryUsuario);
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
        
		// Passando o id do usuário para variavel us do tipo usuario
		Usuario us =Ajustes.idUsuarioAoLogar(repositoryUsuario);
		// Convertendo as datas
		String datainicio = Ajustes.dataConvertida(datai);
		String datafinal = Ajustes.dataConvertida(dataf);
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
	    if(descricao.equalsIgnoreCase("lista")) {
	    	return "redirect:/despesas/lista";
	    } else if (!descricao.isEmpty()&& datai.isEmpty() && dataf.isEmpty()) {
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

		// Passando o id do usuário para variavel us do tipo usuario
		 Usuario us =Ajustes.idUsuarioAoLogar(repositoryUsuario);
		// Convertendo as datas
		String datainicio = Ajustes.dataConvertida(datai);
		String datafinal = Ajustes.dataConvertida(dataf);
		
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

}
