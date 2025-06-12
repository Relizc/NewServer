package net.itsrelizc.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.RegisteredListener;

import com.mojang.datafixers.types.Type;

import net.itsrelizc.commands.RelizcCommand;
import net.itsrelizc.players.locales.Locale;
import net.itsrelizc.string.StringUtils;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredListener;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

public class CommandCheckEvents extends RelizcCommand {

	public CommandCheckEvents() {
		super("events", "checks all the events");
		this.setRelizcOp(true);
		
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public boolean onPlayerExecute(Player sender, String[] args) {
		
		Plugin pp = EventRegistery.main;
		
		System.out.println(pp.getName());
        ArrayList<RegisteredListener> rls = HandlerList.getRegisteredListeners(pp);
        for (RegisteredListener rl : rls) {
            String cl = rl.getListener().getClass().getName();
            Method[] methods = rl.getListener().getClass().getDeclaredMethods();
            for (Method m : methods) {
                String n = m.getName();
                Class<?>[] params = m.getParameterTypes();
                
                List<String> paramsList = StringUtils.fromNewList();
                
                for (Class<?> pa : params) {
                	
                	Bukkit.broadcastMessage(" " + (pa.isInstance(new Event() {

						@Override
						public HandlerList getHandlers() {
							// TODO Auto-generated method stub
							return null;
						}})));
                	
                    String par = pa.getName();
                    paramsList.add(par);
                    
                    
                }
                
                 
            }

        }
		
		return true;
		
	}


}
