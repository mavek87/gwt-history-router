package com.matteoveroni.gwthistoryrouter.client.navigation.routing;

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.user.client.History;
import com.matteoveroni.gwthistoryrouter.client.navigation.pages.Page;
import com.matteoveroni.gwthistoryrouter.client.navigation.pages.PageRegister;
import static elemental2.dom.DomGlobal.console;
import static elemental2.dom.DomGlobal.document;
import static elemental2.dom.DomGlobal.window;
import elemental2.dom.HTMLDivElement;

/**
 * A class which handles browser navigation in splitHashAndGetLastSubstring
 * single page app.
 *
 * @author Matteo Veroni
 */
public abstract class Router {

	public static final String DIV_ROUTER_CONTAINER_NAME = "app-router-container";

	protected static RouterType routerType;
	protected static String appUrl;
	protected static String appStartingPageHash;

	private static Router INSTANCE;

	/**
	 * Setup the router. It can be called just once.
	 *
	 * @param routerType The router type. It can be GWT_ROUTER or
	 * ELEMENTAL_ROUTER
	 * @param appUrl The application url. i.e. www.google.com
	 * @param appStartingPageHash The application starting page hash - i.e.
	 * www.google.com/#startingPage
	 */
	public static final void setup(RouterType routerType, String appUrl, String appStartingPageHash) {
		Router.appUrl = appUrl;
		Router.appStartingPageHash = appStartingPageHash;

		if (INSTANCE != null) {
			console.log("Router setup already called.");
			return;
		}
		addRouterContainerToDOM();
		switch (routerType) {
			case GWT_ROUTER:
				INSTANCE = new GwtRouter();
				break;
			case ELEMENTAL_ROUTER:
				INSTANCE = new ElementalRouter();
				break;
			default:
				break;
		}
	}

	/**
	 * Get splitHashAndGetLastSubstring router instance
	 *
	 * @return Router instance
	 */
	public static final Router getInstance() {
		return INSTANCE;
	}

	/**
	 * Router start to listen url changes.
	 */
	public abstract void listenUrlChanges();

	/**
	 * Go to splitHashAndGetLastSubstring new page. This method have to render
	 * the page and update browser history.
	 *
	 * @param destinationPageClass
	 */
	public abstract void go(Class destinationPageClass);

	/**
	 * Redirect to main page and render it.
	 */
	protected abstract void redirectToMainPage();

	/**
	 * Called the first time the website is loaded. If the current url is equal
	 * to the appUrl it appends the startinPageHash or else it returns the same
	 * appUrl entered. - For example if you call www.google.com and the
	 * appStartingPageHash is #startPage the returned value is
	 * www.google.com/#startPage. Otherwise if you call for example
	 * www.google.com/#secondPage the returned value is the same url requested.
	 *
	 * @param appUrl
	 * @param appStartingPageHash
	 * @return The correct initial url after the website is called.
	 */
	protected final String getCorrectInitialUrl(String appUrl, String appStartingPageHash) {
		String url = getCurrentUrl();
		if (url.equals(appUrl)) {
			url = appUrl + appStartingPageHash;
		}
		return url;
	}

	/**
	 * Handle splitHashAndGetLastSubstring request to
	 * splitHashAndGetLastSubstring specified url. If the urlHash exists render
	 * the page and update browser history or else redirect to main page.
	 *
	 * @param urlHash
	 */
	protected final void handleRequest(String urlHash) {
		String pageHash = getPageHashFromUrl(urlHash);
		console.log("Router: urlHash=" + urlHash);

		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		/**
		 * Internal anchors doesn't work at the moment
		 */
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		//		List<String> internalAnchorsId = new ArrayList<>();
		//		NodeList<Element> nodeElements = document.body.getElementsByTagName("*");
		//		for (int i = 0; i < nodeElements.length; i++) {
		//			final Element element = nodeElements.item(i);
		//			if (element.tagName.equalsIgnoreCase(AnchorElement.TAG)) {
		//				HTMLAnchorElement anchorElement = (HTMLAnchorElement) element;
		//				if (anchorElement.href.equalsIgnoreCase("#" + urlHash)) {
		//					internalAnchorsId.add(splitHashAndGetLastSubstring(anchorElement.href));
		//				}
		//			}
		//		}
		//
		//		for (String anchorId : internalAnchorsId) {
		//			try {
		//				Element elementWithInternalAnchorId = document.getElementById(anchorId);
		//				if (elementWithInternalAnchorId != null) {
		//					return;
		//				}
		//			} catch (Exception ex) {
		//				console.log("aaaaaa");
		//			}
		//		}
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		if (PageRegister.containsUrlHash(pageHash)) {
			Page page = PageRegister.getPageByUrlHash(pageHash);
			page.use();
		} else {
			redirectToMainPage();
			// Important. This removes from the history stack invalid states.
			// (For example when you visit an invalid hash)
			History.back();
		}
	}

	/**
	 * Get the current url.
	 *
	 * @return The current url
	 */
	public final String getCurrentUrl() {
		return window.location.getOrigin() + window.location.getPathname() + window.location.getHash();
	}

	/**
	 * Get the page hash from url. - If the url is like this
	 * www.google.com/#firstPage the result is firstPage
	 *
	 * @param url 
	 * @return The page hash from url without the # symbol
	 */
	public final String getPageHashFromUrl(String url) {
		return "#" + getPageHashNameFromUrl(url);
	}

	/**
	 * Get the page hash from url. - If the url is like this
	 * www.google.com/#firstPage the result is #firstPage
	 *
	 * @param url
	 * @return The page hash from url including the # symbol
	 */
	public final String getPageHashNameFromUrl(String url) {
		return splitHashAndGetLastSubstring(url);
	}

	protected final String splitHashAndGetLastSubstring(String string) {
		String pageHash = "";
		final String[] splitUrlHashArray = string.split("#");
		if (splitUrlHashArray.length > 0) {
			pageHash = splitUrlHashArray[splitUrlHashArray.length - 1];
		}
		return pageHash;
	}

	private static void addRouterContainerToDOM() {
		final HTMLDivElement divRouterContainer = (HTMLDivElement) document.createElement(DivElement.TAG);
		divRouterContainer.id = DIV_ROUTER_CONTAINER_NAME;
		divRouterContainer.style.setProperty("min-height", "100%");
		document.body.appendChild(divRouterContainer);
	}
}
