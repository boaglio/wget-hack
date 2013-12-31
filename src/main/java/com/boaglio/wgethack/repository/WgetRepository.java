package com.boaglio.wgethack.repository;

import java.util.List;

import com.boaglio.wgethack.domain.Podcast;
import com.boaglio.wgethack.domain.Tipo;

public interface WgetRepository {

	List<Tipo> getTodosTipos();

	void gravaStatus(Podcast podcast);

	Podcast buscaPodcastById(Podcast p);

}
