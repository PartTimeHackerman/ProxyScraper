package org.scraper.model.checker;

import org.scraper.model.Proxy;

public interface IConnectionCheckers {
	ConnectionCheck check(Proxy proxy, Integer timeout);
}
