package org.scraper.MVC.model;

import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ReadOnlyFloatWrapper;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.util.Callback;
import org.scraper.MVC.control.IClipboard;
import org.scraper.MVC.control.IDeleter;
import org.scraper.main.MainLogger;
import org.scraper.main.Pool;
import org.scraper.main.Proxy;
import org.scraper.main.checker.IProxyChecker;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class ProxyModelFX extends ProxyUtility implements IDeleter<Proxy>, IClipboard<Proxy> {
	
	private ObservableList<Proxy> visible;
	
	private ObservableList<Proxy> allFX = FXCollections.observableArrayList();
	private ListProperty<Proxy> allProperty = new SimpleListProperty<>(allFX);
	
	private ObservableList<Proxy> workingFX = FXCollections.observableArrayList();
	private ListProperty<Proxy> workingProperty = new SimpleListProperty<>(workingFX);
	
	public ProxyModelFX(IProxyChecker checker, Pool pool) {
		super(checker, pool);
		
		Callback<Proxy, Observable[]> callback = (Proxy proxy) -> new Observable[]{new ReadOnlyFloatWrapper(proxy.getSpeed())};
		visible = FXCollections.observableArrayList(callback);
		
		visible.addListener((ListChangeListener<Proxy>) c -> {
			while (c.next()) {
				if (c.wasUpdated()) {
					MainLogger.log().info(" updated");
				}
			}
		});
	}
	
	public void addProxy(Proxy proxy) {
		super.addProxy(proxy);
		
		Platform.runLater(() -> {
			allFX.add(proxy);
			if (proxy.isWorking() || !proxy.isChecked() || showBroken)
				visible.add(proxy);
		});
	}
	
	public void addProxy(String proxyString) {
		if (!proxyString.matches(proxyPattern)) return;
		Proxy proxy = new Proxy(proxyString);
		addProxy(proxy);
	}
	
	public Collection<Proxy> filterByType(Proxy.Type type) {
		Collection<Proxy> filtered = super.filterByType(type);
		clearAndAddToVisibleProxies(filtered);
		return filtered;
	}
	
	public Collection<Proxy> filterByAnonymity(Proxy.Anonymity anonymity) {
		Collection<Proxy> filtered = super.filterByAnonymity(anonymity);
		clearAndAddToVisibleProxies(filtered);
		return filtered;
	}
	
	public Collection<Proxy> filterByTimeout(Float time) {
		Collection<Proxy> filtered = super.filterByTimeout(time);
		clearAndAddToVisibleProxies(filtered);
		return filtered;
	}
	
	public Collection<Proxy> filterBroken(boolean broken) {
		Collection<Proxy> filtered = super.filterBroken(broken);
		clearAndAddToVisibleProxies(filtered);
		return filtered;
	}
	
	private void clearAndAddToVisibleProxies(Collection<Proxy> proxies) {
		visible.clear();
		visible.addAll(proxies);
	}
	
	@Override
	public void deleteSelected(List<Proxy> selected) {
		for (Proxy proxy : new CopyOnWriteArrayList<>(selected)) {
			visible.remove(proxy);
			allFX.remove(proxy);
		}
	}
	
	@Override
	public void handleCopy(List<Proxy> proxyList) {
		String selectedProxiesString = proxyList.stream()
				.map(Proxy::getIpPort)
				.collect(Collectors.joining("\n"));
		copy(selectedProxiesString);
	}
	
	@Override
	public void handlePaste() {
		List<String> clipboardContent = paste();
		clipboardContent.forEach(this::addProxy);
	}
	
	public ObservableList<Proxy> getVisible() {
		return visible;
	}
	
	
	public ReadOnlyIntegerProperty getWorkingSize() {
		return workingProperty.sizeProperty();
	}
	
	public ReadOnlyIntegerProperty getAllSize() {
		return allProperty.sizeProperty();
	}
}
