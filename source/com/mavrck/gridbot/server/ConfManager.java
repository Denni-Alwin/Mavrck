package com.mavrck.gridbot.server;

import java.io.File;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.FileInputStream;
import java.io.FileOutputStream;


public class ConfManager
{
	private static Logger logger = Logger.getLogger(ConfManager.class.getName());

	private static String serverHome = null;
	
	static
        {
                serverHome = System.getProperty("server.home");
        }
	
	private static String serverConfFile = null;
	private static Properties serverConf;

	private static String driverConfFile = null;
	private static Properties driverConf;

	private static String credentialConfFile = null;
	private static Properties credentialConf;

	private static String ticker = "BTC";
	
	private static String currency = "INR";

 	private static String reportsBasePath = System.getProperty("user.home") + File.separator + "reports";
	
	private static String extension = "";

	private static String driverPath = "";
	private static String chromePath = "";
	private static String baseUrl = "";

	private static String username = "";
	private static String password = "";


	public static boolean initialize() throws Exception
	{
		serverConfFile = serverHome+File.separator+"conf"+File.separator+"conf.properties"+extension;
		driverConfFile = serverHome+File.separator+"conf"+File.separator+"server.properties"+extension;
		credentialConfFile = serverHome+File.separator+"conf"+File.separator+"credential.properties"+extension;


		serverConf = getProperties(serverConfFile);
		driverConf = getProperties(driverConfFile);
		credentialConf = getProperties(credentialConfFile);

		//serverConf
		reportsBasePath = serverConf.getProperty("reportsbasepath", reportsBasePath);
		ticker = serverConf.getProperty("ticker", ticker);
		baseUrl = serverConf.getProperty("baseurl", baseUrl);

		//driverConf
		driverPath = driverConf.getProperty("driverpath", driverPath);
		chromePath = driverConf.getProperty("chromepath", chromePath);

		//credentialConf
		username = credentialConf.getProperty("username", username);
		password = credentialConf.getProperty("password", password);
		
		logger.log(Level.INFO, "MAVERICK Server ConfManager initialized Successfully.");//No I18n
        return true;
	}
	
	public static Properties getProperties(String propsFile)
	{
		try
		{
			logger.info("Loading props "+propsFile);//No I18N
			Properties props = new Properties();
			props.load(new FileInputStream(propsFile));
			return props;
		}
		catch(Exception exp)
		{
			logger.log(Level.SEVERE, "Unable to load conf file : "+propsFile, exp);//No I8n
			return null;
		}
	}


	public static void setServerHome(String srvhome)
	{
		serverHome = srvhome;
	}

	public static void setTicker(String symbol)
	{
		ticker = symbol;
	}

	public static String getTicker()
	{
		return ticker;
	}

	public static String getBaseUrl()
	{
		return baseUrl;
	}

	public static String getUserName()
	{
		return username;
	}

	public static String getPassword()
	{
		return password;
	}

	public static String getChromeDriverPath()
	{
		return driverPath;
	}

	public static String getChromePath()
	{
		return chromePath;
	}

	public static String getCurrency()
	{
		return currency;
	}
}



