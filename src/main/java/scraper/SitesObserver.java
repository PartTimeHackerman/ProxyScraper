package scraper;

import scraper.data.Site;
import scraper.data.SitesRepo;
import scraper.manager.AssignManager;
import scraper.scraper.ScrapeType;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class SitesObserver implements Observer{
	
	private SitesRepo sitesRepo;
	
	private AssignManager assignManager;
	
	public SitesObserver(SitesRepo sitesRepo, AssignManager assignManager) {
		this.sitesRepo = sitesRepo;
		this.assignManager = assignManager;
	}
	
	
	@Override
	public void update(Observable o, Object arg) {
		if (arg instanceof List) {
			List<Object> list = (List) arg;
			list.forEach(this::handleSite);
		} else {
			handleSite(arg);
		}
	}
	
	private void handleSite(Object arg) {
		if (!(arg instanceof Site)) return;
		
		Site site = (Site) arg;
		if (site.getType() == ScrapeType.UNCHECKED)
			assignManager.assign(site);
		sitesRepo.addSite(site);
	}
}
