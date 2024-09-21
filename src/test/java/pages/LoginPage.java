package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import utilities.Driver;

public class LoginPage {
    WebDriver driver;

    public LoginPage(){
        this.driver = Driver.getDriver();
        PageFactory.initElements(driver,this);
    }
    @FindBy(xpath = "//input[@placeholder=\"Username\"]")
    public WebElement userName;
    @FindBy(xpath = "//input[@placeholder=\"Password\"]")
    public WebElement password;
    @FindBy(xpath = "//input[@class=\"submit-button btn_action\"]")
    public WebElement loginBtn;
    String url = "https://www.saucedemo.com/inventory.html";


}
