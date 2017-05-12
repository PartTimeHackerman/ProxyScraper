package scraper;


public class ScraperCLI {
	
	private static Scraper model;
	
	private static GeneralOptions generalOptions;
	
	
	public static void main(String[] args) {
		
		if (args.length == 0) {
			System.out.println("Add some args :P");
		}
		
		model = new Scraper();
		generalOptions = new GeneralOptions(model.getProxyRepo(), model.getCheckOnFly(), model.getLimiter(), model.getPool());
		
	}
}
