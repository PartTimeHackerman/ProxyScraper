package org.scraper.model.modles;

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
import org.scraper.control.IClipboard;
import org.scraper.control.IDeleter;
import org.scraper.control.filters.*;
import org.scraper.model.IConcurrent;
import org.scraper.model.MainLogger;
import org.scraper.model.Proxy;
import org.scraper.model.checker.IProxyChecker;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class ProxyModel implements IConcurrent, IDeleter<Proxy>, IClipboard<Proxy> {
	
	private IProxyChecker checker;
	
	private ObservableList<Proxy> visible;
	private ObservableList<Proxy> all = FXCollections.observableArrayList();
	
	private ListProperty<Proxy> allProperty = new SimpleListProperty<>(all);
	
	
	private ObservableList<Proxy> working = FXCollections.observableArrayList();
	private ListProperty<Proxy> workingProperty = new SimpleListProperty<>(working);
	
	private String proxyPattern = "[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}.*[0-9]";
	
	private Boolean showBroken = false;
	
	private IFilter<Proxy.Anonymity, Proxy> anonymityFilter = new AnonymityFilter();
	private IFilter<Proxy.Type, Proxy> typeFilter = new TypeFilter();
	private IFilter<Float, Proxy> timeFilter = new TimeFilter();
	private IFilter<Boolean, Proxy> workingFilter = new WorkingFilter();
	private FiltersHandler<Proxy> filtersHandler = new FiltersHandler<>(anonymityFilter, typeFilter, timeFilter, workingFilter);
	
	public ProxyModel(IProxyChecker checker) {
		this.checker = checker;
		
		Callback<Proxy, Observable[]> callback = (Proxy proxy) -> new Observable[]{new ReadOnlyFloatWrapper(proxy.getSpeed())};
		visible = FXCollections.observableArrayList(callback);
		
		visible.addListener(new ListChangeListener<Proxy>() {
			@Override
			public void onChanged(Change<? extends Proxy> c) {
				while (c.next()) {
					if (c.wasUpdated()) {
						MainLogger.log().info(" updated");
					}
				}
			}
		});
	}
	
	public void addProxy(Proxy proxy) {
		if (all.contains(proxy))
			return;
		
		Platform.runLater(() -> {
			all.add(proxy);
			if (proxy.isWorking() || showBroken)
				visible.add(proxy);
		});
	}
	
	public void addProxy(String proxyString) {
		if (!proxyString.matches(proxyPattern)) return;
		Proxy proxy = new Proxy(proxyString);
		addProxy(proxy);
	}
	
	public void check(List<Proxy> proxies) {
		send(() ->
					 checker.checkProxies(proxies), false);
	}
	
	public void filterType(Proxy.Type type) {
		typeFilter.setFilter(type);
		Collection<Proxy> filtered = filtersHandler.filterList(all);
		visible.clear();
		visible.addAll(filtered);
		
	}
	
	public void filterAnonymity(Proxy.Anonymity anonymity) {
		anonymityFilter.setFilter(anonymity);
		Collection<Proxy> filtered = filtersHandler.filterList(all);
		visible.clear();
		visible.addAll(filtered);
	}
	
	public void filterTimeout(Float time) {
		timeFilter.setFilter(time);
		Collection<Proxy> filtered = filtersHandler.filterList(all);
		visible.clear();
		visible.addAll(filtered);
	}
	
	public void showBroken(boolean show) {
		showBroken = show;
		workingFilter.setFilter(show);
		Collection<Proxy> filtered = filtersHandler.filterList(all);
		visible.clear();
		visible.addAll(filtered);
	}
	
	@Override
	public void deleteSelected(List<Proxy> selected) {
		for (Proxy proxy : new CopyOnWriteArrayList<>(selected)) {
			visible.remove(proxy);
			all.remove(proxy);
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
	
	public ReadOnlyIntegerProperty getAllSize() {
		return allProperty.sizeProperty();
	}
	
	public ReadOnlyIntegerProperty getWorkingSize() {
		return workingProperty.sizeProperty();
	}
}
