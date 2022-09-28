package br.edu.ifrn.atelie.Modelo;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

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
	 
	 public String getPerfilUsuario() {
		return perfilUsuario;
	}
	public void setPerfilUsuario(String perfilUsuario) {
		this.perfilUsuario = perfilUsuario;
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
