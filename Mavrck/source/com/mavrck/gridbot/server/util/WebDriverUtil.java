//$Id$
package com.mavrck.gridbot.server.util;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;


public class WebDriverUtil
{

	private static final Logger LOGGER = Logger.getLogger(WebDriverUtil.class.getName());

	/**
	 * Finds and clicks an element with given description and a selector.
	 *
	 * @param webDriver the webdriver.
	 * @param elementDescription given description of the element, for logging purpose
	 * @param selector given selector to locate element
	 * @param action true to use Actions to click the element instead of WebElement.click()
	 */
	public static void clickElement(WebDriver webDriver, String elementDescription, By selector,boolean action) throws Exception 
	{
		clickElement(webDriver, elementDescription, selector, 0, action);
	}

	/**
	 * Finds and clicks an element with given description and a selector.
	 *
	 * @param webDriver the webdriver.
	 * @param elementDescription given description of the element, for logging purpose
	 * @param selector given selector to locate element
	 */
	public static void clickElement(WebDriver webDriver, String elementDescription, By selector) throws Exception 
	{
		clickElement(webDriver, elementDescription, selector, -1, false);
	}

	/**
	 * Finds and clicks an element with given description and a selector.
	 *
	 * @param webDriver the webdriver.
	 * @param elementDescription given description of the element, for logging purpose
	 * @param selector given selector to locate element
	 * @param index index of the element with given selector
	 * @param action true to use Actions to click the element instead of WebElement.click()
	 */
	public static void clickElement(WebDriver webDriver, String elementDescription, By selector,int index, boolean action) throws Exception 
	{
		WebElement element = getElement(webDriver, elementDescription, selector, index);
		try
		{
			clickElement(webDriver, element, action);
			Util.waitAround(Timeouts.ONE_SECOND_INTERVAL / 2);
		} 
		catch (Exception e) 
		{
			throw e;
		}
	}

	/**
	 * Click element.
	 *
	 * @param webDriver the webdriver
	 * @param element a given WebElement
	 * @param action true to use Actions to click the element instead of WebElement.click()
	 */
	public static void clickElement(WebDriver webDriver, WebElement element, boolean action) 
	{
		if (action) 
		{
			Actions actions = new Actions(webDriver);
			actions.moveToElement(element).click().perform();
		} 
		else 
		{
			element.click();
		}
	}

	/**
	 * Close drivers.
	 *
	 * @param webDriverList the web driver list
	 */
	public static void closeDrivers(List<WebDriver> webDriverList) 
	{
		if (!webDriverList.isEmpty()) 
		{
			LOGGER.info("Closing down webDrivers");
			for (WebDriver webDriver : webDriverList) 
			{
				if (webDriver != null) {
					try 
					{
						// Open about:config in case of fennec (Firefox for Android) and close.
						if (((RemoteWebDriver) webDriver).getCapabilities().getBrowserName().equalsIgnoreCase("fennec")) 
						{
							webDriver.get("about:config");
							webDriver.close();
						}
						webDriver.quit();
					} 
					catch (Exception e) 
					{
						LOGGER.log(Level.WARNING, "Exception while closing/quitting the WebDriver", e);
					}
				}
			}
			// prevent this get called more than once
			webDriverList.clear();
		}
	}

	/**
	 * Performs a double tap action at a specific point
	 *
	 * @param webDriver the web driver
	 * @param x the x
	 * @param y the y
	 */
	public static void doubleTap(WebDriver webDriver, int x, int y) 
	{
		new Actions(webDriver).moveByOffset(x, y).doubleClick().perform();
	}

	/**
	 * Find all elements with the corresponding locator and iterate through the list to find the one
	 * that is required.
	 *
	 * @param webDriver the web driver
	 * @param locator locator to locate the elements
	 * @param attribute type of the attribute we
	 * @param value the value
	 * @return web element
	 */
	public static WebElement findElementWithCondition(WebDriver webDriver, By locator, String attribute, String value) 
	{
		List<WebElement> elements = webDriver.findElements(locator);
		for (WebElement element : elements) 
		{
			switch (attribute) 
			{
				case "text": 
					{
						if (element.getText().equalsIgnoreCase(value) || element.getCssValue("value").equalsIgnoreCase(value)) 
						{
							return element;
						}
						break;
					}
				default: 
					{
						if (element.getAttribute(attribute).equalsIgnoreCase(value)) 
						{
							return element;
						}
						break;
					}
			}
		}
		return null;
	}

	/**
	 * Get the web element from web driver
	 *
	 * @param webDriver the webdriver.
	 * @param elementDescription given description of the element, for logging purpose
	 * @param selector given selector to locate element
	 * @return the corresponding WebElement Object
	 * @throws Exception if an the element can not be found.
	 */
	public static WebElement getElement(WebDriver webDriver, String elementDescription, By selector) throws Exception 
	{
		return getElement(webDriver, elementDescription, selector, -1);
	}

	/**
	 * Get the web element from web driver with index
	 *
	 * @param webDriver the webdriver.
	 * @param elementDescription given description of the element, for logging purpose
	 * @param selector given selector to locate elementwaitForElement
	 * @param index index of element with given selector
	 * @return the corresponding WebElement Object
	 * @throws Exception if an the element can not be found.
	 */
	public static WebElement getElement(WebDriver webDriver, String elementDescription, By selector, int index) throws Exception 
	{
		waitForElement(webDriver, selector, Timeouts.TEN_SECOND_INTERVAL);
		if (index == -1) 
		{
			try 
			{
				return webDriver.findElement(selector);
			} 
			catch (Exception e) 
			{
				throw e;
			}
		} 
		else 
		{
			WebElement element = webDriver.findElements(selector).get(index);
			if (element != null) 
			{
				return element;
			}
			else
			{
				return null;
				//throw e;
			}
		}
	}

	/**
	 * Switches to a new tab on a webdriver.
	 *
	 * @param webDriver the webdriver
	 * @throws Exception the kite test exception
	 */
	public static String getOtherWindowHandler(WebDriver webDriver) throws Exception 
	{
		String currentWindowHandler = webDriver.getWindowHandle();
		for (String windowHandler : webDriver.getWindowHandles()) 
		{
			if (windowHandler != currentWindowHandler) 
			{
				return windowHandler;
			}
		}
		return currentWindowHandler;
	}

	/**
	 * Finds and get text from an element with given name and a given selector.
	 *
	 * @param webDriver the webdriver.
	 * @param elementDescription given name of the element, for logging purpose
	 * @param selector given selector to locate element
	 * @return the text
	 * @throws Exception if an Exception occurs during method execution or fail.
	 */
	public static String getText(WebDriver webDriver, String elementDescription, By selector) throws Exception 
	{
		WebElement element = getElement(webDriver, elementDescription, selector);
		try 
		{
			String text = element.getText().trim();
			return text.isEmpty() ? null : text;
		} 
		catch (Exception e) 
		{
			throw e;
		}
	}

	/**
	 * Returns the dimension of the window if possible, if exception, returns a fixed one;
	 *
	 * @param webDriver the web driver
	 * @return window size
	 */
	public static Dimension getWindowSize(WebDriver webDriver) 
	{
		int fixedMeasure = 1024;
		try 
		{
			return webDriver.manage().window().getSize();
		} 
		catch (Exception e) 
		{
			return new Dimension(fixedMeasure, fixedMeasure);
		}
	}

	/**
	 * Looks for the number of elements with given selector
	 *
	 * @param webDriver the webdriver.
	 * @param selector to locate the element
	 * @param expectedNumber expected number of element visible on page.
	 * @return whether there are an exact number of element.
	 */
	private static boolean hasExpectedNumberOfElements(WebDriver webDriver, By selector,int expectedNumber) 
	{
		return webDriver.findElements(selector).size() == expectedNumber;
	}

	public static Boolean isAlive(WebDriver webDriver) 
	{
		try 
		{
			webDriver.getCurrentUrl();
			return true;
		} 
		catch (Exception ex) 
		{
			return false;
		}
	}

	/**
	 * @return true if it is electron
	 */
	public static boolean isElectron(WebDriver webDriver) 
	{
		Capabilities capabilities = ((RemoteWebDriver) webDriver).getCapabilities();
		return "chrome".equals(capabilities.getBrowserName()) && capabilities.getBrowserVersion().isEmpty();//No I18N
	}

	/**
	 * @return true if it is chrome
	 */
	public static boolean isChrome(WebDriver webDriver) 
	{
		Capabilities capabilities = ((RemoteWebDriver) webDriver).getCapabilities();
		return "chrome".equals(capabilities.getBrowserName()); //No I18N
	}


	/**
	 * Finds out if an element with given name and xpath value is visible on current page.
	 *
	 * @param webDriver the webdriver.
	 * @param selector given selector to locate element
	 * @return true if the element is visible
	 */
	public static boolean isVisible(WebDriver webDriver, By selector) 
	{
		try 
		{
			WebElement element = webDriver.findElement(selector);
			return element != null && element.isDisplayed();
		} 
		catch (Exception e) 
		{
			return false;
		}
	}

	/**
	 * Load the page, waiting for document.readyState to be complete
	 *
	 * @param url the url of the web page
	 * @param timeoutInSeconds timeout in seconds
	 * @param webDriver the webdriver.
	 */
	public static void loadPage(WebDriver webDriver, String url, int timeoutInSeconds) 
	{
		webDriver.get(url);
		WebDriverWait driverWait = new WebDriverWait(webDriver, Duration.ofSeconds(timeoutInSeconds));
		driverWait.until(driver ->
				((JavascriptExecutor) driver)
				.executeScript("return document.readyState") //No I18N
				.equals("complete")); //No I18N
	}

	/**
	 * Resizes the windows to the screen's available width and height
	 */
	public static void maximizeCurrentWindow(WebDriver webDriver) throws Exception 
	{
		if (!isElectron(webDriver)) 
		{
			try 
			{
				String getScreenHeight = "return screen.availHeight"; //No I18N
				String getScreenWidth = "return screen.availWidth"; //No I18N
				int screenHeight = (int) ((long) executeJsScript(webDriver, getScreenHeight));
				int screenWidth = (int) ((long) executeJsScript(webDriver, getScreenWidth));
				webDriver.manage().window().setSize(new Dimension(screenWidth, screenHeight));
			} 
			catch (Exception e) 
			{
				throw e;
			}
		}
	}


	/**
	 * Open a new tab (window handler should be on the new tab directly).
	 *
	 * @param webDriver the web driver
	 */
	public static void openNewTab(WebDriver webDriver) 
	{
		//webDriver.findElement(By.tagName("body")).sendKeys(Keys.COMMAND + "t");
		webDriver.switchTo().newWindow(WindowType.TAB);
		//String selectLinkOpeninNewTab = Keys.chord(Keys.COMMAND,"t");
		//webDriver.findElement(By.linkText("urlLink")).sendKeys(selectLinkOpeninNewTab);
	}

	/**
	 * Finds and sends a String to an element with given name and xpath value.
	 *
	 * @param webDriver the webdriver.
	 * @param elementDescription given name of the element, for logging purpose
	 * @param selector given selector to locate element
	 * @param index the index of the element in the list of elements
	 * @param message String to send
	 * @throws Exception if an Exception occurs during method execution or fail.
	 */
	public static void sendKeysToElement(WebDriver webDriver, String elementDescription, By selector,int index, String message) throws Exception 
	{
		WebElement element = getElement(webDriver, elementDescription, selector, index);
		try 
		{
			Util.waitAround(Timeouts.ONE_SECOND_INTERVAL / 2);
			try 
			{
				if (!element.getAttribute("value").isEmpty()) 
				{
					element.clear();
				}
				if (!element.getText().isEmpty()) 
				{
					element.clear();
				}
			} 
			catch (Exception e) {
				// Ignore, just a check to see if the element has any existing value/ text.
			}
			element.sendKeys(message);
		} 
		catch (Exception e) 
		{
			throw e;
		}
	}

	/**
	 * Finds and sends a String to an element with given name and xpath value.
	 *
	 * @param webDriver the webdriver.
	 * @param elementDescription given name of the element, for logging purpose
	 * @param selector given selector to locate element
	 * @param message String to send
	 * @throws Exception if an Exception occurs during method execution or fail.
	 */
	public static void sendKeysToElement(WebDriver webDriver, String elementDescription, By selector,String message) throws Exception 
	{
		sendKeysToElement(webDriver, elementDescription, selector, -1, message);
	}

	/**
	 * Sets implicit wait for the webdriver in milliseconds.
	 *
	 * @param webDriver the web driver
	 * @param waitInMilliSeconds the wait in milli seconds
	 */
	public static void setImplicitWait(WebDriver webDriver, int waitInMilliSeconds) 
	{
		webDriver.manage().timeouts().implicitlyWait(waitInMilliSeconds, TimeUnit.MILLISECONDS);
	}

	/**
	 * Switches to a new tab on a webdriver.
	 *
	 * @param webDriver the webdriver
	 * @throws Exception the kite test exception
	 */
	public static void switchWindowHandler(WebDriver webDriver) throws Exception 
	{
		String currentWindowHandler = webDriver.getWindowHandle();

		boolean changed = false;
		String windowHandler = getOtherWindowHandler(webDriver);
		if (windowHandler != currentWindowHandler) 
		{
			webDriver.switchTo().window(windowHandler);
			changed = true;
		}
		if (!changed) 
		{
			throw new Exception("Could not switch tab or window. Current window handler not found.");
		}
	}

	/**
	 * Switches to tab corressponding to the given windowhandler on a webdriver.
	 *
	 * @param webDriver the webdriver
	 * @param windowHandlerId the WindowhanderId of the tab
	 * @throws Exception the kite test exception
	 */
	public static void switchWindowHandler(WebDriver webDriver, String windowHandlerId) throws Exception
	{
		if (windowHandlerId != null)
		{
			webDriver.switchTo().window(windowHandlerId);
		}
	}

	/**
	 * Waits for an element to be visible.
	 *
	 * @param webDriver the webdriver.
	 * @param selector to locate the element
	 * @throws Exception if an Exception occurs during method execution or fail.
	 */
	public static void waitForElement(WebDriver webDriver, By selector) throws Exception 
	{
		waitForElement(webDriver, selector, Timeouts.SHORT_TIMEOUT);
	}

	/**
	 * Waits for an element to be visible
	 *
	 * @param webDriver the webdriver.
	 * @param selector to locate the element
	 * @param timeout defaultWait duration.
	 * @throws Exception if an Exception occurs during method execution or fail.
	 */
	public static void waitForElement(WebDriver webDriver, By selector, int timeout) throws Exception 
	{
		setImplicitWait(webDriver, Timeouts.ONE_SECOND_INTERVAL);
		for (int waitTime = 0; waitTime < timeout; waitTime += Timeouts.ONE_SECOND_INTERVAL) 
		{
			try 
			{
				if (webDriver.findElement(selector) != null) 
				{
					setImplicitWait(webDriver, Timeouts.SHORT_TIMEOUT);
					return;
				}
			} 
			catch (Exception e) 
			{
				// in case the wait throw an abnormal exception, meaning the defaultWait does not defaultWait as it should.
				// logger.debug("Exception in waitForElement " + getStackTrace(e));
				// ignore
				Util.waitAround(Timeouts.ONE_SECOND_INTERVAL);
			}
		}
		throw new Exception("Timeout waiting for element: " + selector.toString());
	}

	/**
	 * Wait for existences of a number of certain elements on page
	 *
	 * @param webDriver the webdriver.
	 * @param selector to locate the element
	 * @param expectedNumber expected number of element visible on page
	 * @throws Exception if an Exception occurs during method execution or fail.
	 */
	public static void waitForExpectedNumberOfElements(WebDriver webDriver, By selector,int expectedNumber) throws Exception 
	{
		for (int waitTime = 0; waitTime < Timeouts.DEFAULT_TIMEOUT; waitTime += Timeouts.ONE_SECOND_INTERVAL) 
		{
			try 
			{
				Util.waitAround(Timeouts.ONE_SECOND_INTERVAL / 2);
				if (hasExpectedNumberOfElements(webDriver, selector, expectedNumber)) 
				{
					Util.waitAround(Timeouts.ONE_SECOND_INTERVAL);
					return;
				}
			} 
			catch (Exception e) 
			{
				// in case the defaultWait throw an abnormal exception, meaning the defaultWait does not defaultWait as it should.
			}
			Util.waitAround(Timeouts.ONE_SECOND_INTERVAL);
		}
		//TODO throw custom exception
	}

	/**
	 * Executes a JS script string with a given webdriver
	 *
	 * @param webDriver the webdriver
	 * @param scriptString the JS script to execute
	 * @return the result of the script execution
	 */
	public static Object executeJsScript(WebDriver webDriver, String scriptString) throws Exception
	{
		try 
		{
			return ((JavascriptExecutor) webDriver).executeScript(scriptString);
		} 
		catch (Exception e) 
		{
			throw e;
		}
	}
	
	/**
	 * Gets the my public ip.
	 *
	 * @return the my public ip
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static String getPublicIp(WebDriver webDriver) throws Exception 
	{
		String publicIpURL = "http://bot.whatismyipaddress.com"; //No I18N
		webDriver.get(publicIpURL);
		String pageSource = webDriver.getPageSource();
		try 
		{
			return pageSource.split("<body>")[1].split("</body>")[0];
		} 
		catch (Exception e) 
		{
			throw e;
		}
	}


}