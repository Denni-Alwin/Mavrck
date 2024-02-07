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
			logger.info("[MAVERICK] Server Started Successfully");
			BotRoom botRoom = new BotRoom();
			botRoom.handleBotSession();
		}
		catch(Exception exp)
                {
                        logger.log(Level.SEVERE,"[SERVERMAIN] Exception while Starting server",exp);
                }

		//TEST


	}
}
