package com.matteoveroni.gwthistoryrouter.client.navigation.routing;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.user.client.History;
import com.matteoveroni.gwthistoryrouter.client.navigation.pages.Page;
import com.matteoveroni.gwthistoryrouter.client.navigation.pages.PageRegister;

import static elemental2.dom.DomGlobal.console;

/**
 * 
 * @author Matteo Veroni
 */
public class GwtRouter extends Router {

	protected GwtRouter() {
		String url = getCorrectInitialUrl(appUrl, appStartingPageHash);
		console.log("GWTRouter built: initial url = " + url);

		History.replaceItem(getPageHashNameFromUrl(url), true);
		handleRequest(url);
	}

	@Override
	public void listenUrlChanges() {
		History.addValueChangeHandler((ValueChangeEvent<String> event) -> {
			console.log("History ValueChangeEvent<String> fired");
			handleRequest(getCurrentUrl());
		});
	}

	@Override
	public void go(Class destinationPageClass) {
		String pageName = destinationPageClass.getSimpleName();
		if (PageRegister.containsPageName(pageName)) {
			Page page = PageRegister.getPageByName(pageName);
			page.use();
			History.newItem(splitHashAndGetLastSubstring(page.getURLHash()), true);
		}
	}

	@Override
	protected void redirectToMainPage() {
		console.log("Router: The page " + getCurrentUrl() + " doesn\'t exists. Redirect to main page.");
		
		Page mainPage = PageRegister.getMainPage();
		mainPage.use();
		
		History.replaceItem(splitHashAndGetLastSubstring(mainPage.getURLHash()), true);
	}
}
