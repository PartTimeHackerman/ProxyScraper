package org.scraper.comp.other;

import javax.swing.filechooser.FileSystemView;
import java.io.*;
import java.util.Map;
import java.util.concurrent.*;

public class Persons {

	public static final String path = FileSystemView.getFileSystemView().getDefaultDirectory().getPath();

	public static int personsNum = 100;

	private Map<Integer, Person> personMap = new ConcurrentHashMap<>();

	public void loadPersons() {

		if (!new File(path + "/persons.ser").exists()) {
			ExecutorService executor = Executors.newFixedThreadPool(10,new ThreadFactory() {
				@Override
				public Thread newThread(Runnable r) {
					Thread t = Executors.defaultThreadFactory().newThread(r);
					t.setDaemon(true);
					return t;
				}
			});
			CountDownLatch counter = new CountDownLatch(personsNum);
			for (int i = 0; i < personsNum; i++) {
				int finalI = i;
				executor.execute(new Runnable() {
					@Override
					public void run() {
						System.out.print("\n" + finalI);
						Person person;
						try {
							person = new Person();
						}
						catch (Exception e){
							person=null;
						}
						personMap.put(finalI, person);
						counter.countDown();
					}
				});
			}
			try {
				counter.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			serializePersons(personMap);
		} else {
			try {
				FileInputStream fileIn = new FileInputStream(path + "/persons.ser");
				ObjectInputStream in = new ObjectInputStream(fileIn);
				personMap = (Map<Integer, Person>) in.readObject();
				in.close();
				fileIn.close();
			} catch (IOException | ClassNotFoundException i) {
				i.printStackTrace();
			}
		}
	}

	public static void serializePersons(Map<Integer, Person> persons){
		try {
			FileOutputStream fileOut = new FileOutputStream(path + "/persons.ser");
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(persons);
			out.close();
			fileOut.close();
		} catch (IOException i) {
			i.printStackTrace();
		}
	}

	public Map<Integer, Person> getPersons(){
		loadPersons();
		return personMap;
	}
}
