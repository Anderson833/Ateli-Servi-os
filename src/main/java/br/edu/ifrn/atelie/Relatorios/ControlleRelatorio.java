package br.edu.ifrn.atelie.Relatorios;


import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.codec.binary.Base64;
import org.attoparser.dom.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.fasterxml.jackson.core.format.DataFormatDetector;

import br.edu.ifrn.atelie.Service.ServicosRelatorios;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

@Controller
public class ControlleRelatorio {
    
	@Autowired
	private ServicosRelatorios RelatorioServices;
	@Autowired
	private JdbcTemplate jdbactemplate;
	//@GetMapping(value="/relatorioPdf",produces = "apllication/pdf")
	public ResponseEntity<String> donwloadPdf(HttpServletRequest request) throws JRException, SQLException{
		byte [] pdf = RelatorioServices.geraRelatorio("servicos.", request.getServletContext());
		
		String base64Pdf ="data:application/pdf;base64,"+Base64.encodeBase64String(pdf);
		return new ResponseEntity<String>(base64Pdf, HttpStatus.OK);
	}
	@GetMapping("/relatorioPdf")
	public void criarPdf(HttpServletResponse response) throws IOException, JRException {
		response.setContentType("apllication/pdf");
		DateFormat   dataFormatada  = new SimpleDateFormat("yyyy-MM-dd:hh:mm:ss");
		String dataCorreta =dataFormatada.format(new Date());
		String headerkey ="Content-Disposition";
		String headerValue="attachment; filename=pdf_"+dataFormatada+".pdf";
		response.setHeader(headerkey, headerValue);
		RelatorioServices.exportJasperSoft(response);
		
	}

}
