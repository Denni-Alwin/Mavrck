package com.mavrck.gridbot.server;

public class Constants
{
	public enum botStatus
        {
                FREE,
                ACTIVE;
        }

	public enum tradePhase
        {
                INITIATED,
                BUYING,
		SELLING,
		FAILED,
		STAYING,
                COMPLETED;
        }

	public enum tradeStatus
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

