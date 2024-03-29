package com.mavrck.gridbot.server;

import java.util.logging.Logger;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;


public class WebDriverFactory 
{
    private static final Logger LOGGER = Logger.getLogger(WebDriverFactory.class.getName());

    public static WebDriver createWebDriver()
    {
        setChromeDriverPath();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-notifications");
	String user_data_dir = "/home/denni-15540/.config/google-chrome/Profile 2/";
	options.addArguments("--user-data-dir=" + user_data_dir);
       	options.setBinary(ConfManager.getChromePath());
        WebDriver driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        return driver;        
    }    

    public static void setChromeDriverPath()
    {
        System.setProperty("webdriver.chrome.driver", ConfManager.getChromeDriverPath());
    }

}
