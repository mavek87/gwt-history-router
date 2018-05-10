package com.matteoveroni.gwthistoryrouter.client.navigation.pages;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PageRegister {

	private static final Map<String, Page> pagesByName = new HashMap<>();
	private static final Map<String, Page> pagesByUrl = new HashMap<>();

	private static Page mainPage;

	public static final void setMainPage(Page page) {
		mainPage = page;
	}

	public static final Page getMainPage() {
		return mainPage;
	}

	public static final void addPage(Page page) {
		pagesByName.put(page.getName(), page);
		pagesByUrl.put(page.getURLHash(), page);
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
		return pagesByName.get(name);
	}

	public static boolean containsPageWithName(String name) {
		return pagesByName.containsKey(name);
	}

	public static Page getPageByUrlHash(String url) {
		return pagesByUrl.get(url);
	}

	public static boolean containsPageWithUrlHash(String url) {
		return pagesByUrl.containsKey(url);
	}

	public static void removePages() {
		pagesByName.clear();
		pagesByUrl.clear();
	}
}
