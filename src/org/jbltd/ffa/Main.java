package org.jbltd.ffa;

import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jbltd.ffa.killstreaks.GapplerKillstreak;
import org.jbltd.ffa.killstreaks.Killstreak;
import org.jbltd.ffa.killstreaks.SupplyDrop;
import org.jbltd.ffa.listeners.Basic;
import org.jbltd.ffa.listeners.CombatListener;
import org.jbltd.ffa.listeners.InventoryListener;
import org.jbltd.ffa.listeners.ResourcepackListener;
import org.jbltd.ffa.managers.CombatManager;
import org.jbltd.ffa.managers.NotificationManager;
import org.jbltd.ffa.notifications.Assist;
import org.jbltd.ffa.notifications.KingSlayer;
import org.jbltd.ffa.notifications.Payback;
import org.jbltd.ffa.notifications.Survivor;
import org.jbltd.ffa.notifications.Trickshotter;
import org.jbltd.ffa.perks.BruteStrength;
import org.jbltd.ffa.perks.Ghost;
import org.jbltd.ffa.perks.Kevlar;
import org.jbltd.ffa.perks.Lightweight;
import org.jbltd.ffa.perks.Perk;
import org.jbltd.ffa.util.DatabaseManager;
import org.jbltd.ffa.util.F;
import org.jbltd.ffa.util.InventoryOpenCommand;
import org.jbltd.ffa.util.UpdateTask;

import net.minecraft.server.v1_8_R3.MinecraftServer;

public class Main extends JavaPlugin
{

	public void onEnable()
	{

		try
		{
			new DatabaseManager(this).setupDB();
		} catch (ClassNotFoundException | SQLException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
 
		Ghost ghost = new Ghost(this);
		Lightweight lightweight = new Lightweight(this);
		CombatManager manager = new CombatManager(this);
		NotificationManager nmanager = new NotificationManager();
		CombatListener listener = new CombatListener(manager);

		new Assist(this, manager, nmanager);
		new KingSlayer(this, manager, nmanager);
		new Payback(this, manager, nmanager);
		new Survivor(this, manager, nmanager);
		new Trickshotter(this, manager, nmanager);

		Perk.allPerks.add(new BruteStrength(this));
		Perk.allPerks.add(new Kevlar(this));
		Perk.allPerks.add(ghost);
		Perk.allPerks.add(lightweight);

		Killstreak.allStreaks.add(new GapplerKillstreak(this, manager));
		Killstreak.allStreaks.add(new SupplyDrop(this, manager));

		getServer().getPluginManager().registerEvents(new Basic(), this);
		getServer().getPluginManager().registerEvents(new InventoryListener(), this);
		getServer().getPluginManager().registerEvents(listener, this);
		getServer().getPluginManager().registerEvents(manager, this);
		getServer().getPluginManager().registerEvents(new ResourcepackListener(), this);
		getServer().getPluginManager().registerEvents(nmanager, this);

		getCommand("perk").setExecutor(new InventoryOpenCommand());
		
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, ghost, 0L, 20L);
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, lightweight, 0L, 20L);

		new UpdateTask(this);

		System.out.println(F.info("Game", true, "Game ready."));

		MinecraftServer.getServer().getPropertyManager().setProperty("debug", true);
	}


}
