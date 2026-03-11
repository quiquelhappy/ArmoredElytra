package nl.pim16aap2.armoredElytra.handlers;

import nl.pim16aap2.armoredElytra.ArmoredElytra;
import nl.pim16aap2.armoredElytra.nbtEditor.NBTEditor;
import nl.pim16aap2.armoredElytra.util.ArmorTier;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ListIterator;

public class Uninstaller implements Listener
{
    private static final ItemStack AIR = new ItemStack(org.bukkit.Material.AIR, 1);

    private final ArmoredElytra plugin;
    private final NBTEditor nbtEditor;

    public Uninstaller(ArmoredElytra plugin, NBTEditor nbtEditor)
    {
        this.plugin = plugin;
        this.nbtEditor = nbtEditor;
    }

    public int removeArmoredElytras(Inventory inv)
    {
        int count = 0;
        ListIterator<ItemStack> iterator = inv.iterator();
        while (iterator.hasNext())
        {
            ItemStack is = iterator.next();
            if (nbtEditor.getArmorTierFromElytra(is) != ArmorTier.NONE)
            {
                iterator.set(AIR);
                ++count;
            }
        }
        return count;
    }

    public int removeArmoredElytraFromArmorSlot(Player player)
    {
        PlayerInventory inventory = player.getInventory();
        ItemStack chestplate = inventory.getChestplate();
        if (nbtEditor.getArmorTierFromElytra(chestplate) != ArmorTier.NONE)
        {
            inventory.setChestplate(null);
            return 1;
        }
        return 0;
    }

    @EventHandler
    public void onChestOpen(InventoryOpenEvent event)
    {
        Inventory inv = event.getInventory();
        int removed = removeArmoredElytras(inv);
        if (removed != 0)
            plugin.messagePlayer(event.getPlayer(), ChatColor.RED,
                                 "Removed " + removed + " armored elytras from your chest!");
    }

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event)
    {
        // Slight delay so the inventory has time to get loaded.
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                Inventory inv = event.getPlayer().getInventory();

                int removed = removeArmoredElytraFromArmorSlot(event.getPlayer());
                removed += removeArmoredElytras(inv);

                if (removed != 0)
                    plugin.messagePlayer(event.getPlayer(), ChatColor.RED,
                                         "Removed " + removed + " armored elytras from your inventory!");
            }
        }.runTaskLater(plugin, 20);
    }
}
