package com.mavrck.gridbot.server;

public class Constants
{
	public enum BotStatus
        {
                FREE,
                ACTIVE;
        }

	public enum TradePhase
        {
                INITIATED,
                BUYING,
		SELLING,
		FAILED,
		STAYING,
                COMPLETED;
        }

	public enum TradeStatus
	{
		BUY,
		SELL,
		STAY;
	}

	public class ConfigurationKeys
        {
		public static final String TICKER = "ticker"; 
		public static final String GREEN = "green"; 
		public static final String RED = "red";

			
	}

	public class Symbols
	{
		public static final String BTCUSDT = "BTCUSDT"; 
	}
}

