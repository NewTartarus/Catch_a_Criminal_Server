package com.tartarus.catchacriminal.core.structure;

import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author New_Tartarus
 */
public class Lobby
{
   protected List<Room> rooms;
   protected List<String> usedPlayerIds;
   
   public void init(String[] roomNames)
   {
      for(String name : roomNames)
      {
         addRoom(name);
      }
   }
   
   public void addRoom(Room room)
   {
      if(rooms == null)
      {
         rooms = new ArrayList<>();
      }
      
      rooms.add(room);
   }
   
   public void addRoom(String roomName)
   {
      addRoom(new Room(roomName));
   }
   
   public Room getDefaultRoom()
   {
      if(rooms == null)
      {
         addRoom("Global");
      }
      
      return rooms.get(0);
   }
   
   public List<String> getUsedPlayerIds()
   {
      if(usedPlayerIds == null)
      {
         usedPlayerIds = new ArrayList<>();
      }
      
      return usedPlayerIds;
   }
   
   public void addPlayerId(String playerId)
   {
      if(usedPlayerIds == null)
      {
         usedPlayerIds = new ArrayList<>();
      }
      
      usedPlayerIds.add(playerId);
   }
   
   public List<Room> getRooms()
   {
      if(rooms == null)
      {
         rooms = new ArrayList<>();
      }
      
      return rooms;
   }
   
   public Player getPlayerById(String id) throws Exception
   {
      List<Player> playerList = new ArrayList<>();
      
      for(Room room : getRooms())
      {
         Player player = room.getPlayerById(id);
         
         if(player != null)
         {
            playerList.add(player);
         }
      }
      
      if(playerList.isEmpty())
      {
         throw new Exception("No player with the id: " + id + " was found.");
      }
      
      return playerList.get(0);
   }
}
