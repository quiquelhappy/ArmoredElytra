package nl.pim16aap2.armoredElytra.handlers;

import nl.pim16aap2.armoredElytra.ArmoredElytra;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class LoginHandler implements Listener
{
    private final ArmoredElytra plugin;
    private final String message;

    public LoginHandler(ArmoredElytra plugin, String message)
    {
        this.plugin = plugin;
        this.message = message;
    }

    @EventHandler
    public void onLogin(PlayerLoginEvent event)
    {
        Player player = event.getPlayer();
        if (player.hasPermission("armoredElytra.admin"))
            // Slight delay so the player actually receives the message;
            plugin.getFoliaLib().getScheduler().runLater(() ->
                plugin.messagePlayer(player, ChatColor.AQUA, message), 10L);
    }
}
