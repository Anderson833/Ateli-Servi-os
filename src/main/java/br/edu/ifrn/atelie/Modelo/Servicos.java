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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.GenericGenerator;

// ESSA CLASSE VAI MODELA OS SERVIÇOS PARA CONSTRUIR UM OBJETO SERVIÇOS

@Entity
@Table(name="servicos")
public class Servicos {
	// ATRIBUTOS DE SERVIÇOS
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int id;
	private String nome;
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	@Column(nullable = true)
	private String descricao;
	@Column(nullable = true)
	private double valorUnitario;
   @Column(nullable = true)
	private double valorTotal;
	@Column(nullable = true)
	private double Quantidade;
	   @Column(nullable = true)
	private String dataInicio; 
	@Column(nullable = true)
	private String dataTermino;
	@Column(nullable = true)
	private String tipoServico;
	
	public String getTipoServicos() {
		return tipoServico;
	}
	public void setTipoServicos(String tipoServico) {
		this.tipoServico = tipoServico;
	}

	@ForeignKey(name="cliente_id")
	@ManyToOne 
	private ClienteModel cliente;

	
	//métodos getters e setters
	
	public ClienteModel getCliente() {
		return cliente;
	}
	@Override
	public int hashCode() {
		return Objects.hash(cliente, id);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Servicos other = (Servicos) obj;
		return Objects.equals(cliente, other.cliente) && id == other.id;
	}
	public void setCliente(ClienteModel cliente) {
		this.cliente = cliente;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public double getQuantidade() {
		return Quantidade;
	}
	public void setQuantidade(double quantidade) {
		Quantidade = quantidade;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public double getValorUnitario() {
		return valorUnitario;
	}
	public void setValorUnitario(double valorUnitario) {
		this.valorUnitario = valorUnitario;
	}
	public double getValorTotal() {
		return valorTotal;
	}
	public void setValorTotal(double valorTotal) {
		this.valorTotal = valorTotal;
	}
	public String getDataInicio() {
		return dataInicio;
	}
	public void setDataInicio(String dataInicio) {
		this.dataInicio = dataInicio;
	}
	public String getDataTermino() {
		return dataTermino;
	}
	public void setDataTermino(String dataTermino) {
		this.dataTermino = dataTermino;
	}
	
	
	
}
