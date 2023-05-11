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
import br.edu.ifrn.atelie.Service.Ajustes;

//Essa classe vai controlar as requisições feitas para serviços 

@Controller
@RequestMapping("/servicos") // Url de inicio para unir com as específicas
public class ServicosController {

	// realizando a instância da classe Servicos
	@Autowired
	private ServicosRepository repositoryServico;

	@Autowired
	private ClienteRepository clienteRp;

	@Autowired
	private CalculorRepository repositoryCal;

	@Autowired
	private UsuarioRepository repository;

	// método para abrir a página de serviços e passa os objetos de serviços
	@GetMapping("/atividades")
	public String tarefas(ModelMap model, Servicos serv) {
		// Passando o objeto do tipo usuário para salvar os serviços
		serv.setUsuario(Ajustes.idUsuarioAoLogar(repository));
		System.out.println(" o ID do objeto " + serv.getUsuario());
		// Passando o objeto para exibir na página html de cadastro de servicos
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
		md.addAttribute("tarefas", findById);
		// retornando a ação para página de seviços
		return "view/tarefas";
	}

	// método para adicionar um serviço ou mais para os clientes
	@PostMapping("/adicionar")
	@Transactional(readOnly = false)
	@PreAuthorize("hasAuthority('admin')")
	public String adicionarServicos(Servicos serv, ModelMap md, RedirectAttributes att,
			@RequestParam(name = "nome", required = false) String nomeCliente) {

		List<ClienteModel> cliente = clienteRp.listaClientePeloNome(nomeCliente);
		// condição para lista tudo
		if (cliente.isEmpty() || cliente == null) {
			att.addFlashAttribute("msgsucesso", "Não tem esse cliente " + nomeCliente + " cadastrado!");
			return "redirect:/servicos/atividades";
		} else {
			// Convertendo a data de date para String e salva no banco de dados como varchar
			String dataConvertida = Ajustes.dataConvertida(serv.getData());
			// setando a data convertida
			serv.setData(dataConvertida);
			att.addFlashAttribute("msgsucesso", "Operação Realizada Com Sucesso! ");

			// já salvar no banco de dados
			repositoryServico.save(serv);
		}

		return "redirect:/servicos/atividades";
	}

	// método para deleta todos os registro
	@GetMapping("/deletaTudo")
	@PreAuthorize("hasAuthority('admin')")
	public String deletaTodosServicos(Servicos sv) {

		repositoryServico.deleteAllInBatch();

		return "view/ListaServicos";
	}

	// Método para deletar um serviços
	@GetMapping("/excluir/{id}")
	@Transactional(readOnly = false)
	@PreAuthorize("hasAuthority('admin')")
	public String excluirServicos(@PathVariable("id") Integer id, Servicos sv, RedirectAttributes att) {
		repositoryServico.delete(sv);

		att.addFlashAttribute("msgError", "Tipo de serviço excluído com Sucesso!");

		return "redirect:/servicos/listaTodos";
	}

	// Método para filtrar os servicos pelo nome do cliente
	@GetMapping("listaPorCliente")
	@Transactional(readOnly = true)
	@PreAuthorize("hasAuthority('admin')")
	public String listaSomaServicosPeloNomeCliente(Usuario us, String nomeCliente, ModelMap model) {

		List<Servicos> servicosListaNomeCliente = repositoryServico.listaServicosPeloIdUsuarioNomeCliente(us,
				nomeCliente);

		// condição para lista tudo
		if (servicosListaNomeCliente == null) {
			// Passando o resultado para decimal
			DecimalFormat decimal = new DecimalFormat("#,##0.00");
			double totalVazio = 0;// exibindo o resultado
			model.addAttribute("msgListaTotal", "R$ " + decimal.format(totalVazio));
			model.addAttribute("mostraServicos", servicosListaNomeCliente);
			return "view/ListaServicos";
		} else if (nomeCliente == null) {
			// Passando o resultado para decimal
			DecimalFormat decimal = new DecimalFormat("#,##0.00");
			double totalVazio = 0;// exibindo o resultado
			model.addAttribute("msgListaTotal", "R$ " + decimal.format(totalVazio));
			model.addAttribute("mostraServicos", servicosListaNomeCliente);
			System.out.println("nome nulo");
			return "view/ListaServicos";
		} else if (repositoryServico.somandoServicosPeloIdUsuarioNomeDoCliente(us, nomeCliente) == null) {
			// Passando o resultado para decimal
			DecimalFormat decimal = new DecimalFormat("#,##0.00");
			double totalZero = 0; // exibindo o resultado
			model.addAttribute("msgListaTotal", "R$ " + decimal.format(totalZero));
			model.addAttribute("mostraServicos", servicosListaNomeCliente);
			System.out.println("valor nulo");
			return "view/ListaServicos";
		} else {
			// Passando o resultado para decimal
			DecimalFormat decimal = new DecimalFormat("#,##0.00");
			double total = repositoryServico.somandoServicosPeloIdUsuarioNomeDoCliente(us, nomeCliente);
			// exibindo o resultado
			model.addAttribute("msgListaTotal", "R$ " + decimal.format(total));
			model.addAttribute("mostraServicos", servicosListaNomeCliente);
			System.out.println(" sem esta nulo");

		}
		return "view/ListaServicos";
	}

	// Método para lista e soma os serviços pelo id usuario e a descriçao
	public String listaSomaServicosPelaDescricao(Usuario us, String descricao, ModelMap model) {

		List<Servicos> listaServciosPelaDescricao = repositoryServico.listaServicosPeloIdUsuarioDescricao(us,
				descricao);

		// condição para lista tudo
		if (listaServciosPelaDescricao == null) {
			// Passando o resultado para decimal
			DecimalFormat decimal = new DecimalFormat("#,##0.00");
			double totalVazio = 0;// exibindo o resultado
			model.addAttribute("msgListaTotal", "R$ " + decimal.format(totalVazio));
			model.addAttribute("mostraServicos", listaSomaServicosPelaDescricao(us, descricao, model));
			return "view/ListaServicos";
		} else if (descricao == null) {
			// Passando o resultado para decimal
			DecimalFormat decimal = new DecimalFormat("#,##0.00");
			double totalVazio = 0;// exibindo o resultado
			model.addAttribute("msgListaTotal", "R$ " + decimal.format(totalVazio));
			model.addAttribute("mostraServicos", listaServciosPelaDescricao);
			return "view/ListaServicos";
		} else if (repositoryServico.somaServicosPeloIdUsuarioDescricao(us, descricao) == null) {
			// Passando o resultado para decimal
			DecimalFormat decimal = new DecimalFormat("#,##0.00");
			double totalZero = 0; // exibindo o resultado
			model.addAttribute("msgListaTotal", "R$ " + decimal.format(totalZero));
			model.addAttribute("mostraServicos", listaServciosPelaDescricao);
			return "view/ListaServicos";
		} else {
			// Passando o resultado para decimal
			DecimalFormat decimal = new DecimalFormat("#,##0.00");
			double total = repositoryServico.somaServicosPeloIdUsuarioDescricao(us, descricao);
			// exibindo o resultado
			model.addAttribute("msgListaTotal", "R$ " + decimal.format(total));
			model.addAttribute("mostraServicos", listaServciosPelaDescricao);
		}
		return "view/ListaServicos";
	}

	// Método para lista e soma os serviços pelo id usuario, descriçao e nome do
	// cliente
	public String listaSomaServicosPelaDescricaoCliente(Usuario us, String descricao, String nome, ModelMap model) {

		List<Servicos> listaServciosPelaDescricaoCliente = repositoryServico
				.listaServicosPeloIdUsuarioDescricaoCliente(us, descricao, nome);

		// condição para lista tudo
		if (listaServciosPelaDescricaoCliente == null) {
			// Passando o resultado para decimal
			DecimalFormat decimal = new DecimalFormat("#,##0.00");
			double totalVazio = 0;// exibindo o resultado
			model.addAttribute("msgListaTotal", "R$ " + decimal.format(totalVazio));
			model.addAttribute("mostraServicos", listaSomaServicosPelaDescricaoCliente(us, descricao, nome, model));
			return "view/ListaServicos";
		} else if (descricao == null) {
			// Passando o resultado para decimal
			DecimalFormat decimal = new DecimalFormat("#,##0.00");
			double totalVazio = 0;// exibindo o resultado
			model.addAttribute("msgListaTotal", "R$ " + decimal.format(totalVazio));
			model.addAttribute("mostraServicos", listaServciosPelaDescricaoCliente);
			return "view/ListaServicos";
		} else if (repositoryServico.somaServicosPeloIdUsuarioDescricaoCliente(us, descricao, nome) == null) {
			// Passando o resultado para decimal
			DecimalFormat decimal = new DecimalFormat("#,##0.00");
			double totalZero = 0; // exibindo o resultado
			model.addAttribute("msgListaTotal", "R$ " + decimal.format(totalZero));
			model.addAttribute("mostraServicos", listaServciosPelaDescricaoCliente);
			return "view/ListaServicos";
		} else {
			// Passando o resultado para decimal
			DecimalFormat decimal = new DecimalFormat("#,##0.00");
			double total = repositoryServico.somaServicosPeloIdUsuarioDescricaoCliente(us, descricao, nome);
			// exibindo o resultado
			model.addAttribute("msgListaTotal", "R$ " + decimal.format(total));
			model.addAttribute("mostraServicos", listaServciosPelaDescricaoCliente);
		}
		return "view/ListaServicos";
	}

	// Método para lista e soma os serviços por id usuário, descrição e entre as
	// datas
	public String listaSomaServicosPelaDescricaoClienteDatas(Usuario us, String descricao, String nome, String datai,
			String dataf, ModelMap model) {

		List<Servicos> listaServciosPelaDescricaoClienteDatas = repositoryServico
				.listaServicosPeloIdUsuarioDescricaoClienteDatas(us, descricao, nome, datai, dataf);

		// condição para lista tudo
		if (listaServciosPelaDescricaoClienteDatas == null) {
			// Passando o resultado para decimal
			DecimalFormat decimal = new DecimalFormat("#,##0.00");
			double totalVazio = 0;// exibindo o resultado
			model.addAttribute("msgListaTotal", "R$ " + decimal.format(totalVazio));
			model.addAttribute("mostraServicos",
					listaSomaServicosPelaDescricaoClienteDatas(us, descricao, nome, datai, dataf, model));
			return "view/ListaServicos";
		} else if (descricao == null) {
			// Passando o resultado para decimal
			DecimalFormat decimal = new DecimalFormat("#,##0.00");
			double totalVazio = 0;// exibindo o resultado
			model.addAttribute("msgListaTotal", "R$ " + decimal.format(totalVazio));
			model.addAttribute("mostraServicos", listaServciosPelaDescricaoClienteDatas);
			return "view/ListaServicos";
		} else if (repositoryServico.somaServicosPeloIdUsuarioDescricaoClienteDatas(us, descricao, nome, datai,
				dataf) == null) {
			// Passando o resultado para decimal
			DecimalFormat decimal = new DecimalFormat("#,##0.00");
			double totalZero = 0; // exibindo o resultado
			model.addAttribute("msgListaTotal", "R$ " + decimal.format(totalZero));
			model.addAttribute("mostraServicos", listaServciosPelaDescricaoClienteDatas);
			return "view/ListaServicos";
		} else {
			// Passando o resultado para decimal
			DecimalFormat decimal = new DecimalFormat("#,##0.00");
			double total = repositoryServico.somaServicosPeloIdUsuarioDescricaoClienteDatas(us, descricao, nome, datai,
					dataf);
			// exibindo o resultado
			model.addAttribute("msgListaTotal", "R$ " + decimal.format(total));
			model.addAttribute("mostraServicos", listaServciosPelaDescricaoClienteDatas);
		}
		return "view/ListaServicos";
	}

	// Método para filtrar, soma os servicos pelas datas de inicio e final, id do
	// Usuário
	@PostMapping("/buscaPorDatas")
	@Transactional(readOnly = true)
	@PreAuthorize("hasAuthority('admin')")
	public String listaServicosPorDatas(Usuario us, String dataI, String dataF, ModelMap model) {
		Servicos serv = new Servicos();
		// Listando todos servicos pelo id do usuário
		List<Servicos> todosServicos = repositoryServico.listaServicosPeloId(us);
		List<ClienteModel> clientes = clienteRp.listaClientesPeloIdUsuario(us);
		List<Servicos> servicosPorDatas = repositoryServico.listaServicosPelasDatasIdUsuario(dataI, dataF, us);

		// condição para saber se as tabelas ClienteModel e Servicos estão vazias
		if (todosServicos.isEmpty() && servicosPorDatas == null) {
			// Passando o resultado para decimal
			DecimalFormat decimal = new DecimalFormat("#,##0.00");
			double total = 0; // exibindo o resultado
			model.addAttribute("msgListaTotal", "R$ " + decimal.format(total));
			// retornando para página de lista servicos
			return "view/ListaServicos";
		} else if (repositoryServico.somamdoPorDatasIdUsuario(dataI, dataF, us) == null) {

			// Passando o resultado para decimal
			DecimalFormat decimal = new DecimalFormat("#,##0.00");
			double total = 0; // exibindo o resultado
			model.addAttribute("msgListaTotal", "R$ " + decimal.format(total));
			// retornando para página de lista servicos
			return "view/ListaServicos";

		} else {

			model.addAttribute("mostraServicos", servicosPorDatas);
			// Pegando toda soma dos servicos pelo id do usuário
			serv.setValorTotal(repositoryServico.somamdoPorDatasIdUsuario(dataI, dataF, us)); // somando a qtd
			// Passando o resultado para decimal
			DecimalFormat decimal = new DecimalFormat("#,##0.00");
			String total = decimal.format(serv.getValorTotal());
			// Passando valor total para se exibida na página html
			model.addAttribute("msgListaTotal", "R$ " + total);

		}
		return "view/ListaServicos";

	}

	// Método para lista todos serviços
	@GetMapping("/listaTodos")
	// @Transactional(readOnly = true)
	public String listaServicos(ModelMap model, Servicos serv) {

		// buscando todos dados do usuário pelo id informa no paramentro
		Usuario us = Ajustes.idUsuarioAoLogar(repository);
		// System.out.println("O objeto é esse " + us.getId());

		// Passando o objeto us de usuário para salva nos serviços
		serv.setUsuario(us);
		// System.out.println(" o ID do objeto " + serv.getUsuario());

		// Listando todos servicos pelo id do usuário
		List<Servicos> todosServicos = repositoryServico.listaServicosPeloId(us);
		List<ClienteModel> clientes = clienteRp.listaClientesPeloIdUsuario(us);

		// condição para saber se as tabelas ClienteModel e Servicos estão vazias
		if (todosServicos.isEmpty() && clientes.isEmpty() || todosServicos.isEmpty()) {
			// Passando o resultado para decimal
			DecimalFormat decimal = new DecimalFormat("#,##0.00");
			double total = 0; // exibindo o resultado
			model.addAttribute("msgListaTotal", "R$ " + decimal.format(total));
			// retornando para página de lista servicos
			return "view/ListaServicos";
		}
		// Pegando toda soma dos servicos pelo id do usuário
		serv.setValorTotal(repositoryServico.soma(us)); // somando a qtd
		// Passando o resultado para decimal
		DecimalFormat decimal = new DecimalFormat("#,##0.00");
		String total = decimal.format(serv.getValorTotal());
		// Passando valor total para se exibida na página html
		model.addAttribute("msgListaTotal", "R$ " + total);
		// Passando todos os objetos para a página de lista de serviços
		model.addAttribute("mostraServicos", todosServicos);
		// retornando para página de lista servicos
		return "view/ListaServicos";
	}

	// método para gerar um auto complete dos nomes dos clientes
	@GetMapping("/autocompleteClientes")
	@Transactional(readOnly = true)
	@ResponseBody
	public List<br.edu.ifrn.atelie.DTO.AutocompleteDTO> autocompleteVagas(@RequestParam("term") String termo) {

		List<ClienteModel> cliente = clienteRp.findByNome(termo);

		List<br.edu.ifrn.atelie.DTO.AutocompleteDTO> eficacia = new ArrayList<>();
		cliente.forEach(v -> eficacia.add(new br.edu.ifrn.atelie.DTO.AutocompleteDTO(v.getNome(), v.getId())));

		return eficacia;
	}

	// Método para lista e soma os serviços pelo di do usuário, nome do cliente, e
	// entre as datas
	public String listaSomaServicosTodasOpcoesMenosDescricao(Usuario us, String nomeCliente, String dataI, String dataF,
			ModelMap model) {

		Servicos serv = new Servicos();

		List<Servicos> servicosTodasOpcoes = repositoryServico.listaServicosPeloIdUsuarioNomeClienteDatas(us,
				nomeCliente, dataI, dataF);
		if (servicosTodasOpcoes == null) {
			// Passando o resultado para decimal
			DecimalFormat decimal = new DecimalFormat("#,##0.00");
			double total = 0; // exibindo o resultado
			model.addAttribute("msgListaTotal", "R$ " + decimal.format(total));
			System.out.println("sem dados dddd");
			// retornando para página de lista servicos
			return "view/ListaServicos";

		} else if (repositoryServico.SomaServicosPeloIdUsuarioNomeClienteDatas(us, nomeCliente, dataI, dataF) == null) {

			// Passando o resultado para decimal
			DecimalFormat decimal = new DecimalFormat("#,##0.00");
			double total = 0; // exibindo o resultado
			model.addAttribute("msgListaTotal", "R$ " + decimal.format(total));
			// repositoryServico.SomaServicosPeloIdUsuarioNomeClienteDatas(us, nomeCliente,
			// dataInicio, dataFinal);
			System.out.println(us + "id " + nomeCliente + " nome " + dataI + " dataI " + dataF + " DATA F sem soma "
					+ repositoryServico.SomaServicosPeloIdUsuarioNomeClienteDatas(us, nomeCliente, dataI, dataF));
			// retornando para página de lista servicos
			return "view/ListaServicos";
		} else if (repositoryServico.SomaServicosPeloIdUsuarioNomeClienteDatas(us, nomeCliente, dataI, dataF) != null) {
			// Passando o resultado para decimal
			DecimalFormat decimal = new DecimalFormat("#,##0.00");
			serv.setValorTotal(
					repositoryServico.SomaServicosPeloIdUsuarioNomeClienteDatas(us, nomeCliente, dataI, dataF));
			String total = decimal.format(serv.getValorTotal());
			// exibindo o resultado
			model.addAttribute("msgListaTotal", "R$ " + total);
			model.addAttribute("mostraServicos", servicosTodasOpcoes);
			System.out.println("sem opção");

		}
		return "view/ListaServicos";
	}

	@PostMapping("/lista/NomeCliente/EntreAsDatas")
	@Transactional(readOnly = true)
	public String listaSomaServicosOpcoes(Servicos serv,
			@RequestParam(name = "pesquisa", required = false) String nomeCliente,
			@RequestParam(name = "tiposervicos", required = false) String descricao,
			@RequestParam(name = "dataInicio", required = true) String dataI,
			@RequestParam(name = "dataFinal", required = true) String dataF, ModelMap md) {

		// Convertendo sa datas de date para String para lista no banco de dados
		//Atenção! Muito cuidado em excluir esse métodos de converter as datas, Por que  estão sendo convertidas somente nesse método e passadas como parâmetros para os demais método
		String dataInicio = Ajustes.dataConvertida(dataI);
		String dataFinal = Ajustes.dataConvertida(dataF);
	//   Atenção Muito cuidado antes de excluir ou fazer alteração nesse método! 
   //   Esse id desse usuário vai para todos os métodos que receber um  objeto do tipo usuário como paramêtros
		Usuario us = Ajustes.idUsuarioAoLogar(repository);

		if (nomeCliente.equalsIgnoreCase("lista")) {
			return "redirect:/servicos/listaTodos";
		} else if (!nomeCliente.isEmpty() && dataI.isEmpty() && dataF.isEmpty()) {
			listaSomaServicosPeloNomeCliente(us, nomeCliente, md);

		} else if (nomeCliente.isEmpty() && !dataInicio.isEmpty() && !dataFinal.isEmpty()) {
			listaServicosPorDatas(us, dataInicio, dataFinal, md);

		} else if (!descricao.isEmpty() && nomeCliente.isEmpty() && dataInicio.isEmpty() && dataFinal.isEmpty()) {
			listaSomaServicosPelaDescricao(us, descricao, md);

		} else if (!descricao.isEmpty() && !nomeCliente.isEmpty() && dataInicio.isEmpty() && dataFinal.isEmpty()) {
			listaSomaServicosPelaDescricaoCliente(us, descricao, nomeCliente, md);

		} else if (!descricao.isEmpty() && !nomeCliente.isEmpty() && !dataInicio.isEmpty() && !dataFinal.isEmpty()) {
			listaSomaServicosPelaDescricaoClienteDatas(us, descricao, nomeCliente, dataInicio, dataFinal, md);
		} else {
			listaSomaServicosTodasOpcoesMenosDescricao(us, nomeCliente, dataInicio, dataFinal, md);
		}

		return "view/ListaServicos";

	}

}
