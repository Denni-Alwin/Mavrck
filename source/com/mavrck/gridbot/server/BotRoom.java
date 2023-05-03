package com.mavrck.gridbot.server;

import com.google.common.collect.ConcurrentHashMultiset;
import com.mavrck.gridbot.server.Constants.TradePhase;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Hashtable;
import java.util.concurrent.ConcurrentHashMap;

public class BotRoom 
{
    private static Logger logger = Logger.getLogger(BotRoom.class.getName());

    private TradePhase tradePhase;

    private static Hashtable finalExecutionOrder;

    private static ConcurrentHashMap<String,BotSession> idVsActiveSession = new ConcurrentHashMap<String,BotSession>();
    public void handleBotSession() throws Exception
    {
        BotSession start = new BotSession();
        updateTradePhase(Constants.TradePhase.INITIATED);
		start.startBotSession();
		start.checkTicker();
		Thread.sleep(2000);
        logger.info("BUYING");
        start.buy();
        Thread.sleep(2000);
        logger.info("SELLING");
        start.sell();
        addSession(start);
        System.out.println(idVsActiveSession);
        Thread.sleep(2000);
        finalExecutionOrder = CalcHandler.getFinalExecutionOrder();
        Thread.sleep(10000);
        while(true)
        {
            Thread.sleep(5000);
            int currentprice = start.getCurrentPrice();
            if(currentprice != CalcHandler.getCurrentPrice())
            {
                int higherValue = CalcHandler.getCurrentPrice() + CalcHandler.getLevels();
                System.out.println("HIGHERVALUE --> " + higherValue);
                if(higherValue <= currentprice)
                {
                    logger.info("CROSSED LEVEL");
                    finalExecutionOrder = CalcHandler.getFinalExecutionOrder();
                }
                int lowerValue = CalcHandler.getCurrentPrice() - CalcHandler.getLevels();
                System.out.println("CURRENTPRICE --> " + currentprice);
                System.out.println("LOWERVALUE --> " + lowerValue);
                if(lowerValue >= currentprice)
                {
                    logger.info("Waiting for next buy");
                    start.sell();
                    finalExecutionOrder = CalcHandler.getFinalExecutionOrder();
                    //wait for next buy
                    //next buy for the next green
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
        }

        // TODO
        // -> check or get quantity
        // -> map P and L if possible
        // -> reports
        // -> wallet to check acc balc
        
    }

    public void updateTradePhase(TradePhase tradePhase)
    {
        this.tradePhase = tradePhase;
        if(tradePhase == Constants.TradePhase.INITIATED)
        {
            
        }
        if(tradePhase == Constants.TradePhase.FAILED)
        {
            //Handle failed session
        }
    }

    public void addSession(BotSession session)
    {
        this.idVsActiveSession.put(ConfManager.getTicker(),session);
    }

    public static ConcurrentHashMap<String, BotSession> getIdVsActiveSession()
    {
        return idVsActiveSession;
    }





}
