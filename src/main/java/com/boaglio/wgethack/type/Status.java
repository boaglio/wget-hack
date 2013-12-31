package com.boaglio.wgethack.type;

public enum Status {

	naoEncontrado("N"),
	baixado("B");

	String valor;

	Status(String valor) {
		this.valor = valor;

	}

	public String valor() {
		return valor;
	}

}
