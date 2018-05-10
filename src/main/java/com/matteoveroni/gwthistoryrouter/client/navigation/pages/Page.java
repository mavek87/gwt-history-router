package com.matteoveroni.gwthistoryrouter.client.navigation.pages;

import com.matteoveroni.gwthistoryrouter.client.navigation.Router;
import static elemental2.dom.DomGlobal.document;
import elemental2.dom.HTMLDivElement;

public abstract class Page {

	private final String name;

	public Page(Class clazz) {
		name = clazz.getSimpleName();
	}

	public String getName() {
		return name;
	}

	public String getURLHash() {
		return "#" + name.toLowerCase();
	}

	public abstract String getHtmlText();

	public void use() {
		showHTML();
	}

	public void showHTML() {
		final HTMLDivElement divRouterContainer = (HTMLDivElement) document.getElementById(Router.DIV_ROUTER_CONTAINER_NAME);
		divRouterContainer.innerHTML = "";
		divRouterContainer.innerHTML = getHtmlText();
	}
}
