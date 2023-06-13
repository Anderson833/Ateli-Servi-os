package br.edu.ifrn.atelie.Modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.GenericGenerator;
 // Tabela de Usuário construida
@Entity
public class Usuario {
    
	// atributos de usuário
	  public static final String admin="admin";
	  @Id
	  @GeneratedValue(generator = "increment")
	  @GenericGenerator(name = "increment", strategy = "increment")
	 private int id;
	 private String perfilUsuario=admin;
	
	 private static String EmailUsuario;
	
	public static String getEmailUsuario() {
		return EmailUsuario;
	}
	public static void setEmailUsuario(String emailUsuario) {
		EmailUsuario = emailUsuario;
	}
	
	public String getPerfilUsuario() {
		return perfilUsuario;
	}
	public void setPerfilUsuario(String perfilUsuario) {
		this.perfilUsuario = perfilUsuario;
	}


	@OneToMany(mappedBy = "usuario",orphanRemoval = true, cascade = CascadeType.ALL)
	private List<Servicos> servicos = new ArrayList<>(); 
	
	public List<Servicos> getServicos() {
		return servicos;
	}

	public void setServicos(List<Servicos> servicos) {
		this.servicos = servicos;
	}
	
	@OneToMany(mappedBy = "usuario",orphanRemoval = true, cascade = CascadeType.ALL)
	private List<ClienteModel> clientes = new ArrayList<>(); 
	
	
	public List<ClienteModel> getClientes() {
		return clientes;
	}
	public void setClientes(List<ClienteModel> clientes) {
		this.clientes = clientes;
	}
	
	@OneToMany(mappedBy = "usuario",orphanRemoval = true, cascade = CascadeType.ALL)
	private List<Despesa> despesa = new ArrayList<>(); 
	
	public List<Despesa> getDespesa() {
		return despesa;
	}
	public void setDespesa(List<Despesa> despesa) {
		this.despesa = despesa;
	}

	@OneToMany(mappedBy = "usuario",orphanRemoval = true, cascade = CascadeType.ALL)
	private List<InvestimentoModel> investimento = new ArrayList<>();
	
	public List<InvestimentoModel> getInvestimento() {
		return investimento;
	}
	public void setInvestimento(List<InvestimentoModel> investimento) {
		this.investimento = investimento;
	}

	@Column(nullable = false)
	private String nome;
	@Column(nullable = false)
	 private String Email;
	@Column(nullable = false)
	 private String senha;
	 
	//Métodos getters e setters 
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getEmail() {
		return Email;
	}
	public void setEmail(String email) {
		Email = email;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	
	// Gerado os hashCode e equals para  o id do Usuário
	
	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Usuario other = (Usuario) obj;
		return id == other.id;
	}
	 
	 
}
