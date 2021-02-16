package com.tartarus.catchacriminal.configuration;

/**
 *
 * @author New_Tartarus
 */
public class Configuration 
{
    protected int port;
    protected String webroot;
    protected String welcomeMsg;
    protected String[] roomNames;
    
    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getWebroot() {
        return webroot;
    }

    public void setWebroot(String webroot) {
        this.webroot = webroot;
    }
    
    public String getWelcomeMessage() {
        return webroot;
    }

    public void setWelcomeMessage(String msg) {
        this.welcomeMsg = msg;
    }

   public String[] getRoomNames()
   {
      return roomNames;
   }

   public void setRoomNames(String[] roomNames)
   {
      this.roomNames = roomNames;
   }
}
