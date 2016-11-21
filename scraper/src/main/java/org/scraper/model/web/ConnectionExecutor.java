package org.scraper.model.web;

import org.jsoup.Connection;
import org.scraper.model.MainLogger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ConnectionExecutor implements IConnectionExecutor {
	
	@Override
	public Connection.Response execute(Connection connection) {
		try {
			return connection.execute();
		} catch (IOException e) {
			MainLogger.log().fatal(e);
			return null;
		}
	}
	
	@Override
	public byte[] executeAsBytes(Connection connection) {
		return execute(connection).bodyAsBytes();
	}
	
	@Override
	public String executeParse(Connection connection) {
		try {
			return execute(connection).parse().text();
		} catch (IOException e) {
			MainLogger.log().fatal(e);
			return null;
		}
	}
	
	@Override
	public List<Connection.Response> execute(List<Connection> connections){
		List<Connection.Response> responses = new ArrayList<>();
		connections.forEach(this::execute);
		return responses;
	}
	
	@Override
	public List<byte[]> executeAsBytes(List<Connection> connections){
		List<byte[]> responses = new ArrayList<>();
		connections.forEach(this::executeAsBytes);
		return responses;
	}
	
	@Override
	public List<String> executeParse(List<Connection> connections){
		List<String> responses = new ArrayList<>();
		connections.forEach(this::executeParse);
		return responses;
	}
}
