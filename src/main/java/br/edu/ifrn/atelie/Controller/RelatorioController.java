package br.edu.ifrn.atelie.Controller;

import java.text.DecimalFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.edu.ifrn.atelie.Modelo.ClienteModel;
import br.edu.ifrn.atelie.Modelo.Despesa;
import br.edu.ifrn.atelie.Modelo.InvestimentoModel;
import br.edu.ifrn.atelie.Modelo.Servicos;
import br.edu.ifrn.atelie.Modelo.Usuario;
import br.edu.ifrn.atelie.Repository.DespesaRepository;
import br.edu.ifrn.atelie.Repository.InvestimentoRepository;
import br.edu.ifrn.atelie.Repository.ServicosRepository;
import br.edu.ifrn.atelie.Repository.UsuarioRepository;
import br.edu.ifrn.atelie.Service.Ajustes;

@Controller
public class RelatorioController {

	@Autowired
	private DespesaRepository RepositoryDesp;

	@Autowired
	private InvestimentoRepository repositoryInvest;

	@Autowired
	private UsuarioRepository repositoryUsuario;

	// realizando a instância da classe Servicos
	@Autowired
	private ServicosRepository repositoryServico;

	@GetMapping("/relatorio")
	public String relatorio(ModelMap md, Despesa desp, InvestimentoModel invest, Servicos serv) {
		

	    // Passando id do usuario para uma variável us do tipo usuario
		Usuario us = Ajustes.idUsuarioAoLogar(repositoryUsuario);
		// ações dos métodos nas linhas debaixo
		// Operação das despesas
		// Listando todas despesas pelo id do usuário
		double totalDespesa = 0, totalInvest = 0, totalServicos = 0;
		// As condições para saber se as coluna do valor toal de cada tabela no banco de dados estão vazias
		if (RepositoryDesp.somaDespesasPorIdUsuario(us) == null && repositoryInvest.soma(us) == null
				&& repositoryServico.soma(us) == null) {
			// Passando o resultado para decimal
			DecimalFormat decimal = new DecimalFormat("#,##0.00");
			// exibindo o resultado
			md.addAttribute("TotalDasDespesas", "R$ " + decimal.format(totalDespesa));
			md.addAttribute("TotalDosInvestimento", "R$ " + decimal.format(totalInvest));
			md.addAttribute("msgListaTotal", "R$ " + decimal.format(totalServicos));

			// exibindo os ganhos na página html
			md.addAttribute("TotalAntesDespesas", "R$ " + ganhosAntesDespesas(totalServicos, totalInvest));
			md.addAttribute("TotalPosDespesas", "R$ " + ganhosPosDespesas(totalServicos, totalInvest, totalDespesa));
			// retornando para página de lista despesas
			return "view/relatorio";

			// condição para despesas diferente de nulo e investimento, serviço nulos
		} else if (RepositoryDesp.somaDespesasPorIdUsuario(us) != null && repositoryInvest.soma(us) == null
				&& repositoryServico.soma(us) == null) {
			desp.setValorTotal(RepositoryDesp.somaDespesasPorIdUsuario(us)); // somando a qtd
			// Passando o resultado para decimal
			DecimalFormat decimal = new DecimalFormat("#,##0.00");
			String total = decimal.format(desp.getValorTotal());
			// Passando valor total para se exibida na página html
			md.addAttribute("TotalDasDespesas", "R$ " + total);
			md.addAttribute("TotalDosInvestimento", "R$ " + decimal.format(totalInvest));
			md.addAttribute("msgListaTotal", "R$ " + decimal.format(totalServicos));
			return "view/relatorio";

			// condição para só investimento sendo diferente de nulo
		} else if (RepositoryDesp.somaDespesasPorIdUsuario(us) == null && repositoryInvest.soma(us) != null
				&& repositoryServico.soma(us) == null) {
			// Pegando toda soma dos investimentos pelo id do usuário
			invest.setValorTotal(repositoryInvest.soma(us));
			// Passando o resultado para decimal
			DecimalFormat decimal = new DecimalFormat("#,##0.00");
			String totalInvestido = decimal.format(invest.getValorTotal());

			// Passando valor total para se exibida na página html
			md.addAttribute("TotalDasDespesas", "R$ " + decimal.format(totalDespesa));
			md.addAttribute("TotalDosInvestimento", "R$ " + totalInvestido);
			md.addAttribute("msgListaTotal", "R$ " + decimal.format(totalServicos));
			return "view/relatorio";

			// condição para só serviços é diferente de nulo
		} else if (RepositoryDesp.somaDespesasPorIdUsuario(us) == null && repositoryInvest.soma(us) == null
				&& repositoryServico.soma(us) != null) {
			// Pegando toda soma dos servicos pelo id do usuário
			serv.setValorTotal(repositoryServico.soma(us)); // somando a qtd
			// Passando o resultado para decimal
			DecimalFormat decimal = new DecimalFormat("#,##0.00");
			String totalservicos = decimal.format(serv.getValorTotal());

			// Passando valor total para se exibida na página html
			md.addAttribute("TotalDasDespesas", "R$ " + decimal.format(totalDespesa));
			md.addAttribute("TotalDosInvestimento", "R$ " + decimal.format(totalInvest));
			md.addAttribute("msgListaTotal", "R$ " + totalservicos);
			return "view/relatorio";

			// condição para despesa e investimento se é diferente de nulo e servicos ao
			// contrário
		} else if (RepositoryDesp.somaDespesasPorIdUsuario(us) != null && repositoryInvest.soma(us) != null
				&& repositoryServico.soma(us) == null) {
			desp.setValorTotal(RepositoryDesp.somaDespesasPorIdUsuario(us)); // somando a qtd
			// Passando o resultado para decimal
			DecimalFormat decimal = new DecimalFormat("#,##0.00");
			String total = decimal.format(desp.getValorTotal());

			// Pegando toda soma dos investimentos pelo id do usuário
			invest.setValorTotal(repositoryInvest.soma(us));
			// Passando o resultado para decimal
			String totalInvestimento = decimal.format(invest.getValorTotal());

			// Passando valor total para se exibida na página html
			md.addAttribute("TotalDasDespesas", "R$ " + total);
			md.addAttribute("TotalDosInvestimento", "R$ " + totalInvestimento);
			md.addAttribute("msgListaTotal", "R$ " + decimal.format(totalServicos));
			return "view/relatorio";

			// condição despesa sendo nulo investimento e servicos não
		} else if (RepositoryDesp.somaDespesasPorIdUsuario(us) == null && repositoryInvest.soma(us) != null
				&& repositoryServico.soma(us) != null) {
			// Passando o resultado para decimal
			DecimalFormat decimal = new DecimalFormat("#,##0.00");
			// Pegando toda soma dos investimentos pelo id do usuário
			invest.setValorTotal(repositoryInvest.soma(us));
			// Passando o resultado para decimal
			String totalInvestido = decimal.format(invest.getValorTotal());

			// Pegando toda soma dos servicos pelo id do usuário
			serv.setValorTotal(repositoryServico.soma(us)); // somando a qtd
			// Passando o resultado para decimal
			String totalServico = decimal.format(serv.getValorTotal());

			// Passando valor total para se exibida na página html
			md.addAttribute("TotalDosInvestimento", "R$ " + totalInvestido);
			md.addAttribute("TotalDasDespesas", "R$ " + decimal.format(totalDespesa));
			md.addAttribute("msgListaTotal", "R$ " + totalServico);
			return "view/relatorio";

			// condição servicos e despesa são diferente de nulo investimento é nulo
		} else if (RepositoryDesp.somaDespesasPorIdUsuario(us) != null && repositoryInvest.soma(us) == null
				&& repositoryServico.soma(us) != null) {
			// Passando o resultado para decimal
			DecimalFormat decimal = new DecimalFormat("#,##0.00");
			// Pegando toda soma dos servicos pelo id do usuário
			serv.setValorTotal(repositoryServico.soma(us));
			// Passando o resultado para decimal
			String totalServico = decimal.format(serv.getValorTotal());

			desp.setValorTotal(RepositoryDesp.somaDespesasPorIdUsuario(us));
			String totalDespesas = decimal.format(desp.getValorTotal());

			// Passando valor total para se exibida na página html
			md.addAttribute("msgListaTotal", "R$ " + totalServico);
			md.addAttribute("TotalDosInvestimento", "R$ " + decimal.format(totalInvest));
			md.addAttribute("TotalDasDespesas", "R$ " + totalDespesas);
			return "view/relatorio";
		} else {
			// Passando o resultado para decimal
			DecimalFormat decimal = new DecimalFormat("#,##0.00");
			// Pegando toda soma dos servicos pelo id do usuário
			serv.setValorTotal(repositoryServico.soma(us));
			// Passando o resultado para decimal
			String totalDosServicos = decimal.format(serv.getValorTotal());

			desp.setValorTotal(RepositoryDesp.somaDespesasPorIdUsuario(us));
			String totalDasDespesas = decimal.format(desp.getValorTotal());

			// Pegando toda soma dos investimentos pelo id do usuário
			invest.setValorTotal(repositoryInvest.soma(us));
			// Passando o resultado para decimal
			String totalInvestido = decimal.format(invest.getValorTotal());

			// Passando valor total para se exibida na página html
			md.addAttribute("msgListaTotal", "R$ " + totalDosServicos);
			md.addAttribute("TotalDosInvestimento", "R$ " + totalInvestido);
			md.addAttribute("TotalDasDespesas", "R$ " + totalDasDespesas);

			// exibindo os ganhos na página html
			if (serv.getValorTotal() > invest.getValorTotal()) {
				md.addAttribute("TotalAntesDespesas",
						"R$ " + ganhosAntesDespesas(serv.getValorTotal(), invest.getValorTotal()));
			} else {
				md.addAttribute("TotalAntesDespesasNegativa",
						"R$ " + ganhosAntesDespesas(serv.getValorTotal(), invest.getValorTotal()));
			}

			if (desp.getValorTotal() > serv.getValorTotal() && desp.getValorTotal() > invest.getValorTotal()) {
				md.addAttribute("TotalPosDespesasNegativa",
						"R$ " + ganhosPosDespesas(serv.getValorTotal(), invest.getValorTotal(), desp.getValorTotal()));
			} else {
				md.addAttribute("TotalPosDespesas",
						"R$ " + ganhosPosDespesas(serv.getValorTotal(), invest.getValorTotal(), desp.getValorTotal()));
			}

			return "view/relatorio";
		}

	}

	
	
	//--------------------------Daqui para baixo as operações de relatório vão se realizadas entre as datas -------------------------
	
	
	// Método para realizar relatório por datas
	public String relatorioOperacoesEntreDatas(ModelMap md, Despesa desp, InvestimentoModel invest, Servicos serv,@RequestParam(name="data_i",required = false)String datai,@RequestParam(name="data_f",required = false)String dataf) {
		
		// Passando id do usuário para uma variável us do tipo usuário 
		Usuario us =Ajustes.idUsuarioAoLogar(repositoryUsuario);
		System.out.println("O objeto é esse  " + us.getId());
		
		//Convertendo as datas
        String dataComeco=Ajustes.dataConvertida(datai); String dataFinal=Ajustes.dataConvertida(dataf);
		// ações dos métodos nas linhas debaixo
		// Operação das despesas
		// Listando todas despesas pelo id do usuário
		double totalDespesa = 0, totalInvest = 0, totalServicos = 0;
		// As condições para saber se esta vazias as colunas do valor total de cada tabela no banco de dados
		if (RepositoryDesp.somaDespesasPorIdUsuarioEntreDatas(us, dataComeco, dataFinal) == null && repositoryInvest.somaInvestimentoPeloIdUsuarioDatas(us, dataComeco, dataFinal) == null
				&& repositoryServico.somamdoPorDatasIdUsuario(dataComeco, dataFinal, us) == null) {
			// Passando o resultado para decimal
			DecimalFormat decimal = new DecimalFormat("#,##0.00");
			// exibindo o resultado
			md.addAttribute("TotalDasDespesas", "R$ " + decimal.format(totalDespesa));
			md.addAttribute("TotalDosInvestimento", "R$ " + decimal.format(totalInvest));
			md.addAttribute("msgListaTotal", "R$ " + decimal.format(totalServicos));

			// exibindo os ganhos na página html
			md.addAttribute("TotalAntesDespesas", "R$ " + ganhosAntesDespesas(totalServicos, totalInvest));
			md.addAttribute("TotalPosDespesas", "R$ " + ganhosPosDespesas(totalServicos, totalInvest, totalDespesa));
			// retornando para página de lista despesas
			return "view/relatorio";

			// condição para despesas diferente de nulo e investimento, serviço nulos
		} else if (RepositoryDesp.somaDespesasPorIdUsuarioEntreDatas(us, dataComeco, dataFinal) != null && repositoryInvest.somaInvestimentoPeloIdUsuarioDatas(us, dataComeco, dataFinal) == null
				&& repositoryServico.somamdoPorDatasIdUsuario(dataComeco, dataFinal, us) == null) {
			desp.setValorTotal(RepositoryDesp.somaDespesasPorIdUsuarioEntreDatas(us, dataComeco, dataFinal)); 
			// Passando o resultado para decimal
			DecimalFormat decimal = new DecimalFormat("#,##0.00");
			String total = decimal.format(desp.getValorTotal());
			// Passando valor total para se exibida na página html
			md.addAttribute("TotalDasDespesas", "R$ " + total);
			md.addAttribute("TotalDosInvestimento", "R$ " + decimal.format(totalInvest));
			md.addAttribute("msgListaTotal", "R$ " + decimal.format(totalServicos));
			return "view/relatorio";

			// condição para só investimento sendo diferente de nulo
		} else if (RepositoryDesp.somaDespesasPorIdUsuarioEntreDatas(us, dataComeco, dataFinal) == null && repositoryInvest.somaInvestimentoPeloIdUsuarioDatas(us, dataComeco, dataFinal) != null
				&& repositoryServico.somamdoPorDatasIdUsuario(dataComeco, dataFinal, us) == null) {
			// Pegando toda soma dos investimentos pelo id do usuário as datas
			invest.setValorTotal(repositoryInvest.somaInvestimentoPeloIdUsuarioDatas(us, dataComeco, dataFinal));
			// Passando o resultado para decimal
			DecimalFormat decimal = new DecimalFormat("#,##0.00");
			String totalInvestido = decimal.format(invest.getValorTotal());

			// Passando valor total para se exibida na página html
			md.addAttribute("TotalDasDespesas", "R$ " + decimal.format(totalDespesa));
			md.addAttribute("TotalDosInvestimento", "R$ " + totalInvestido);
			md.addAttribute("msgListaTotal", "R$ " + decimal.format(totalServicos));
			return "view/relatorio";

			// condição para só serviços é diferente de nulo
		} else if (RepositoryDesp.somaDespesasPorIdUsuarioEntreDatas(us, dataComeco, dataFinal) == null && repositoryInvest.somaInvestimentoPeloIdUsuarioDatas(us, dataComeco, dataFinal) == null
				&& repositoryServico.somamdoPorDatasIdUsuario(dataComeco, dataFinal, us) != null) {
			// Pegando toda soma dos servicos pelo id do usuário
			serv.setValorTotal(repositoryServico.somamdoPorDatasIdUsuario(dataComeco, dataFinal, us)); // somando a qtd
			// Passando o resultado para decimal
			DecimalFormat decimal = new DecimalFormat("#,##0.00");
			String totalservicos = decimal.format(serv.getValorTotal());

			// Passando valor total para se exibida na página html
			md.addAttribute("TotalDasDespesas", "R$ " + decimal.format(totalDespesa));
			md.addAttribute("TotalDosInvestimento", "R$ " + decimal.format(totalInvest));
			md.addAttribute("msgListaTotal", "R$ " + totalservicos);
			return "view/relatorio";

			// condição para despesa e investimento se é diferente de nulo e servicos ao
			// contrário
		} else if (RepositoryDesp.somaDespesasPorIdUsuarioEntreDatas(us, dataComeco, dataFinal) != null && repositoryInvest.somaInvestimentoPeloIdUsuarioDatas(us, dataComeco, dataFinal) != null
				&& repositoryServico.somamdoPorDatasIdUsuario(dataComeco, dataFinal, us) == null) {
			desp.setValorTotal(RepositoryDesp.somaDespesasPorIdUsuarioEntreDatas(us, dataComeco, dataFinal)); 
			// Passando o resultado para decimal
			DecimalFormat decimal = new DecimalFormat("#,##0.00");
			String total = decimal.format(desp.getValorTotal());

			// Pegando toda soma dos investimentos pelo id do usuário as datas
			invest.setValorTotal(repositoryInvest.somaInvestimentoPeloIdUsuarioDatas(us, dataComeco, dataFinal));
			// Passando o resultado para decimal
			String totalInvestimento = decimal.format(invest.getValorTotal());

			// Passando valor total para se exibida na página html
			md.addAttribute("TotalDasDespesas", "R$ " + total);
			md.addAttribute("TotalDosInvestimento", "R$ " + totalInvestimento);
			md.addAttribute("msgListaTotal", "R$ " + decimal.format(totalServicos));
			return "view/relatorio";

			// condição despesa sendo nulo investimento e servicos não
		} else if (RepositoryDesp.somaDespesasPorIdUsuarioEntreDatas(us, dataComeco, dataFinal) == null && repositoryInvest.somaInvestimentoPeloIdUsuarioDatas(us, dataComeco, dataFinal) != null
				&& repositoryServico.somamdoPorDatasIdUsuario(dataComeco, dataFinal, us) != null) {
			// Passando o resultado para decimal
			DecimalFormat decimal = new DecimalFormat("#,##0.00");
			// Pegando toda soma dos investimentos pelo id do usuário e as datas
			invest.setValorTotal(repositoryInvest.somaInvestimentoPeloIdUsuarioDatas(us, dataComeco, dataFinal));
			// Passando o resultado para decimal
			String totalInvestido = decimal.format(invest.getValorTotal());

			// Pegando toda soma dos servicos pelo id do usuário as datas
			serv.setValorTotal(repositoryServico.somamdoPorDatasIdUsuario(dataComeco, dataFinal, us)); 
			// Passando o resultado para decimal
			String totalServico = decimal.format(serv.getValorTotal());

			// Passando valor total para se exibida na página html
			md.addAttribute("TotalDosInvestimento", "R$ " + totalInvestido);
			md.addAttribute("TotalDasDespesas", "R$ " + decimal.format(totalDespesa));
			md.addAttribute("msgListaTotal", "R$ " + totalServico);
			return "view/relatorio";

			// condição servicos e despesa são diferente de nulo investimento é nulo
		} else if (RepositoryDesp.somaDespesasPorIdUsuarioEntreDatas(us, dataComeco, dataFinal) != null && repositoryInvest.somaInvestimentoPeloIdUsuarioDatas(us, dataComeco, dataFinal) == null
				&& repositoryServico.somamdoPorDatasIdUsuario(dataComeco, dataFinal, us) != null) {
			// Passando o resultado para decimal
			DecimalFormat decimal = new DecimalFormat("#,##0.00");
			// Pegando toda soma dos servicos pelo id do usuário e as datas
			serv.setValorTotal(repositoryServico.soma(us));
			// Passando o resultado para decimal
			String totalServico = decimal.format(serv.getValorTotal());

			desp.setValorTotal(RepositoryDesp.somaDespesasPorIdUsuarioEntreDatas(us, dataComeco, dataFinal));
			String totalDespesas = decimal.format(desp.getValorTotal());

			// Passando valor total para se exibida na página html
			md.addAttribute("msgListaTotal", "R$ " + totalServico);
			md.addAttribute("TotalDosInvestimento", "R$ " + decimal.format(totalInvest));
			md.addAttribute("TotalDasDespesas", "R$ " + totalDespesas);
			return "view/relatorio";
		} else {
			// Passando o resultado para decimal
			DecimalFormat decimal = new DecimalFormat("#,##0.00");
			// Pegando toda soma dos servicos pelo id do usuário e as datas
			serv.setValorTotal(repositoryServico.somamdoPorDatasIdUsuario(dataComeco, dataFinal, us));
			// Passando o resultado para decimal
			String totalDosServicos = decimal.format(serv.getValorTotal());
             // Pegando toda soma dos despesas pelo id do usuário e as datas
			desp.setValorTotal(RepositoryDesp.somaDespesasPorIdUsuarioEntreDatas(us, dataComeco, dataFinal));
			String totalDasDespesas = decimal.format(desp.getValorTotal());

			// Pegando toda soma dos investimentos pelo id do usuário e as datas
			invest.setValorTotal(repositoryInvest.somaInvestimentoPeloIdUsuarioDatas(us, dataComeco, dataFinal));
			// Passando o resultado para decimal
			String totalInvestido = decimal.format(invest.getValorTotal());

			// Passando valor total para se exibida na página html
			md.addAttribute("msgListaTotal", "R$ " + totalDosServicos);
			md.addAttribute("TotalDosInvestimento", "R$ " + totalInvestido);
			md.addAttribute("TotalDasDespesas", "R$ " + totalDasDespesas);

			// exibindo os ganhos na página html
			if (serv.getValorTotal() > invest.getValorTotal()) {
				md.addAttribute("TotalAntesDespesas",
						"R$ " + ganhosAntesDespesas(serv.getValorTotal(), invest.getValorTotal()));
			} else {
				md.addAttribute("TotalAntesDespesasNegativa",
						"R$ " + ganhosAntesDespesas(serv.getValorTotal(), invest.getValorTotal()));
			}

			if (desp.getValorTotal() > serv.getValorTotal() && desp.getValorTotal() > invest.getValorTotal()) {
				md.addAttribute("TotalPosDespesasNegativa",
						"R$ " + ganhosPosDespesas(serv.getValorTotal(), invest.getValorTotal(), desp.getValorTotal()));
			} else {
				md.addAttribute("TotalPosDespesas",
						"R$ " + ganhosPosDespesas(serv.getValorTotal(), invest.getValorTotal(), desp.getValorTotal()));
			}

			return "view/relatorio";
		}

	}

     //Método para exibir o relatório pelas datas
	@PostMapping("/relatorio/datas")
	public String relatorioPorDatas(ModelMap md, Despesa desp, InvestimentoModel invest, Servicos serv,@RequestParam(name="data_i",required = false)String datai,@RequestParam(name="data_f",required = false)String dataf) {
	  
		if(!datai.isEmpty() && !dataf.isEmpty()) {
			//Método para realizar as operações de despesas,investimentos e servicos pelas datas e id do usuário
			relatorioOperacoesEntreDatas(md, desp, invest, serv, datai, dataf);
		}
		
		return "view/relatorio";	
	}
	
	// Método para fazer o calculo do ganho antes das despesas
	public String ganhosAntesDespesas(double servicos, double invest) {
		ModelMap md = new ModelMap();
		// Passando o resultado para decimal
		DecimalFormat decimal = new DecimalFormat("#,##0.00");
		double totalAntesDespesas = servicos - invest;
		String totalGanho = decimal.format(totalAntesDespesas);
		return totalGanho;
	}

	// Método para fazer o calculo do ganho pós despesas
	public String ganhosPosDespesas(double servicos, double invest, double desp) {
		// Passando o resultado para decimal
		DecimalFormat decimal = new DecimalFormat("#,##0.00");
		double totalPosDespesas = (servicos - invest) - desp;
		String totalGanho = decimal.format(totalPosDespesas);
		return totalGanho;
	}
    
}
