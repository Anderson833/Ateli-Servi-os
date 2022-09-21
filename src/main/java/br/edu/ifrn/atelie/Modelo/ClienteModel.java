package br.edu.ifrn.atelie.Modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

//Essa classe será para modelar o objeto cliente

@Entity
public class ClienteModel {
    // Os Atributos para cliente
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
	
	@OneToMany(mappedBy = "cliente",orphanRemoval = true, cascade = CascadeType.ALL)
	private List<Servicos> servicos = new ArrayList<>(); 
	
	public List<Servicos> getServicos() {
		return servicos;
	}

	public void setServicos(List<Servicos> servicos) {
		this.servicos = servicos;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ClienteModel other = (ClienteModel) obj;
		return id == other.id;
	}
	
	// Os métodos getters e setters
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
	public String getEndereco() {
		return endereco;
	}
	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}
	public String getTelefone() {
		return telefone;
	}
	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}
	@Column(nullable = false)
	private String nome;
	@Column(nullable = false)
	private String endereco;
	@Column(nullable = false)
	private String telefone;
		
}
