package com.tartarus.catchacriminal.transport.interfaces;

/**
 *
 * @author New_Tartarus
 */
public interface ICommand
{
   public String getName();
   public void setName(String name);
   public String[] getParameters();
   public void setParameters(String[] parameters);
   
   public boolean execute();
}
