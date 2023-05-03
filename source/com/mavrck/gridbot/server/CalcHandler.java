package com.mavrck.gridbot.server;

import java.util.Arrays; 
import java.util.Hashtable; 
import java.util.concurrent.ThreadLocalRandom;
//Get total hashtable 
//price levels


public class CalcHandler 
{

    public static Hashtable finalExecutionOrder = new Hashtable<>();
    //public static int levels = 25000;
    public static int levels = 10000;

    public static int[] priceLevels = new int[12];
    
    public static String[] ExecutionSignals = new String[12];

    public static int currentPrice;

    public static int index;

    public static Hashtable getFinalExecutionOrder() throws Exception
    {
        BotSession botSession = BotRoom.getIdVsActiveSession().get(ConfManager.getTicker());
        System.out.println(botSession);
        currentPrice = botSession.getCurrentPrice();
        calculatePriceLevels();
        getExecutionSignals();
        calc();

        return finalExecutionOrder;
    }

    public static int[] calculatePriceLevels() throws Exception
    {   
        priceLevels[6] = currentPrice;
        for(int i=6;i>0;i--)
        {
            priceLevels[i-1] = priceLevels[i] - levels;
        }
        for(int i=6;i<11;i++)
        {
            priceLevels[i+1] = priceLevels[i] + levels;
        }
        
        System.out.println(Arrays.toString(priceLevels));
        return priceLevels;    
    }

    public static String[] getExecutionSignals()
    {
        for(int i=0;i<ExecutionSignals.length;i++)
        {
            ExecutionSignals[i] = Constants.ConfigurationKeys.RED;
        }
        return ExecutionSignals;
    }

    public static void calc()
    {
        //int currentPrice = ThreadLocalRandom.current().nextInt(2305006, 2580006);
        //int position = currentPrice - (currentPrice % levels);
        //System.out.println(">>>>>>>>" + position + ">>>>>>>>>>>>>>>>.");
        index = Arrays.binarySearch(priceLevels, currentPrice);
        //index = index + 1;
        if(index<12 && index>0)
        { 
                for(int i = 0; i <= index; i++)
                {
                        if(ExecutionSignals[i] != Constants.ConfigurationKeys.RED)
                        {
                                ExecutionSignals[i] = Constants.ConfigurationKeys.RED;
                        }
                }
                for(int i = index+1; i < priceLevels.length; i++)
                {
                        if(ExecutionSignals[i] == Constants.ConfigurationKeys.RED)
                        {
                                ExecutionSignals[i] = Constants.ConfigurationKeys.GREEN;
                        }
                }
        }
        else
        {
                for(int i = 0; i < priceLevels.length; i++)
                    {
                        if(ExecutionSignals[i] == Constants.ConfigurationKeys.RED)
                        {
                                ExecutionSignals[i] = Constants.ConfigurationKeys.GREEN;
                        }
                }
        }       
        System.out.println(Arrays.toString(ExecutionSignals));                                                                                                                                             
        for(int i=0; i< priceLevels.length; i++)
        {
            finalExecutionOrder.put(priceLevels[i],ExecutionSignals[i]);
        }
        System.out.println(finalExecutionOrder);
    }

    public static int getIndex()
    {
        return index;
    }

    public static int getLevels()
    {
        return levels;
    }

    public static int getCurrentPrice()
    {
        return currentPrice;
    }
}
