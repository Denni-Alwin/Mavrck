package com.mavrck.gridbot.server;

import com.google.common.collect.ConcurrentHashMultiset;
import com.mavrck.gridbot.server.Constants.TradePhase;
import com.mavrck.gridbot.server.Constants.TradeStatus;
import com.mavrck.gridbot.server.util.Colour;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Hashtable;
import java.util.concurrent.ConcurrentHashMap;

public class BotRoom 
{
	private static Logger logger = Logger.getLogger(BotRoom.class.getName());

	private static TradeStatus tradeStatus;

	private static Hashtable finalExecutionOrder;

	private static Hashtable timestampVsBuyPrice = new Hashtable<>();
	private static Hashtable timestampVsSellPrice = new Hashtable<>();
	private static float currentprice = 0f;

	private static ConcurrentHashMap<String,BotSession> idVsActiveSession = new ConcurrentHashMap<String,BotSession>();
	public void handleBotSession() throws Exception
	{
		BotSession start = new BotSession();
		start.startBotSession();
		start.checkTicker();
		currentprice = start.getCurrentPrice();
		start.buy();
		addSession(start);
		finalExecutionOrder = CalcHandler.getFinalExecutionOrder();
		while(true)
		{
			Thread.sleep(5000);
			currentprice = start.getCurrentPrice();
			float higherValue = CalcHandler.getCurrentPrice() + CalcHandler.getLevels();
			float lowerValue = CalcHandler.getCurrentPrice() - CalcHandler.getLevels();

				System.out.println(Colour.Background.ANSI_CYAN_BACKGROUND + " HIGHERPRICE -->  " + higherValue + Colour.ANSI_RESET);
				System.out.println(Colour.Background.ANSI_CYAN_BACKGROUND + " CURRENTPRICE --> " + currentprice + Colour.ANSI_RESET);
				System.out.println(Colour.Background.ANSI_CYAN_BACKGROUND + " LOWERVALUE -->   " + lowerValue + Colour.ANSI_RESET);

			if(currentprice != CalcHandler.getCurrentPrice())
			{

				if(higherValue <= currentprice)
				{
					logger.info("CROSSED LEVEL");
					finalExecutionOrder = CalcHandler.getFinalExecutionOrder();
					if(this.tradeStatus == Constants.TradeStatus.SELL)
					{
						start.buy();
					}
					if(this.tradeStatus == Constants.TradeStatus.BUY)
					{
						logger.info("GOING UP");
					}
				}
				if(lowerValue >= currentprice)
				{
					logger.info("Waiting for next buy");
					if(this.tradeStatus == Constants.TradeStatus.BUY)
					{ 
						start.sell();
					}
					finalExecutionOrder = CalcHandler.getFinalExecutionOrder();
				}
				else
				{
					logger.info("STAYING"); 
					logger.info("INSIDE LOOP");   
				}
			}
			else
			{
				logger.info("STAYING");   
			}
			System.out.println(Colour.Background.ANSI_RED_BACKGROUND + " BUY ORDERS -->  " + timestampVsBuyPrice + Colour.ANSI_RESET);
			System.out.println("  ");
			System.out.println(Colour.Background.ANSI_RED_BACKGROUND + " SELL ORDERS -->  " + timestampVsSellPrice + Colour.ANSI_RESET);
		}

		// TODO
		// -> check or get quantity
		// -> map P and L if possible
		// -> reports
		// -> wallet to check acc balc

	}

	public static void updateTradeStatus(TradeStatus tradestatus)
	{
		tradeStatus = tradestatus;
	}

	public void addSession(BotSession session)
	{
		this.idVsActiveSession.put(ConfManager.getTicker(),session);
	}

	public static ConcurrentHashMap<String, BotSession> getIdVsActiveSession()
	{
		return idVsActiveSession;
	}

	public static float getCurrentPrice()
	{
		return currentprice;
	}

	public static void updateBuyPriceAction()
	{
		timestampVsBuyPrice.put(System.currentTimeMillis(), getCurrentPrice());
	}

	public static void updateSellPriceAction()
	{
		timestampVsSellPrice.put(System.currentTimeMillis(), getCurrentPrice());
	}

}
