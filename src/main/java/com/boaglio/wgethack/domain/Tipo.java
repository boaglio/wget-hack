package com.boaglio.wgethack.domain;

 
public class Tipo {
 
	private Integer id;

	private String nome;

	private String diretorioRaiz;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDiretorioRaiz() {
		return diretorioRaiz;
	}

	public void setDiretorioRaiz(String diretorioRaiz) {
		this.diretorioRaiz = diretorioRaiz;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (nome == null ? 0 : nome.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) { return true; }
		if (obj == null) { return false; }
		if (getClass() != obj.getClass()) { return false; }
		Tipo other = (Tipo) obj;
		if (nome == null) {
			if (other.nome != null) { return false; }
		} else if (!nome.equals(other.nome)) { return false; }
		return true;
	}

	@Override
	public String toString() {
		return "Tipo [id=" + id + ", nome=" + nome + ", diretorioRaiz=" + diretorioRaiz + "]";
	}

}
