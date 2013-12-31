package com.boaglio.wgethack.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "podcast")
public class Podcast {

	@Id
	@GeneratedValue
	private Integer id;

	private String status;
	private String dia;

	@ManyToOne
	private Tipo tipo;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDia() {
		return dia;
	}

	public void setDia(String dia) {
		this.dia = dia;
	}

	public Tipo getTipo() {
		return tipo;
	}

	public void setTipo(Tipo tipo) {
		this.tipo = tipo;
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
		result = prime * result + (dia == null ? 0 : dia.hashCode());
		result = prime * result + (tipo == null ? 0 : tipo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) { return true; }
		if (obj == null) { return false; }
		if (getClass() != obj.getClass()) { return false; }
		Podcast other = (Podcast) obj;
		if (dia == null) {
			if (other.dia != null) { return false; }
		} else if (!dia.equals(other.dia)) { return false; }
		if (tipo == null) {
			if (other.tipo != null) { return false; }
		} else if (!tipo.equals(other.tipo)) { return false; }
		return true;
	}

	@Override
	public String toString() {
		return "Podcast [id=" + id + ", status=" + status + ", dia=" + dia + ", tipo=" + tipo + "]";
	}

}
