package com.tartarus.catchacriminal.transport.interfaces;

import com.tartarus.catchacriminal.core.structure.Player;
import java.util.List;


/**
 *
 * @author New_Tartarus
 */
public interface IRoom
{

   public void addPlayer(Player player);

   public List<Player> getPlayers();

   public Player getPlayerById(String id);
}
