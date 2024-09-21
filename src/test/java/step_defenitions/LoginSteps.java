package step_defenitions;

import io.cucumber.java.en.*;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import pages.LoginPage;
import utilities.Config;
import utilities.Driver;

public class LoginSteps {
    WebDriver driver = Driver.getDriver();
    LoginPage loginPage = new LoginPage();
    @Given("base URL {string}")
    public void base_url(String string) {
        driver.get(Config.getProperty("saucedemo"));
    }
    @Then("user add user name {string}")
    public void user_add_user_name(String string) {
        loginPage.userName.sendKeys(Config.getProperty("username"));

    }
    @Then("user add password {string}")
    public void user_add_password(String string) {
        loginPage.password.sendKeys(Config.getProperty("password"));

    }
    @Then("user clicks on login button")
    public void user_clicks_on_login_button() {
        loginPage.loginBtn.click();

    }
    @Then("user successful on page")
    public void user_successful_on_page() {
        String url = "https://www.saucedemo.com/inventory.html";
        Assert.assertEquals(url, driver.getCurrentUrl());

    }

}
