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
		// Pegando email do usuário
		String email = Usuario.getEmailUsuario();
		System.out.println(" aqui o email " + Usuario.getEmailUsuario());
		// Pegando id do usuário pelo email informado no paramentro
		int id = repositoryUsuario.BuscaIdPeloEmail(email);
		System.out.println("aqui  id do usuário é = " + id);

		// buscando todos dados do usuário pelo id informa no paramentro
		Usuario us = repositoryUsuario.BuscaTodosDadosDoUsuarioPeloId(id);
		System.out.println("O objeto é esse  " + us.getId());

		// ações dos métodos nas linhas debaixo
		// Operação das despesas
		// Listando todas despesas pelo id do usuário
		double totalDespesa = 0, totalInvest = 0, totalServicos = 0;
		// As condições para saber se esta vazias as tabelas
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
		// Pegando email do usuário
		String email = Usuario.getEmailUsuario();
		System.out.println(" aqui o email " + Usuario.getEmailUsuario());
		// Pegando id do usuário pelo email informado no paramentro
		int id = repositoryUsuario.BuscaIdPeloEmail(email);
		System.out.println("aqui  id do usuário é = " + id);

		// buscando todos dados do usuário pelo id informa no paramentro
		Usuario us = repositoryUsuario.BuscaTodosDadosDoUsuarioPeloId(id);
		System.out.println("O objeto é esse  " + us.getId());
		
		//Convertendo as datas
        String dataComeco=dataConvertida(datai); String dataFinal=dataConvertida(dataf);
		// ações dos métodos nas linhas debaixo
		// Operação das despesas
		// Listando todas despesas pelo id do usuário
		double totalDespesa = 0, totalInvest = 0, totalServicos = 0;
		// As condições para saber se esta vazias as tabelas
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
