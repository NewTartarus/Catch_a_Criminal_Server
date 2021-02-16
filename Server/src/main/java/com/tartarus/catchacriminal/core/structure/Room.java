package com.tartarus.catchacriminal.core.structure;

import com.tartarus.catchacriminal.transport.interfaces.IRoom;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 *
 * @author New_Tartarus
 */
public class Room implements IRoom
{
   protected List<Game> games;
   protected List<Player> players;
   
   protected String roomName;
   
   public Room(String name)
   {
      this.roomName = name;
   }
   
   @Override
   public void addPlayer(Player player)
   {
      if(players == null)
      {
         players = new ArrayList<>();
      }
      
      players.add(player);
   }
   
   public void addGame(Game game)
   {
      if(games == null)
      {
         games = new ArrayList<>();
      }
      
      games.add(game);
   }
   
   public List<Game> getGames()
   {
      if(games == null)
      {
         games = new ArrayList<>();
      }
      
      return games;
   }
   
   @Override
   public List<Player> getPlayers()
   {
      if(players == null)
      {
         players = new ArrayList<>();
      }
      
      return players;
   }
   
   @Override
   public Player getPlayerById(String id)
   {
      List<Player> playerList = getPlayers().stream().filter(p -> p.id.equals(id)).collect(Collectors.toList());
      
      if(playerList.isEmpty())
      {
         for(Game game : games)
         {
            Player player = game.getPlayerById(id);
            if(player != null)
            {
               playerList.add(player);
            }
         }
      }
      
      return playerList.isEmpty() ? null: playerList.get(0);
   }
}
