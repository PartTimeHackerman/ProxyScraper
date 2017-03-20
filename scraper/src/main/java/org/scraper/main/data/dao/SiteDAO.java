package org.scraper.main.data.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.scraper.main.data.Site;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SiteDAO implements DAO<Site> {
	
	private SessionFactory sessionFactory;
	
	public SiteDAO(){
		sessionFactory = SessionFactoryBuilder.get().getSessionFactory();
	}
	
	@Override
	public void post(Site site) {
		postAll(new ArrayList<>(Collections.singletonList(site)));
	}
	
	@Override
	public void postAll(List<Site> sites) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		sites.forEach(session::saveOrUpdate);
		session.getTransaction().commit();
		session.close();
	}
	
	@Override
	public List<Site> fetchAll() {
		Session session = sessionFactory.openSession();
		Query<Site> query = session.createQuery("FROM Site", Site.class);
		List<Site> sites = query.list();
		session.close();
		return sites;
	}
}
