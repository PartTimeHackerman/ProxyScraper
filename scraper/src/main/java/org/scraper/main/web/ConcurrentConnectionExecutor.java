package org.scraper.main.web;

import org.jsoup.Connection;
import org.scraper.main.IConcurrent;
import org.scraper.main.MainLogger;
import org.scraper.main.Pool;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

public class ConcurrentConnectionExecutor extends ConnectionExecutor implements IConcurrent {
	
	private Pool pool;
	
	public ConcurrentConnectionExecutor(Pool pool) {
		this.pool = pool;
	}
	
	@Override
	public Connection.Response execute(Connection connection) {
		return send(connection::execute, pool);
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
		}
		return "";
	}
	
	@Override
	public List<Connection.Response> execute(List<Connection> connections) {
		List<Callable<Connection.Response>> responses = new ArrayList<>(connections.size());
		connections
				.forEach(connection ->
								 responses.add(() ->
													   super.execute(connection)));
		
		return pool.sendTasks(responses);
	}
	
	@Override
	public List<byte[]> executeAsBytes(List<Connection> connections) {
		List<Callable<byte[]>> responses = new ArrayList<>(connections.size());
		connections
				.forEach(connection ->
								 responses.add(() ->
													   super.executeAsBytes(connection)));
		
		return pool.sendTasks(responses);
	}
	
	@Override
	public List<String> executeParse(List<Connection> connections) {
		List<Callable<String>> responses = new ArrayList<>(connections.size());
		connections
				.forEach(connection ->
								 responses.add(() ->
													   super.executeParse(connection)));
		
		return pool.sendTasks(responses);
	}
}
