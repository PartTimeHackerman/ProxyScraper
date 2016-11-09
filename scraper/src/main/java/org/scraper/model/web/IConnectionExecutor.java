package org.scraper.model.web;

import org.jsoup.Connection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public interface IConnectionExecutor {
	
	Connection.Response execute(Connection connection);
	
	byte[] executeAsBytes(Connection connection);
	
	String executeParse(Connection connection);
	
	
	List<Connection.Response> execute(List<Connection> connections);
	
	List<byte[]> executeAsBytes(List<Connection> connections);
	
	List<String> executeParse(List<Connection> connections);
}
