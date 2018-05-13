package com.matteoveroni.gwthistoryrouter.client.navigation.routing;

import com.matteoveroni.gwthistoryrouter.client.navigation.pages.Page;
import com.matteoveroni.gwthistoryrouter.client.navigation.pages.PageRegister;
import static elemental2.dom.DomGlobal.console;
import static elemental2.dom.DomGlobal.window;

/**
 *
 * @author Matteo Veroni
 */
public class ElementalRouter extends Router {

	public ElementalRouter() {
		String url = getCorrectInitialUrl(appUrl, appStartingPageHash);
		console.log("ElementalRouter built: initial url = " + url);

		window.history.replaceState(null, "", url);
		handleRequest(url);
	}

	@Override
	public void listenUrlChanges() {
		window.addEventListener("popstate", event -> {
			console.log("postate fired");
			handleRequest(getCurrentUrl());
		});
	}

	@Override
	public void go(Class destinationPageClass) {
		String pageName = destinationPageClass.getSimpleName();
		if (PageRegister.containsPageName(pageName)) {
			final Page page = PageRegister.getPageByName(pageName);
			page.use();
			window.history.pushState(null, page.getName(), page.getURLHash());
		}
	}

	@Override
	protected void redirectToMainPage() {
		console.log("Router: The page " + getCurrentUrl() + " doesn\'t exists. Redirect to main page.");

		final Page mainPage = PageRegister.getMainPage();
		mainPage.use();

		String mainPageUrl = appUrl + mainPage.getURLHash();
		window.history.replaceState(null, "", mainPageUrl);
	}
}
