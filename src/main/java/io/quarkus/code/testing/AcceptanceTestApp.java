package io.quarkus.code.testing;

import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@QuarkusMain
public class AcceptanceTestApp implements QuarkusApplication {

    @Override public int run(String... args) throws Exception {
        ChromeDriverService service = null;
        RemoteWebDriver driver = null;
        try {
            service = new ChromeDriverService.Builder()
                    .usingDriverExecutable(Paths.get("/chromedriver/chromedriver").toFile())
                    .usingAnyFreePort()
                    .build();
            service.start();
            ChromeOptions chromeOptions = new ChromeOptions();
            chromeOptions.setHeadless(true);
            chromeOptions.addArguments("--headless","--disable-gpu", "--no-sandbox");
            driver = new RemoteWebDriver(service.getUrl(), chromeOptions);
            driver.manage().window().setSize(new Dimension(1920, 1080));
            driver.get("https://stage.code.quarkus.io");
            Thread.sleep(3000);
            final WebElement element = driver.findElementByClassName("generate-button");
            // do some things
            System.out.println("Successfully loaded with generate button text:");
            System.out.println(element.getAttribute("innerHTML"));
        } finally {
            if(driver != null) {
                driver.quit();
            }
            if(service != null) {
                service.stop();
            }
        }

        return 0;
    }

    private String getDriverName() {
        final String os = System.getProperty("os.name");
        System.out.println("Running on: " + os);
        if (os.contains("Mac")) {
            return "/chromedriver-mac64/chromedriver";
        } else if(os.contains("Linux")){
            return "/chromedriver-linux64/chromedriver";
        }
        throw new UnsupportedOperationException("This os is not supported");
    }
}
