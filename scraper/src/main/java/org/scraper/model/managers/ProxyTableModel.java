package org.scraper.model.managers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import org.scraper.model.Proxy;
import org.scraper.model.checker.ProxyChecker;
import org.scraper.model.scrapers.RegexMatcher;

public class ProxyTableModel {
	
	private ObservableList<Proxy> visible = FXCollections.observableArrayList();
	private ObservableList<Proxy> all = FXCollections.observableArrayList();
	
	private Boolean showBroken = false;
	private Proxy.Type visibleType = Proxy.Type.ALL;
	private Proxy.Anonymity visibleAnonymity = Proxy.Anonymity.ALL;
	
	private ProxyManager proxyManager;
	
	private TableView<Proxy> table;
	
	private ProxyChecker checker;
	
	private String proxyPattern = "[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}.*[0-9]";
	
	public ProxyTableModel(ProxyChecker checker, TableView<Proxy> table) {
		this.checker = checker;
		this.table = table;
	}
	
	public void addProxy(Proxy proxy) {
		Platform.runLater(() -> {
			all.add(proxy);
			if (proxy.isWorking() || showBroken)
				visible.add(proxy);
		});
	}
	
	public void addProxy(String proxyString) {
		if (!proxyString.matches(proxyPattern)) return;
		
		Proxy proxy = RegexMatcher.matchOne(proxyString);
		proxy = proxyManager.getIfPresent(proxy);
		visible.add(proxy);
		
	}
	
	public void filterType(Proxy.Type type) {
		visible.clear();
		visibleType = type;
		all.forEach(proxy -> {
			if ((proxy.getType() == type || type == Proxy.Type.ALL)
					&& (proxy.getAnonymity() == visibleAnonymity || visibleAnonymity == Proxy.Anonymity.ALL)
					&& (showBroken || proxy.isWorking()))
				visible.add(proxy);
		});
	}
	
	public void filterAnonymity(Proxy.Anonymity anonymity) {
		visible.clear();
		visibleAnonymity = anonymity;
		
		all.forEach(proxy -> {
			if ((proxy.getAnonymity() == anonymity || anonymity == Proxy.Anonymity.ALL)
					&& (proxy.getType() == visibleType || visibleType == Proxy.Type.ALL)
					&& (showBroken || proxy.isWorking()))
				visible.add(proxy);
		});
	}
	
	public void filterTimeout(Float time) {
		visible.clear();
		all.forEach(proxy -> {
			if (proxy.getSpeed() <= time)
				visible.add(proxy);
		});
	}
	
	public void showBroken(boolean show) {
		showBroken = show;
		visible.clear();
		
		all.forEach(proxy -> {
			if (proxy.isWorking() || show)
				visible.add(proxy);
		});
	}
	
	public void check() {
		ObservableList<Proxy> selected = table.getSelectionModel().getSelectedItems();
		
		if (selected.size() > 0) {
			checker.checkProxies(selected.subList(0, selected.size() - 1), false);
		} else {
			ObservableList<Proxy> all = table.getItems();
			checker.checkProxies(all.subList(0, all.size() - 1), false);
		}
	}
	
	public void setProxyManager(ProxyManager proxyManager) {
		this.proxyManager = proxyManager;
	}
	
	public ObservableList<Proxy> getVisible() {
		return visible;
	}
	
}
