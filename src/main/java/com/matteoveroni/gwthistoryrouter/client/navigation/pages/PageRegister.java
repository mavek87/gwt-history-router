package com.matteoveroni.gwthistoryrouter.client.navigation.pages;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PageRegister {

	private static final Map<String, Page> PAGES_BY_NAME = new HashMap<>();
	private static final Map<String, Page> PAGES_BY_URL = new HashMap<>();

	private static Page mainPage;

	public static final void setMainPage(Page page) {
		mainPage = page;
	}

	public static final Page getMainPage() {
		return mainPage;
	}

	public static final void addPage(Page page) {
		PAGES_BY_NAME.put(page.getName(), page);
		PAGES_BY_URL.put(page.getURLHash(), page);
	}

	public static final void addPages(Page... pages) {
		for (Page page : pages) {
			addPage(page);
		}
	}

	public static final void addPages(List<Page> pages) {
		for (Page page : pages) {
			addPage(page);
		}
	}

	public static Page getPageByName(String name) {
		return PAGES_BY_NAME.get(name);
	}

	public static boolean containsPageName(String name) {
		return PAGES_BY_NAME.containsKey(name);
	}

	public static Page getPageByUrlHash(String url) {
		return PAGES_BY_URL.get(url);
	}

	public static boolean containsUrlHash(String url) {
		return PAGES_BY_URL.containsKey(url);
	}

	public static void removePages() {
		PAGES_BY_NAME.clear();
		PAGES_BY_URL.clear();
	}
}
