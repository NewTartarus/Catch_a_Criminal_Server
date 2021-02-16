package com.tartarus.catchacriminal.util;

import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author New_Tartarus
 */
public class CACUtils
{
   public static String getString(Object obj)
   {
      if(obj != null)
      {
         return obj.toString();
      }
      
      return "";
   }
   
   public static long getLong(Object obj)
   {
      if(obj != null)
      {
         if(obj instanceof Number)
         {
            return ((Number) obj).longValue();
         }
         else if(obj instanceof String)
         {
            return Long.getLong(obj.toString());
         }
      }
      
      return 0;
   }
   
   public static String charArrayToString(Character[] charArray)
    {
        String retval = "";
        for(Character c : charArray)
        {
            retval += c;
        }
        
        return retval;
    }
    
    public static <T> Integer[] getAvailableIndexesFromArray(T[] input)
    {
        Integer[] retval;
        
        List<Integer> indexes = new ArrayList<>();
        for(int i = 0; i < input.length; i++)
        {
            if(input[i] == null)
            {
                indexes.add(i);
            }
        }
        
        retval = new Integer[indexes.size()];
        indexes.toArray(retval);
        
        return retval;
    }
}
