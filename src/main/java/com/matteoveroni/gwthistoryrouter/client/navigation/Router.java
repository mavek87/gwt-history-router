package com.matteoveroni.gwthistoryrouter.client.navigation;

import com.google.gwt.dom.client.DivElement;
import com.matteoveroni.gwthistoryrouter.client.navigation.pages.Page;
import com.matteoveroni.gwthistoryrouter.client.navigation.pages.PageRegister;
import static elemental2.dom.DomGlobal.console;
import static elemental2.dom.DomGlobal.document;
import static elemental2.dom.DomGlobal.self;
import static elemental2.dom.DomGlobal.window;
import elemental2.dom.HTMLDivElement;

public class Router {

	public static final String DIV_ROUTER_CONTAINER_NAME = "router-container";

	private final String appUrl;

	public Router(String appUrl, String appStartingPageHash) {
		this.appUrl = appUrl;

		addRouterContainerToDOM();

		String url = getCurrentUrl();
		if (url.equals(appUrl)) {
			url = appUrl + appStartingPageHash;
		}

		window.history.replaceState(null, "", url);
		handleRequest(url);
	}

	public void listenUrlChanges() {
		self.addEventListener("popstate", event -> {
			handleRequest(getCurrentUrl());
		});
	}

	public static final void go(Class destinationPageClass) {
		final String pageName = destinationPageClass.getSimpleName();
		if (PageRegister.containsPageWithName(pageName)) {
			final Page pageController = PageRegister.getPageByName(pageName);
			pageController.use();
			window.history.pushState(null, pageController.getName(), pageController.getURLHash());
		}
	}

	private void handleRequest(String url) {
		String pageHash = getPageHashFromUrl(url);
		console.log("Router: pagehash=" + pageHash);

		if (PageRegister.containsPageWithUrlHash(pageHash)) {
			Page pageController = PageRegister.getPageByUrlHash(pageHash);
			pageController.use();
		} else {
			redirectToMainPage();
		}
	}

	private void redirectToMainPage() {
		console.log("Router: The page " + getCurrentUrl() + " doesn\'t exists. Redirect to main page.");

		Page mainPage = PageRegister.getMainPage();
		String mainPageUrl = appUrl + mainPage.getURLHash();
		window.history.replaceState(null, "", mainPageUrl);
		mainPage.use();
	}

	private String getPageHashFromUrl(String url) {
		String pageHash = "";

		final String[] splitUrlHashArray = url.split("#");
		if (splitUrlHashArray.length > 0) {
			pageHash = splitUrlHashArray[splitUrlHashArray.length - 1];
		}

		return "#" + pageHash;
	}

	private String getCurrentUrl() {
		return window.location.getOrigin() + window.location.getPathname() + window.location.getHash();
	}

	private void addRouterContainerToDOM() {
		final HTMLDivElement divRouterContainer = (HTMLDivElement) document.createElement(DivElement.TAG);
		divRouterContainer.id = DIV_ROUTER_CONTAINER_NAME;
		divRouterContainer.style.setProperty("min-height", "100%");
		document.body.appendChild(divRouterContainer);
	}
}
