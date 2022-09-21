package br.edu.ifrn.atelie.DTO;
// Essa class será resposáveel para realizar o autocomplete no campo de vagas
public class AutocompleteDTO {
        // variável para armazer o titulo da vaga na pesquisa
	 private String label;
	 // variável para armazenar o id da vaga
	 private Integer value;
	 
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public Integer getValue() {
		return value;
	}
	public void setValue(Integer value) {
		this.value = value;
	}
	public AutocompleteDTO(String label, Integer value) {
		super();
		this.label = label;
		this.value = value;
	}
}
