package com.tartarus.catchacriminal.util;

import java.math.BigInteger;
import java.util.Random;


/**
 *
 * @author New_Tartarus
 */
public class IDGenerator
{
   public static String createPlayerId(String userName, String timeStamp)
    {
        String timeBytes = new BigInteger(timeStamp.getBytes()).toString(2);
        String userBytes = new BigInteger(userName.getBytes()).toString(2);
        
        String xorResult = xor(timeBytes, userBytes);
        
        String[] arr = xorResult.split("(?<=\\G.{32})");
        String xorCompressed = arr[0];
        for(int i = 1; i < arr.length; i++)
        {
            xorCompressed = xor(xorCompressed, arr[i]);
        }
        
        String xorInt = Integer.parseInt(xorCompressed, 2) + "";
        
        String finalStringId = "";
        for(int i = 0; i < xorInt.length(); i++)
        {
            if(i < userName.length())
            {
                finalStringId += Character.toString(userName.charAt(i) + 3);
            }
            else
            {
                String alphabet = "abcdefghijklmnopqrstuvwxyz123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
                Random r = new Random();
                finalStringId += alphabet.charAt(r.nextInt(alphabet.length()));
            }
            finalStringId += xorInt.charAt(i);
        }
        
        String reordered = reorderString(finalStringId);
        arr = reordered.split("(?<=\\G.{6})");
        return String.join("-", arr);
    }
    
    protected static String xor(String a, String b)
    {
        int dif = a.length() - b.length();
        
        if(dif < 0)
        {
            a = replenishString(a, b.length());
        }
        else if(dif > 0)
        {
            b = replenishString(b, a.length());
        }
        
        String result = "";
        for(int i = 0; i < a.length(); i++)
        {
            if(a.charAt(i) == b.charAt(i))
            {
                result += "0";
            }
            else
            {
                result += "1";
            }
        }
        
        return result;
    }
    
    protected static String replenishString(String input, int newSize)
    {
        String result;
        
        String binaryLength = new BigInteger((input.length()+"").getBytes()).toString(2);
        if(input.length() + binaryLength.length() <= newSize)
        {
            int repetitions = newSize - input.length() - binaryLength.length();
            result = input + repeatedChar('0', repetitions) + binaryLength;
        }
        else
        {
            int repetitions = newSize - input.length();
            result = input + repeatedChar('0', repetitions);
        }
        
        return result;
    }
    
    protected static String repeatedChar(char c, int amount)
    {
        String output = "";
        for(int i =0; i < amount; i++)
        {
            output += c;
        }
        
        return output;
    }
    
    protected static String reorderString(String input)
    {
        Character[] returnChars = new Character[input.length()];
        Random r = new Random();
        
        char[] singleChars = input.toCharArray();
        for(char c : singleChars)
        {
            while(true)
            {
                Integer[] arrayIndexes = CACUtils.getAvailableIndexesFromArray(returnChars);
                if(arrayIndexes.length <= 1)
                {
                    returnChars[arrayIndexes[0]] = c;
                    break;
                }
                else
                {
                    int randInt = r.nextInt(arrayIndexes.length);
                    if(returnChars[arrayIndexes[randInt]] == null)
                    {
                        returnChars[arrayIndexes[randInt]] = c;
                        break;
                    }  
                }
            }
        }
        String finalKey = CACUtils.charArrayToString(returnChars);
        
        if(checkKey(input, finalKey))
        {
            return finalKey;
        }
        
        return reorderString(input);
    }
    
    protected static boolean checkKey(String defaultKey, String reorderedKey)
    {
        boolean retval = true;
        if(defaultKey.length() == reorderedKey.length())
        {
            for(int i = 0; i < defaultKey.length(); i++)
            {
                if(defaultKey.charAt(i) == reorderedKey.charAt(i))
                {
                    retval = false;
                    break;
                }
            }
        }
        
        return retval;
    }
}
