package org.scraper.main.data.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.scraper.main.data.Domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DomainDAO implements DAO<Domain> {
	
	private SessionFactory sessionFactory;
	
	public DomainDAO(){
		sessionFactory = SessionFactoryBuilder.get().getSessionFactory();
	}
	
	@Override
	public void post(Domain domain) {
		postAll(new ArrayList<>(Collections.singletonList(domain)));
	}
	
	@Override
	public void postAll(List<Domain> domains) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		domains.forEach(session::saveOrUpdate);
		session.getTransaction().commit();
		session.close();
	}
	
	@Override
	public List<Domain> fetchAll() {
		Session session = sessionFactory.openSession();
		Query<Domain> query = session.createQuery("FROM Domain", Domain.class);
		List<Domain> domains = query.list();
		session.close();
		return domains;
	}
}
