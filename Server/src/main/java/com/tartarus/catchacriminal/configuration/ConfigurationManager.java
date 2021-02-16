package com.tartarus.catchacriminal.configuration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.tartarus.catchacriminal.util.Json;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author New_Tartarus
 */
public class ConfigurationManager 
{
    protected static ConfigurationManager instance;
    protected static Configuration config;
    
    public static ConfigurationManager getInstance()
    {
        if(instance == null)
        {
            instance = new ConfigurationManager();
        }
        
        return instance;
    }
    
    
    public void loadConfigurationFile(String path)
    {
        FileReader fr;
        try 
        {
            fr = new FileReader(path);
        } 
        catch (FileNotFoundException ex) 
        {
            throw new ConfigurationException(ex);
        }
        StringBuffer sb = new StringBuffer();
        
        int i;
        try 
        {
            while((i = fr.read()) != -1)
            {
                sb.append((char)i);
            }
        } 
        catch (IOException ex) 
        {
            throw new ConfigurationException(ex);
        }
        
        JsonNode conf;
        try 
        {
            conf = Json.parse(sb.toString());
        } 
        catch (IOException ex)
        {
            throw new ConfigurationException("Error parsing the configuration file.", ex);
        }
        try 
        {
            config = Json.fromJson(conf, Configuration.class);
        } 
        catch (JsonProcessingException ex)
        {
            throw new ConfigurationException("Error parsing the configuration file, internal.", ex);
        }
    }
    
    public Configuration getCurrentConfiguration()
    {
        if(this.config == null)
        {
            throw new ConfigurationException("No current configuration set.");
        }
        
        return this.config;
    }
}
