package br.edu.ifrn.atelie.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections4.Get;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import br.edu.ifrn.atelie.Modelo.Usuario;
import br.edu.ifrn.atelie.Repository.UsuarioRepository;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

@Service
public class ServicosRelatorios implements Serializable{
	
	private static final long serialVersionUID = 1L;
	@Autowired
	private JdbcTemplate jdbactemplate;
	
	@Autowired
	private UsuarioRepository repositoryUsuario;
	
	public byte[] geraRelatorio(String nomeRelatorio,ServletContext servletContext) throws JRException, SQLException {
		
		/*Fazendo a conexão com banco de dados */
		Connection connection =jdbactemplate.getDataSource().getConnection();
		
		/*Criando o caminho do arquivo jasper*/
		String caminhoJasper= servletContext.getRealPath("")
				+File.separator+nomeRelatorio+"jasper";
		
		/*Gerar o relatório com os dados e conexao*/
		JasperPrint print = JasperFillManager.fillReport(caminhoJasper, new HashMap<>(), connection);
		
		/* Exporta para byte o pdf para download*/
		byte[] retorno= JasperExportManager.exportReportToPdf(print);
		connection.close();
		return retorno;
	
	}
	 public void exportJasperSoft(HttpServletResponse response) throws IOException, JRException{
		 List<Usuario> listaUsuarios =repositoryUsuario.findAll();
		 File file = ResourceUtils.getFile("classpath:servicos.jrxml");
		 JasperReport  jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
		 JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(listaUsuarios);
		 HashMap<String,Object> parametro = new HashMap<>();
		 parametro.put("12345","Anderson");
		 /*Gerar o relatório com os dados e conexao*/
		JasperPrint print = JasperFillManager.fillReport(jasperReport, new HashMap<>(), dataSource);
		JasperExportManager.exportReportToPdfStream(print, response.getOutputStream());
		
			
	 }
}
