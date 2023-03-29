package com.mavrck.gridbot.server.util;

import java.io.File;
import java.util.Hashtable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Properties;
import java.util.regex.Pattern;
import java.util.logging.Logger;
import java.util.logging.Level;
import org.json.JSONObject;

public class Util
{
	public static Logger logger = Logger.getLogger(Util.class.getName());

	public static void waitAround(int durationInMillisecond) 
	{
		try 
		{
			Thread.sleep(durationInMillisecond);
		} 
		catch (InterruptedException e) 
		{
			Thread.currentThread().interrupt();
		}
	}

	public static boolean isNull(String st)
	{
		return (st == null || st == "" || st.equals("null"));
	}
}										