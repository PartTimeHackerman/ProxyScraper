package org.scraper.model.web;

import org.jsoup.Connection;
import org.scraper.model.IConcurrent;
import org.scraper.model.MainLogger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class ConcurrentConnectionExecutor extends ConnectionExecutor implements IConcurrent {
	
	private static ConnectionExecutor connectionExecutor;
	
	private ConcurrentConnectionExecutor() {
	}
	
	public static ConnectionExecutor getInstance() {
		if (connectionExecutor == null) {
			synchronized (ConcurrentConnectionExecutor.class) {
				if (connectionExecutor == null)
					connectionExecutor = new ConcurrentConnectionExecutor();
			}
		}
		return connectionExecutor;
	}
	
	@Override
	public Connection.Response execute(Connection connection) {
		return send(connection::execute);
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
		
		return sendTasks(responses);
	}
	
	@Override
	public List<byte[]> executeAsBytes(List<Connection> connections) {
		List<Callable<byte[]>> responses = new ArrayList<>(connections.size());
		connections
				.forEach(connection ->
								 responses.add(() ->
													   super.executeAsBytes(connection)));
		
		return sendTasks(responses);
	}
	
	@Override
	public List<String> executeParse(List<Connection> connections) {
		List<Callable<String>> responses = new ArrayList<>(connections.size());
		connections
				.forEach(connection ->
								 responses.add(() ->
													   super.executeParse(connection)));
		
		return sendTasks(responses);
	}
}
