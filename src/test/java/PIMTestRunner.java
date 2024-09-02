import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.Set;

public class PIMTestRunner extends Setup {
    @Test
    public void createNewEmployee() throws IOException, ParseException {
        driver.findElements(By.className("oxd-main-menu-item")).get(1).click();
        driver.findElements(By.className("oxd-button")).get(2).click();
    }
    @BeforeTest
    public void getStoredCookie() throws IOException, ParseException {

        // Load cookies from JSON file
        JSONParser parser = new JSONParser();
        JSONArray cookiesArray = (JSONArray) parser.parse(new FileReader("./src/test/resources/cookies.json"));

        // Retrieve all current cookies from the browser
        Set<Cookie> existingCookies = driver.manage().getCookies();

        for (Object obj : cookiesArray) {
            JSONObject cookieJson = (JSONObject) obj;
            String domain = cookieJson.get("domain").toString();
            String name = cookieJson.get("name").toString();
            // Check if the cookie already exists in the browser
            for (Cookie existingCookie : existingCookies) {
                if (existingCookie.getDomain().equals(domain) && existingCookie.getName().equals(name)) {
                    // Replace the cookie value with the value from cookies.json
                    Cookie updatedCookie = new Cookie.Builder(name, cookieJson.get("value").toString())
                            .domain(existingCookie.getDomain())
                            .path(existingCookie.getPath())
                            .expiresOn(existingCookie.getExpiry())
                            .isSecure(existingCookie.isSecure())
                            .build();

                    driver.manage().deleteCookie(existingCookie);
                    driver.manage().addCookie(updatedCookie);
                    break;
                }
            }
        }

        // Refresh the page to apply the updated cookies
        driver.navigate().refresh();

        // Optional: Print the cookies to verify
        Set<Cookie> updatedCookies = driver.manage().getCookies();
        for (Cookie ck : updatedCookies) {
            System.out.println("Domain: " + ck.getDomain() + " | Name: " + ck.getName() + " | Value: " + ck.getValue());
        }
    }


}
