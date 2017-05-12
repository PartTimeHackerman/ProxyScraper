package scraper.checker;

import scraper.Proxy;

public interface IConnectionCheckers {
	ConnectionCheck check(Proxy proxy, Integer timeout);
}
