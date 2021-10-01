package io.quarkus.code.testing;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;

import java.util.List;

@QuarkusMain
public class AcceptanceTestApp implements QuarkusApplication {

    @Override public int run(String... args) throws Exception {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                    .setArgs(List.of("--headless","--disable-gpu", "--no-sandbox")));
            BrowserContext context = browser.newContext();

            // Open new page
            Page page = context.newPage();
            page.navigate("https://stage.code.quarkus.io");
            final ElementHandle generateButton = page.waitForSelector(".generate-button");
            System.out.println(generateButton.textContent());
        }
        return 0;
    }

}
