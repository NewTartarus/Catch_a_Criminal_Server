package com.tartarus.catchacriminal.core.structure;

import com.tartarus.catchacriminal.transport.interfaces.IRoom;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 *
 * @author New_Tartarus
 */
public class Game implements IRoom
{
   protected List<Player> players;
   
   protected String gameName;
   protected String password;
   protected GameSettings settings;
   
   public Game(String name, String password, GameSettings settings)
   {
      this.gameName = name;
      this.password = password;
      this.settings = settings;
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
   
   public void changeSettings(GameSettings settings)
   {
      this.settings = settings;
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
      
      return playerList.isEmpty() ? null : playerList.get(0);
   }
}
