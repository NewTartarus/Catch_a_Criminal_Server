package com.tartarus.catchacriminal.core.structure;

import com.tartarus.catchacriminal.transport.interfaces.IRoom;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author New_Tartarus
 */
public class Player
{
   protected String name;
   protected String id;
   protected String playerName;
   
   protected IRoom currentRoom;
   
   protected SocketChannel socketChannel;
   
   
   public Player(String name, String id, SocketChannel sc) throws Exception
   {
      if(name == null || id == null || sc == null)
      {
         throw new Exception("Player could not be initialized.");
      }
      
      this.name = name;
      this.id = id;
      this.socketChannel = sc;
   }
   
   public SocketChannel getSocketChannel()
   {
      return socketChannel;
   }
   
   public String getPlayerName()
   {
      if(playerName == null)
      {
         playerName = name + "#" + id.split("-")[0];
      }
      
      return playerName;
   }
   
   public void setCurrentRoom(IRoom room)
   {
      this.currentRoom = room;
   }
   
   public IRoom getCurrentRoom()
   {
      return this.currentRoom;
   }
   
   public boolean sendMessage(String msg)
   {
      int response;
      ByteBuffer msgBuf = ByteBuffer.wrap(msg.getBytes());
      try
      {
         response = socketChannel.write(msgBuf);
         msgBuf.rewind();
      }
      catch (IOException ex)
      {
         Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, ex);
         return false;
      }
      
      return true;
   }
}
