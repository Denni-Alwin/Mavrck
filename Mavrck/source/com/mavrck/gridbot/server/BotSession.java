package com.mavrck.gridbot.server;

import java.io.File; 
import java.io.InputStream; 
import java.io.InputStreamReader; 
import java.io.BufferedReader; 
import java.util.logging.Logger;

import org.json.JSONObject; 
import org.json.JSONObject; 
import org.json.JSONTokener; 
import org.json.simple.parser.JSONParser;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.Color;

import com.mavrck.gridbot.server.Constants.TradePhase;
import com.mavrck.gridbot.server.Constants.TradeStatus;
import com.mavrck.gridbot.server.util.Colour;
import com.mavrck.gridbot.server.util.Timeouts;
import com.mavrck.gridbot.server.util.Util;
import com.mavrck.gridbot.server.util.WebDriverUtil;


import java.util.Scanner; 

public class BotSession 
{
    private static final Logger LOGGER = Logger.getLogger(BotSession.class.getName());

    private WebDriver webDriver = null;

    private static TradePhase tradePhase = null;

    private static TradeStatus testStatus = null;

    public boolean startBotSession() throws Exception
    {
        this.webDriver = WebDriverFactory.createWebDriver();
        Thread.sleep(3000);
        webDriver.get(ConfManager.getBaseUrl());
        login(webDriver);

        return true;
    }

    public boolean login(WebDriver webDriver) 
    {
        try 
        {
            //if(!ConfManager.getLoginAccess())
           Boolean bool = false;
            if(bool)
            {
                Util.waitAround(Timeouts.ONE_SECOND_INTERVAL);
                System.out.println(ConfManager.getUserName());
                WebDriverUtil.sendKeysToElement(webDriver, "Send keys to UserName Box", By.xpath("//*[@id='username']"), ConfManager.getUserName());
                WebDriverUtil.clickElement(webDriver, "Clicking Next Button", By.xpath("//*[@id='click_login_submit']"));
                System.out.println(ConfManager.getPassword());
                WebDriverUtil.sendKeysToElement(webDriver, "Send keys to Password Box", By.xpath("//*[@id='wrap_app']/main/div/div[3]/div/form/div/div[2]/div/input"), 0, ConfManager.getPassword());
                WebDriverUtil.clickElement(webDriver, "Clicking Next Button", By.xpath("//*[@id='click_login_submit']"));
                WebDriverUtil.clickElement(webDriver, "Clicking Next Button", By.xpath("//*[@id='mfa-email-input']"));
            }
            else
            {   
                Scanner sc = new Scanner(System.in);
                String loggedin = sc.nextLine();
                if(loggedin == "loggedin")
                {
                    return true;
                } 
            }
            return true;
        }
        catch(Exception e)
        {
            return false;
        }
    }

    public boolean checkTicker() throws Exception
    {  
        WebElement tickerElement = WebDriverUtil.getElement(webDriver, "HIGHLIGHT", By.xpath("//*[@id='selected-coin-market-chart-section']"));
        String tickerString = tickerElement.getText();
        LOGGER.info("[checkTicker] [TICKER] " + tickerString + " " +ConfManager.getTicker() + "/" + ConfManager.getCurrency());

        if(tickerString.equals(ConfManager.getTicker() + "/" + ConfManager.getCurrency()))
        {
            LOGGER.info("[checkTicker] [TICKER] " + tickerString);
            return true;
        }
        else
        {
            LOGGER.info("[checkTicker] [TICKER] [TICKER NOT EQUAL] [SELECTING GIVEN TICKER]");
            WebDriverUtil.sendKeysToElement(webDriver, "", By.xpath("//*[@id=\"root\"]/div/div[2]/div/div[1]/div/div[1]/div[1]/div/input"), ConfManager.getTicker());
            WebDriverUtil.clickElement(webDriver, "", By.xpath("//*[@id=\"root\"]/div/div[2]/div/div[1]/div/div[2]"));
            return checkTicker();
        }

    }
    public boolean buy() throws Exception 
    {
        //Lowest-price
        WebDriverUtil.clickElement(webDriver, "", By.xpath("//*[@id='exchange-buy-tab']"));
        //lowest price
        //WebDriverUtil.clickElement(webDriver, "", By.xpath("//*[@id='root']/div/div[3]/div/div[3]/div[2]/div/div[2]/div/form/div[2]/div/div/div[2]/span"));
        //WebDriverUtil.executeJsScript(webDriver, "document.querySelector('#buy-total').value=''");
        WebDriverUtil.getElement(webDriver," ",By.xpath("//*[@id='buy-total']")).clear();
        Thread.sleep(1000);
        WebDriverUtil.sendKeysToElement(webDriver, "", By.xpath("//*[@id='buy-total']"), "1000");
        WebDriverUtil.clickElement(webDriver, "", By.xpath("//*[@id='root']/div/div[2]/div/div[3]/div[2]/div/div[2]/div/form/button"));
        //Confirm buy
        WebDriverUtil.clickElement(webDriver, "", By.xpath("//*[@id='confirm-popup-stop-limit-order']"));
        LOGGER.info(Colour.ANSI_BOLD + Colour.TextDecor.ANSI_GREEN + "[BOTSESSION] [BUY] " + this.getCurrentPrice() + Colour.ANSI_RESET);
        BotRoom.updateTradeStatus(Constants.TradeStatus.BUY);
        BotRoom.updateBuyPriceAction();
        return true;
    }

    public boolean sell() throws Exception
    {
        WebDriverUtil.clickElement(webDriver, "", By.xpath("//*[@id='exchange-sell-tab']"));
        //WebDriverUtil.clickElement(webDriver, "", By.xpath("//*[@id='root']/div/div[3]/div/div[3]/div[2]/div/div[2]/div/form/div[2]/div/div/div[2]/span"));
        //WebDriverUtil.executeJsScript(webDriver, "document.querySelector('#sell-total').value=''");
        WebDriverUtil.getElement(webDriver," ",By.xpath("//*[@id='sell-total']")).clear();
        Thread.sleep(1000);
        WebDriverUtil.sendKeysToElement(webDriver, "", By.xpath("//*[@id='sell-total']"), "1000");
        WebDriverUtil.clickElement(webDriver, "", By.xpath("//*[@id='root']/div/div[2]/div/div[3]/div[2]/div/div[2]/div/form/button"));
        
        //confirm sell
        WebDriverUtil.clickElement(webDriver, "", By.xpath("//*[@id='confirm-popup-stop-limit-order']"));
        //clear

        LOGGER.info(Colour.ANSI_BOLD + Colour.TextDecor.ANSI_RED + "[BOTSESSION] [SELL] " + this.getCurrentPrice() + Colour.ANSI_RESET);
        BotRoom.updateTradeStatus(Constants.TradeStatus.SELL);
        BotRoom.updateSellPriceAction();
        return true;
    }

    public WebDriver getWebDriver()
    {
        return this.webDriver;
    }

    public float getCurrentPrice() throws Exception
    {
        WebElement exchangePrice = WebDriverUtil.getElement(webDriver, "HIGHLIGHT", By.xpath("//*[@id='exchange-last-price']"));
        String currentPrice = exchangePrice.getText();
        String str=currentPrice.replaceAll("[\\p{Sc}-,]", "");  
        LOGGER.info(Colour.ANSI_BOLD + Colour.Background.ANSI_BLUE_BACKGROUND + "[getCurrentPrice] [CURRENT PRICE] " + currentPrice + Colour.ANSI_RESET);
        float currentPriceInt = Float.parseFloat(str);
        return currentPriceInt;
    }
    
}
