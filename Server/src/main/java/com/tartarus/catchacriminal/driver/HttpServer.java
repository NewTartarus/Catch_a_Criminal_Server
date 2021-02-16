package com.tartarus.catchacriminal.driver;

import com.tartarus.catchacriminal.configuration.Configuration;
import com.tartarus.catchacriminal.configuration.ConfigurationManager;
import com.tartarus.catchacriminal.core.NioServerThread;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author New_Tartarus
 */
public class HttpServer 
{
    protected final static Logger LOGGER = LoggerFactory.getLogger(HttpServer.class);
    
    public static void main(String[] args) 
    {
        LOGGER.info("Server is starting ...");
        ConfigurationManager.getInstance().loadConfigurationFile("src/main/resources/http.json");
        Configuration conf = ConfigurationManager.getInstance().getCurrentConfiguration();
        
        LOGGER.info("Port:    " + conf.getPort());
        LOGGER.info("Webroot: " + conf.getWebroot());
        
        try 
        {
            //final String address, final int port, final String msg
            NioServerThread serverListenerThread = new NioServerThread(conf.getWebroot(), conf.getPort(), conf.getWelcomeMessage(), conf.getRoomNames());
            serverListenerThread.start();
        } 
        catch (IOException ex) 
        {
            LOGGER.error("Error while starting a ServerListenerThread", ex);
        }
        
    }
}
