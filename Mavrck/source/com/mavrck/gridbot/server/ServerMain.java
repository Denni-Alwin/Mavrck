package com.mavrck.gridbot.server;

import java.util.logging.Level;
import java.util.logging.Logger;



public class ServerMain
{
	private static Logger logger = Logger.getLogger(ServerMain.class.getName());

	public static void main(String args[])
	{
		//Read confmanager
		try
		{
			if(!ConfManager.initialize())
			{
				logger.severe("UNABLE TO INITIALIZE CONFMANAGER");//No I18N
				System.exit(1);
			}
			BotSession start = new BotSession();
			logger.info("Server Started Successfully");
			start.startBotSession();
			start.checkTicker();
			Thread.sleep(2000);
			logger.info("BUYING");
			start.buy();
			Thread.sleep(2000);
			logger.info("SELLING");
			start.sell();
			Thread.sleep(2000);
			start.getCurrentPrice();
			
		}
		catch(Exception exp)
                {
                        logger.log(Level.SEVERE,"[SERVERMAIN] Exception while Starting server",exp);
                }

		//TEST


	}
}
