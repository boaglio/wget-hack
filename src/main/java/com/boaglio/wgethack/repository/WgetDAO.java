package com.boaglio.wgethack.repository;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import com.boaglio.wgethack.HibernateUtil;
import com.boaglio.wgethack.domain.Podcast;
import com.boaglio.wgethack.domain.Tipo;

public class WgetDAO implements WgetRepository {

	public List<Tipo> getTodosTipos() {

		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
		Session session = sessionFactory.openSession();

		@SuppressWarnings("unchecked")
		List<Tipo> tipos = session.createQuery("from Tipo").list();

		session.close();

		for (Tipo tipo : tipos) {
			System.out.println(tipo.getId() + ". " + tipo.getNome());
		}

		return tipos;
	}

	public void gravaStatus(Podcast podcast) {
		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();

		Podcast storedPodcast = buscaPodcastById(podcast);

		if (storedPodcast != null && storedPodcast.getId() != null) {
			storedPodcast.setStatus(podcast.getStatus());
		} else {
			storedPodcast = podcast;
		}
		session.save(storedPodcast);
		session.flush();
		tx.commit();
		session.close();
	}

	public Podcast buscaPodcastById(Podcast p) {

		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
		Session session = sessionFactory.openSession();

		Podcast podcast = null;

		Criteria criteria = session.createCriteria(Podcast.class);
		criteria.add(Restrictions.eq("dia",p.getDia()));
		criteria.add(Restrictions.eq("tipo.id",p.getTipo().getId()));

		podcast = (Podcast) criteria.uniqueResult();

		session.close();

		System.out.println(podcast);

		return podcast;

	}

}
