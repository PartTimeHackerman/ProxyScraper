package org.scraper.main.data.dao;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.scraper.main.MainLogger;

public class SessionFactoryBuilder {
	
	private static SessionFactoryBuilder sessionFactoryBuilder;
	
	private SessionFactory sessionFactory;
	
	private SessionFactoryBuilder(){
		setUp();
	}
	
	public static SessionFactoryBuilder get(){
		if(sessionFactoryBuilder == null){
			synchronized (SessionFactoryBuilder.class){
				if (sessionFactoryBuilder == null) {
					sessionFactoryBuilder = new SessionFactoryBuilder();
				}
			}
		}
		return sessionFactoryBuilder;
	}
	
	private void setUp() {
		// A SessionFactory is set up once for an application!
		final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
				.configure() // configures settings from hibernate.cfg.xml
				.build();
		try {
			sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
		} catch (Exception e) {
			// The registry would be destroyed by the SessionFactory, but we had trouble building the SessionFactory
			// so destroy it manually.
			MainLogger.log(this).fatal(e);
			StandardServiceRegistryBuilder.destroy(registry);
		}
	}
	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
}
