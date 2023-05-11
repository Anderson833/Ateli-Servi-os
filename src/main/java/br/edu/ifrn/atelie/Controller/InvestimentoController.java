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
import br.edu.ifrn.atelie.Service.Ajustes;
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
	public String inicioInvestimento(ModelMap model, InvestimentoModel invest) {

		// buscando todos dados do usuário pelo id informa no paramentro
		Usuario us = Ajustes.idUsuarioAoLogar(repositoryUsuario);
		// Passando o objeto us de usuário para salva nos serviços
		invest.setUsuario(us);
		model.addAttribute("invest", invest);
		return "view/investimento";
	}

	// Método para excluir algum investimento
	@GetMapping("/excluir/{id}")
	@Transactional(readOnly = false)
	@PreAuthorize("hasAuthority('admin')")
	public String excluirInvestimento(@PathVariable("id") Integer id, InvestimentoModel invest,
			RedirectAttributes att) {
		repository.deleteById(id);

		att.addFlashAttribute("msgExcluirInvestimento", "Investimento excluído com Sucesso!");

		return "redirect:/Investimento/lista";
	}

	@GetMapping("/lista")
	public String listaInvestimento(ModelMap md, InvestimentoModel invest) {

		// buscando todos dados do usuário pelo id informa no paramentro
		Usuario us = Ajustes.idUsuarioAoLogar(repositoryUsuario);
		// Listando todos servicos pelo id do usuário
		List<InvestimentoModel> todosInvestimentos = repository.listaInvestimentoPeloId(us);

		// condição para saber se a tabela de investimento esta vazia
		if (todosInvestimentos.isEmpty() || todosInvestimentos == null) {
			// Passando o resultado para decimal
			DecimalFormat decimal = new DecimalFormat("#,##0.00");
			double total = 0; // exibindo o resultado
			md.addAttribute("TotalInvestimento", "R$ " + decimal.format(total));
			md.addAttribute("invest", repository.listaInvestimentoPeloId(us));
			// retornando para página de lista investimentos
			return "view/ListaInvestimento";
		} else {
			// Pegando toda soma dos investimentos pelo id do usuário
			invest.setValorTotal(repository.soma(us));
			// Passando o resultado para decimal
			DecimalFormat decimal = new DecimalFormat("#,##0.00");
			String total = decimal.format(invest.getValorTotal());
			// Passando valor total para se exibida na página html
			md.addAttribute("TotalInvestimento", "R$ " + total);
			// Passando todos os objetos para a página de lista despesas
			md.addAttribute("invest", repository.listaInvestimentoPeloId(us));
			return "view/listaInvestimento";
		}

	}

	// Método para lista e soma os investimentos por id do usuário e nome do Produto
	public String listaSomaInvestimentoIdUsuarioProduto(ModelMap md, Usuario us, InvestimentoModel invest,
			String produto) {

		// Listando todos servicos pelo id do usuário
		List<InvestimentoModel> listaSomaInvestimentosUsuarioCliente = repository

				.listaInvestimentoPeloIdUsuarioNomeProduto(us, produto);
		if (listaSomaInvestimentosUsuarioCliente == null) {
			// Passando o resultado para decimal
			DecimalFormat decimal = new DecimalFormat("#,##0.00");
			double total = 0; // exibindo o resultado
			md.addAttribute("TotalInvestimento", "R$ " + decimal.format(total));
			md.addAttribute("invest", listaSomaInvestimentosUsuarioCliente);
			// retornando para página de lista investimentos
			return "view/LstaInvestimento";

		} else if (produto.isEmpty()) {
			// Passando o resultado para decimal
			DecimalFormat decimal = new DecimalFormat("#,##0.00");
			double total = 0; // exibindo o resultado
			md.addAttribute("TotalInvestimento", "R$ " + decimal.format(total));

		} else if (repository.somaPorIdUsuarioNomeProduto(us, produto) == null) {
			// Passando o resultado para decimal
			DecimalFormat decimal = new DecimalFormat("#,##0.00");
			double total = 0;// exibindo o resultado
			md.addAttribute("TotalInvestimento", "R$ " + decimal.format(total));
			md.addAttribute("invest", repository.listaInvestimentoPeloIdUsuarioNomeProduto(us, produto));
			// retornando para página de lista investimentos
			return "view/ListaInvestimento";
		} else {
			// Pegando toda soma dos investimentos pelo id do usuário
			invest.setValorTotal(repository.somaPorIdUsuarioNomeProduto(us, produto));
			// Passando o resultado para decimal
			DecimalFormat decimal = new DecimalFormat("#,##0.00");
			String total = decimal.format(invest.getValorTotal());
			// Passando valor total para se exibida na página html
			md.addAttribute("TotalInvestimento", "R$ " + total);
			// Passando todos os objetos para a página de lista despesas
			md.addAttribute("invest", repository.listaInvestimentoPeloIdUsuarioNomeProduto(us, produto));

		}
		return "view/listaInvestimento";
	}

	// Método para lista e soma por id do usuário e as datas
	public String listaSomaInvestimentoIdUsuarioDatas(ModelMap md, Usuario us, InvestimentoModel invest,
			@RequestParam(name = "dataInicio", required = false) String datai,
			@RequestParam(name = "dataFinal", required = false) String dataf) {

		System.out.println("O objeto é esse  " + us.getId());
		// Convertendo as datas
		String dataComenco = Ajustes.dataConvertida(datai);
		String dataFim = Ajustes.dataConvertida(dataf);
		// Listando todos servicos pelo id do usuário e as datas
		List<InvestimentoModel> listaInvestimentosUsuarioDatas = repository.listaInvestimentoPeloIdUsuarioDatas(us,
				dataComenco, dataFim);

		if (listaInvestimentosUsuarioDatas == null) {
			// Passando o resultado para decimal
			DecimalFormat decimal = new DecimalFormat("#,##0.00");
			double total = 0; // exibindo o resultado
			md.addAttribute("TotalInvestimento", "R$ " + decimal.format(total));
			md.addAttribute("invest", listaInvestimentosUsuarioDatas);
			// retornando para página de lista investimentos
			return "view/LstaInvestimento";

		} else if (dataComenco.isEmpty() && dataFim.isEmpty()) {
			// Passando o resultado para decimal
			DecimalFormat decimal = new DecimalFormat("#,##0.00");
			double total = 0; // exibindo o resultado
			md.addAttribute("TotalInvestimento", "R$ " + decimal.format(total));

		} else if (repository.somaInvestimentoPeloIdUsuarioDatas(us, dataComenco, dataFim) == null) {
			// Passando o resultado para decimal
			DecimalFormat decimal = new DecimalFormat("#,##0.00");
			double total = 0;// exibindo o resultado
			md.addAttribute("TotalInvestimento", "R$ " + decimal.format(total));
			md.addAttribute("invest", listaInvestimentosUsuarioDatas);
			// retornando para página de lista investimentos
			return "view/ListaInvestimento";
		} else {
			// Pegando toda soma dos investimentos pelo id do usuário
			invest.setValorTotal(repository.somaInvestimentoPeloIdUsuarioDatas(us, dataComenco, dataFim));
			// Passando o resultado para decimal
			DecimalFormat decimal = new DecimalFormat("#,##0.00");
			String total = decimal.format(invest.getValorTotal());
			// Passando valor total para se exibida na página html
			md.addAttribute("TotalInvestimento", "R$ " + total);
			// Passando todos os objetos para a página de lista despesas
			md.addAttribute("invest", listaInvestimentosUsuarioDatas);

		}
		return "view/listaInvestimento";
	}

	// Método para lista os investimentos pelo id do usuario, nome do produto, e as
	// datas
	public String listaSomaInvestimentoIdUsuarioProdutoDatas(ModelMap md, Usuario us, InvestimentoModel invest,
			String produto, String datai, String dataf) {

		// Convertendo as datas de date para String
		String datainicio = Ajustes.dataConvertida(datai);
		String datafinal = Ajustes.dataConvertida(dataf);
		// Listando todos servicos pelo id do usuário
		List<InvestimentoModel> listaInvestimentoIdUsuarioProdutoDatas = repository
				.listaInvestimentoPeloIdUsuarioProdutoDatas(us, produto, datainicio, datafinal);

		if (listaInvestimentoIdUsuarioProdutoDatas == null) {
			// Passando o resultado para decimal
			DecimalFormat decimal = new DecimalFormat("#,##0.00");
			double total = 0; // exibindo o resultado
			md.addAttribute("TotalInvestimento", "R$ " + decimal.format(total));
			md.addAttribute("invest", listaInvestimentoIdUsuarioProdutoDatas);
			// retornando para página de lista investimentos
			return "view/LstaInvestimento";

		} else if (produto.isEmpty() && datainicio.isEmpty() && datafinal.isEmpty()) {
			// Passando o resultado para decimal
			DecimalFormat decimal = new DecimalFormat("#,##0.00");
			double total = 0; // exibindo o resultado
			md.addAttribute("TotalInvestimento", "R$ " + decimal.format(total));

		} else if (repository.somaInvestimentoPeloIdUsuarioProdutoDatas(us, produto, datainicio, datafinal) == null) {
			// Passando o resultado para decimal
			DecimalFormat decimal = new DecimalFormat("#,##0.00");
			double total = 0;// exibindo o resultado
			md.addAttribute("TotalInvestimento", "R$ " + decimal.format(total));
			md.addAttribute("invest", listaInvestimentoIdUsuarioProdutoDatas);
			// retornando para página de lista investimentos
			return "view/ListaInvestimento";
		} else {
			// Pegando toda soma dos investimentos pelo id do usuário
			invest.setValorTotal(
					repository.somaInvestimentoPeloIdUsuarioProdutoDatas(us, produto, datainicio, datafinal));
			// Passando o resultado para decimal
			DecimalFormat decimal = new DecimalFormat("#,##0.00");
			String total = decimal.format(invest.getValorTotal());
			// Passando valor total para se exibida na página html
			md.addAttribute("TotalInvestimento", "R$ " + total);
			// Passando todos os objetos para a página de lista despesas
			md.addAttribute("invest", listaInvestimentoIdUsuarioProdutoDatas);

		}
		return "view/listaInvestimento";
	}

	@PostMapping("/lista/Produto/Datas")
	public String listaSomaInvestimentoPorOpcoes(ModelMap md, InvestimentoModel invest,
			@RequestParam(name = "listaprodutos", required = false) String produto,
			@RequestParam(name = "dataInicio", required = false) String datai,
			@RequestParam(name = "dataFinal", required = false) String dataf) {

		// Atenção Muito cuidado antes de excluir ou fazer alteração nesse método!
		// Esse id desse usuário vai para todos os métodos que receber um objeto do tipo
		// usuário como paramêtros
		Usuario us = Ajustes.idUsuarioAoLogar(repositoryUsuario);
		if (produto.equalsIgnoreCase("lista")) {
			return "redirect:/Investimento/lista";
		} else if (!produto.isEmpty()) {
			listaSomaInvestimentoIdUsuarioProduto(md, us, invest, produto);
		} else if (produto.isEmpty() && !datai.isEmpty() && !dataf.isEmpty()) {
			listaSomaInvestimentoIdUsuarioDatas(md, us, invest, datai, dataf);
		} else {
			listaSomaInvestimentoIdUsuarioProdutoDatas(md, us, invest, produto, datai, dataf);
		}

		return "view/listaInvestimento";
	}

	@PostMapping("/inserir")
	public String inserirInvestimento(InvestimentoModel invest, RedirectAttributes att) {
		
		// Convertendo a data de date para String antes de salva no banco de dados
		String Converterdata =Ajustes.dataConvertida(invest.getData());
		invest.setData(Converterdata);
		repository.save(invest);
		att.addFlashAttribute("msgInvestimento", "Investimento inserido com sucesso!");
		// return "view/investimento";
		return "redirect:/Investimento/investir";
	}

	// Método para altera ou editar os dados
	@GetMapping("/editar/{id}")
	@PreAuthorize("hasAuthority('admin')")
	public String editarInvestimento(@PathVariable("id") int idinvest, ModelMap md) {

		// Passando os objetos para a página de investimento
		md.addAttribute("invest", repository.findById(idinvest));
		// deletando um investimento
		repository.deleteById(idinvest);
		// retornando a ação para página de investimento
		return "view/Investimento";

	}
}
