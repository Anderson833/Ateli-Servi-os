package br.edu.ifrn.atelie.Service;

import org.springframework.beans.factory.annotation.Autowired;

import br.edu.ifrn.atelie.Modelo.Usuario;
import br.edu.ifrn.atelie.Repository.UsuarioRepository;
 /**
  *  Esse classe é para realizar a buscar do objeto do tipo usuário e converter as datas
  * @author ander 
  *
  */
public class Ajustes {

	@Autowired
	private UsuarioRepository repository;
	
   /**
    *  Método para buscar o id do objeto usuário
    * @param re
    * @return O id do Usuário
    */
	public static Usuario idUsuarioAoLogar(UsuarioRepository re) {
		String email=Usuario.getEmailUsuario();
		System.out.println(" email do usuário "+email);
		 // Pegando id do usuário pelo email informado no paramentro
		  int ids = re.BuscaIdPeloEmail(email);
			System.out.println("aqui  id do usuário é = "+ids);
			// buscando todos dados do usuário pelo id informa no paramentro
		     Usuario  us= re.BuscaTodosDadosDoUsuarioPeloId(ids);
		 System.out.println("O id do objeto Usuário é = "+us.getId());
			return us;
	     } 
	
	/**
	 * Método para converter as datas de date para String
	 * @param data
	 * @return Data Convertida
	 */
		public static String dataConvertida(String data) {
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
