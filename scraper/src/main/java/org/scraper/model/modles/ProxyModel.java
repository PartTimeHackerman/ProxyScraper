package org.scraper.model.modles;

import javafx.application.Platform;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.scraper.model.Proxy;
import org.scraper.model.checker.ProxyChecker;

import java.util.List;

public class ProxyModel {
	
	private ProxyChecker checker;
	
	private ObservableList<Proxy> visible = FXCollections.observableArrayList();
	
	private ObservableList<Proxy> working = FXCollections.observableArrayList();
	private ListProperty<Proxy> workingProperty = new SimpleListProperty<>(working);
	
	private ObservableList<Proxy> all = FXCollections.observableArrayList();
	private ListProperty<Proxy> allProperty = new SimpleListProperty<>(all);
	
	private boolean showBroken = false;
	
	private Proxy.Type visibleType = Proxy.Type.ALL;
	
	private Proxy.Anonymity visibleAnonymity = Proxy.Anonymity.ALL;
	
	public ProxyModel(ProxyChecker checker) {
		this.checker = checker;
	}
	
	
	public void addProxy(Proxy proxy) {
		Platform.runLater(() -> {
			all.add(proxy);
			
			if (proxy.isWorking() || showBroken)
				visible.add(proxy);
			
			if (proxy.isWorking())
				working.add(proxy);
		});
	}
	
	
	public void check(List<Proxy> proxies) {
		checker.checkProxies(proxies);
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
			if (proxy.isWorking() || showBroken)
				visible.add(proxy);
		});
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
