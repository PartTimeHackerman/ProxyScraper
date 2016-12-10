package org.scraper.main.checker;

import org.scraper.main.Proxy;

public interface IConnectionCheckers {
	ConnectionCheck check(Proxy proxy, Integer timeout);
}
