package com.tartarus.catchacriminal.core.commands;

import com.tartarus.catchacriminal.core.structure.Lobby;
import com.tartarus.catchacriminal.core.structure.Player;
import com.tartarus.catchacriminal.core.structure.Room;
import com.tartarus.catchacriminal.transport.enums.ECommands;
import com.tartarus.catchacriminal.transport.enums.EParameters;
import com.tartarus.catchacriminal.transport.interfaces.ICommand;
import com.tartarus.catchacriminal.util.IDGenerator;
import java.nio.channels.SocketChannel;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author New_Tartarus
 */
public class ClientCommand implements ICommand
{
   protected String name;
   protected String[] parameters;
   protected Map<String,Object> additionalParams;
   
   @Override
   public boolean execute()
   {
      switch(this.getName())
      {
         case ECommands.TRY_CONNECT:
            return execute_TryConnect();
         case ECommands.CONNECT:
            return execute_Connect();
         case ECommands.CHANGE_ROOM:
            return execute_ChangeRoom();
         case ECommands.CREATE_GAME:
            return execute_CreateGame();
         case ECommands.JOIN_GAME:
            return execute_JoinGame();
         case ECommands.LOAD_GAME:
            return execute_LoadGame();
         case ECommands.CHANGE_GAME_SETTINGS:
            return execute_ChangeGameSettings();
         case ECommands.CHANGE_PLAYER_SETTINGS:
            return execute_ChangePlayerSettings();
         case ECommands.MESSAGE:
            return execute_Message();
         default:
            return false;
      }
   }
   
   protected boolean execute_TryConnect()
   {
      return false;
   }
   
   protected boolean execute_Connect()
   {
      try
      {
         Lobby lobby = ((Lobby)additionalParams.get(EParameters.LOBBY));
         Room room = lobby.getDefaultRoom();
         String id = createID(lobby,parameters[0]);
         SocketChannel sc = (SocketChannel)additionalParams.get(EParameters.SOCKETCHANNEL);
         
         lobby.addPlayerId(id);
         Player p = new Player(parameters[0],id,sc);
         
         p.setCurrentRoom(room);
         room.addPlayer(p);
         p.sendMessage("Player registerd with id: " + id);
         
         return true;
      }
      catch (Exception ex)
      {
         Logger.getLogger(ClientCommand.class.getName()).log(Level.SEVERE, "Connection to the server failed", ex);
         return false;
      }
   }
   
   protected String createID(Lobby lobby, String playerName)
   {
      String timeStamp = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(Calendar.getInstance().getTime());
      String playerID = IDGenerator.createPlayerId(playerName, timeStamp);
      
      if(lobby.getUsedPlayerIds().contains(playerID))
      {
         playerID = createID(lobby, playerName);
      }
      
      return playerID;
   }
   
   protected boolean execute_ChangeRoom()
   {
      return false;
   }
   
   protected boolean execute_CreateGame()
   {
      return false;
   }
   
   protected boolean execute_JoinGame()
   {
      return false;
   }
   
   protected boolean execute_LoadGame()
   {
      return false;
   }
   
   protected boolean execute_ChangeGameSettings()
   {
      return false;
   }
   
   protected boolean execute_ChangePlayerSettings()
   {
      return false;
   }
   
   protected boolean execute_Message()
   {
      try
      {
         Lobby lobby = ((Lobby)additionalParams.get(EParameters.LOBBY));
         String playerId = parameters[0];
         String msg = parameters[1];
         
         Player player = lobby.getPlayerById(playerId);
         if(player == null || player.getCurrentRoom() == null)
         {
            return false;
         }
         
         msg = String.format("%23s: %s", player.getPlayerName(), msg);
         
         for(Player p : player.getCurrentRoom().getPlayers())
         {
            p.sendMessage(msg);
         }
         
         return true;
      }
      catch (Exception ex)
      {
         Logger.getLogger(ClientCommand.class.getName()).log(Level.SEVERE, "Message could not be sent.", ex);
         return false;
      }
   }

   @Override
   public String getName()
   {
     return this.name;
   }

   @Override
   public void setName(String name)
   {
      this.name = name;
   }

   @Override
   public String[] getParameters()
   {
      return this.parameters;
   }

   @Override
   public void setParameters(String[] parameters)
   {
      this.parameters = parameters;
   }
   
   public void addAdditionalParams(String key, Object value)
   {
      if(additionalParams == null)
      {
         additionalParams = new HashMap<>();
      }
      
      additionalParams.put(key, value);
   }
}
