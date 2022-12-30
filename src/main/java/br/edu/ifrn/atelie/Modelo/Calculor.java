package br.edu.ifrn.atelie.Modelo;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
    // tabela para armazenar os valores e fazer a multiplicação 
@Entity
public class Calculor {
     
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id; 
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
		Calculor other = (Calculor) obj;
		return id == other.id;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	private double valorUnitario;
	private double valorTotal;
	private double Quantidade;
	private static String idUsuario;
	
	public String idBuscado() {
		return this.getIdUsuario();
	}
	
	public static String getIdUsuario() {
		return idUsuario;
	}
	public static void setIdUsuario(String idUsuario) {
		Calculor.idUsuario = idUsuario;
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
	public double getQuantidade() {
		return Quantidade;
	}
	public void setQuantidade(double quantidade) {
		this.Quantidade = quantidade;
	}
	
	
}
