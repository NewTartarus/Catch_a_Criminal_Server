package com.tartarus.catchacriminal.core.structure;

import com.tartarus.catchacriminal.transport.enums.EMaps;


/**
 *
 * @author New_Tartarus
 */
public class GameSettings
{
   protected int map;
   protected int maxPlayerCount;
   
   public GameSettings(Number map, int maxPlayerCount)
   {
      this.map = map == null ? EMaps.LONDON : map.intValue();
      this.maxPlayerCount = maxPlayerCount;
   }
}
