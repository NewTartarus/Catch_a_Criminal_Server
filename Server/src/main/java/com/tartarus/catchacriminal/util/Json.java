package com.tartarus.catchacriminal.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.IOException;


/**
 *
 * @author New_Tartarus
 */
public class Json 
{
    protected static ObjectMapper objectMapper = defaulObjectMapper();
    protected static ObjectMapper defaulObjectMapper()
    {
       ObjectMapper om = new ObjectMapper();
       om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
       return om;
    }
    
    public static JsonNode parse(String jsonSrc) throws IOException
    {
        return objectMapper.readTree(jsonSrc);
    }
    
    public static <T> T fromJson(JsonNode node, Class<T> clazz) throws JsonProcessingException
    {
        return objectMapper.treeToValue(node, clazz);
    }
    
    public static JsonNode toJson(Object obj)
    {
        return objectMapper.valueToTree(obj);
    }
    
    public static String objectToJsonString(Object obj) throws JsonProcessingException
    {
        ObjectWriter objWriter = objectMapper.writer().with(SerializationFeature.INDENT_OUTPUT);
        return objWriter.writeValueAsString(obj);
    }
}
