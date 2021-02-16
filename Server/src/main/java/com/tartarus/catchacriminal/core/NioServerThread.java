package com.tartarus.catchacriminal.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.tartarus.catchacriminal.configuration.Configuration;
import com.tartarus.catchacriminal.configuration.ConfigurationException;
import com.tartarus.catchacriminal.core.commands.ClientCommand;
import com.tartarus.catchacriminal.core.structure.Lobby;
import com.tartarus.catchacriminal.core.structure.Player;
import com.tartarus.catchacriminal.transport.enums.ECommands;
import com.tartarus.catchacriminal.transport.enums.EParameters;
import com.tartarus.catchacriminal.util.Json;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 *
 * @author New_Tartarus
 */
public class NioServerThread extends Thread
{

   final Lobby lobby;

   final protected int port;
   final protected String address;
   final protected ServerSocketChannel ssc;
   final protected Selector selector;

   protected String welcomeMessage = "";
   protected ByteBuffer buf = ByteBuffer.allocate(1024);

   protected final static Logger LOGGER = LoggerFactory.getLogger(NioServerThread.class);

   /**
    *
    * @param address
    * @param port
    * @param msg
    * @param roomNames
    * @throws IOException
    */
   public NioServerThread(final String address,
                          final int port,
                          final String msg,
                          final String[] roomNames)
           throws IOException
   {
      this.address = address;
      this.port = port;

      this.ssc = ServerSocketChannel.open();
      ssc.socket().bind(new InetSocketAddress(address, port));
      ssc.configureBlocking(false);

      this.selector = Selector.open();
      this.ssc.register(selector, SelectionKey.OP_ACCEPT);

      this.welcomeMessage = msg;

      lobby = new Lobby();
      lobby.init(roomNames);
   }

   @Override
   public void run()
   {
      System.out.println("Server starting at " + this.address + ":" + this.port);

      while (true)
      {
         try
         {
            final int readyChannel = this.selector.selectNow();
            if (readyChannel == 0)
            {
               continue;
            }

            Set<SelectionKey> selectedKeys = this.selector.selectedKeys();
            Iterator<SelectionKey> iter = selectedKeys.iterator();

            while (iter.hasNext())
            {
               SelectionKey key = iter.next();

               if (key.isAcceptable())
               {
                  this.acceptConnection(key);
               }
               else if (key.isConnectable())
               {
                  // a connection was established with a remote server.
                  System.out.println("Connection is established.");
               }
               else if (key.isReadable())
               {
                  this.readConnection(key);
               }
               else if (key.isWritable())
               {
                  // a channel is ready for writing
                  System.out.println("Write to client.");
               }

               iter.remove();
            }
         }
         catch (IOException e)
         {
            System.out.println("IOException, server of port " + this.port + " terminating. Stack trace:");
            e.printStackTrace();
         }
      }
   }

   protected void acceptConnection(SelectionKey key)
           throws IOException
   {
      final SocketChannel sc = ((ServerSocketChannel) key.channel()).accept();
      final String address = (new StringBuilder(sc.socket().getInetAddress().toString())).append(":")
              .append(sc.socket().getPort()).toString();
      sc.configureBlocking(false);
      sc.register(selector, SelectionKey.OP_READ, address);

      System.out.println("accepted connection from: " + address);
   }

   protected void readConnection(SelectionKey key)
           throws IOException
   {
      SocketChannel ch = (SocketChannel) key.channel();
      StringBuilder sb = new StringBuilder();

      buf.clear();
      int read = 0;
      
      while ((read = ch.read(buf)) > 0)
      {
         buf.flip();
         byte[] bytes = new byte[buf.limit()];
         buf.get(bytes);
         sb.append(new String(bytes));
         buf.clear();
      }
      String cmd;
      if (read < 0)
      {
         cmd = key.attachment() + " left the chat.\n";
         ch.close();
      }
      else
      {
         cmd = sb.toString();
      }

      ch.register(selector, SelectionKey.OP_WRITE);
      ClientCommand cc = getCommand(cmd);
      boolean result = false;
      
      switch(cc.getName())
      {
         case ECommands.CONNECT:
            cc.addAdditionalParams(EParameters.SOCKETCHANNEL, ch);
         case ECommands.MESSAGE:
            cc.addAdditionalParams(EParameters.LOBBY, lobby);
            result = cc.execute();
            break;
         default:
            break;
      }

      if (!result)
      {
         System.out.println("ERROR: Command '" + cc.getName() + "' could not been executed.");
      }

      ch.register(selector, SelectionKey.OP_READ);
      System.out.println(cmd);
   }

   protected ClientCommand getCommand(String commandline)
   {
      JsonNode cmdJson;

      try
      {
         cmdJson = Json.parse(commandline);
      }
      catch (IOException ex)
      {
         throw new ConfigurationException("Error parsing the command stream.", ex);
      }

      ClientCommand cmd;

      try
      {
         cmd = Json.fromJson(cmdJson, ClientCommand.class);
      }
      catch (JsonProcessingException ex)
      {
         throw new ConfigurationException("Error parsing the command stream, internal.", ex);
      }

      return cmd;
   }
   
   public boolean sendMessage(String msg, SocketChannel channel)
   {
      int response;
      ByteBuffer msgBuf = ByteBuffer.wrap(msg.getBytes());
      try
      {
         response = channel.write(msgBuf);
         msgBuf.rewind();
      }
      catch (IOException ex)
      {
         java.util.logging.Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, ex);
         return false;
      }
      
      return true;
   }
}
