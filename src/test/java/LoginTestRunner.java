import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;

public class LoginTestRunner extends Setup {
    @Test
    public void doLogin() throws IOException {
        LoginPage loginPage=new LoginPage(driver);
        loginPage.doLogin("admin","admin123");
        Assert.assertTrue( driver.findElement(By.className("oxd-userdropdown")).isDisplayed());
        setCookie();
    }
    public void setCookie() throws IOException {
        // Store cookies in JSON file
        Set<Cookie> cookies = driver.manage().getCookies();
        JSONArray cookiesArray = new JSONArray();

        for (Cookie cookie : cookies) {
            JSONObject cookieJson = new JSONObject();
            cookieJson.put("name", cookie.getName());
            cookieJson.put("value", cookie.getValue());
            cookieJson.put("domain", cookie.getDomain());
            cookieJson.put("path", cookie.getPath());
            cookieJson.put("expiry", cookie.getExpiry());
            cookieJson.put("isSecure", cookie.isSecure());
            cookiesArray.add(cookieJson);
        }

        try (FileWriter file = new FileWriter("./src/test/resources/cookies.json")) {
            file.write(cookiesArray.toJSONString());
        }
    }
}
