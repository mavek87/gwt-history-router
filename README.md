# gwt-history-router

Simple and lightweight gwt module to handle routing in a single web page application

***

## Usage

### 1. Import the module in your gwt.xml file:

`<inherits name="com.matteoveroni.gwthistoryrouter.GwtHistoryRouter" />`


### 2. Create your pages extending the Page superclass. Example:


**PageOne.html (HTML Template/View)**
```
<div id="pageOneId" style="border: 5px solid red;">
    <div>Page One</div>
    <button id="btn_name" type="button">My button</button>
</div>
```

**PageOne.java (Controller)**
```
public class PageOne extends Page {
	
	public PageOne() {
		super(PageOne.class);
	}

	public interface PageOneResource extends ClientBundle {
		PageOneResource INSTANCE = GWT.create(PageOneResource.class);

		@Source("PageOne.html")
                // This is the HTML template of this page
		TextResource getResource();
	}

	@Override
	public String getHtmlText() {
		return PageOneResource.INSTANCE.getResource().getText();
	}
	
	@Override
        // This method is called when the page is used
	public void use() {
		super.use();     // call the superclass use method (which calls the showHtml() method to render the page
		addHandlers();   // add the handlers to the view when the page is used
	}

	private void addHandlers() {
                // Search a button named btn_name in the 
		HTMLButtonElement button = (HTMLButtonElement) document.getElementById("btn_name");
                // Add a listener to handle the click event
		button.addEventListener(BrowserEvents.CLICK, clickEvent -> {
			console.log("botton pressed");
		});
	}
}
```


### 3. Populate the PageRegistry with all your pages, create a new router in your app entry point and call the Router.getInstance().listenUrlChanges() method to startup the router

```
public class MyModuleEntryPoint implements EntryPoint {

	private static final String APP_URL = "http://localhost:8080/app/";

	@Override
	public void onModuleLoad() {
		populatePageRegister();
		// You can use RouterType.GWT_ROUTER or RouterType.ELEMENTAL_ROUTER
		// But the elemental router uses html5 popstate which doesn't works well on old browser like IE 11 or above.
		Router.setup(RouterType.GWT_ROUTER, APP_URL, PageRegister.getMainPage().getURLHash());
		Router.getInstance().listenUrlChanges();
	}

	private void populatePageRegister() {
		final FirstPage firstPage = new FirstPage();
		final PageOne pageOne = new PageOne();
		final PageTwo pageTwo= new PageTwo();
		PageRegister.addPages(firstPage, pageOne, pageTwo);
		PageRegister.setMainPage(firstPage);
	}
}
```


### 4. From your Pages you use the router as shown below. For example to go from PageOne to PageTwo call the method Router.getInstance().go(SecondPage.class) in PageOne. Example:

```
HTMLButtonElement button = (HTMLButtonElement) document.getElementById("btn");
button.addEventListener(BrowserEvents.CLICK, clickEvent -> {
	Router.getInstance().go(SecondPage.class);
});
```

[gwt-history-router wiki page](https://github.com/mavek87/gwt-history-router/wiki/Gwt-History-Router-Wiki)
