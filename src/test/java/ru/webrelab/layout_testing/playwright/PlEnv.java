package ru.webrelab.layout_testing.playwright;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

public class PlEnv {
    public static final PlEnv INSTANCE = new PlEnv();
    private Browser browser;
    private Page page;

    public Browser getBrowser() {
        if (browser == null) {
            init();
        }
        return browser;
    }

    public Page getPage() {
        if (page == null) {
            init();
        }
        return page;
    }

    private void init() {
        browser = Playwright.create().chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        page = browser.newPage();
    }
}
