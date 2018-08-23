package net.piratjsk.fixpegasuseffect;

import org.bukkit.Material;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public final class PegasusEffectCure extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onPlayerHopOnHorseWhileFloating(final VehicleEnterEvent event) {
        if (!event.getEntered().getType().equals(EntityType.PLAYER)) {
            // we only care about players
            return;
        }
        final AbstractHorse horse = (AbstractHorse) event.getVehicle();
        final Player player = (Player) event.getEntered();
        // if player is floating (moving uppwards in air)
        if (player.getVelocity().getY() > 0 && player.getLocation().getBlock().getType().equals(Material.AIR)) {
            try {
                final Object connection = this.getPlayerConnection(player);
                final Field floating = this.getAccessibleField(connection, "B");
                floating.setBoolean(connection, false);
                final Field ticksFloating = this.getAccessibleField(connection, "C");
                ticksFloating.setInt(connection, 0);
            } catch (final NoSuchFieldException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                // something went wrong, os we just cancel the event and player will have to enter the vehicle once again this time not floating
                event.setCancelled(true);
            }
        }
    }

    private Object getPlayerConnection(final Player player) throws NoSuchMethodException, NoSuchFieldException, IllegalAccessException, InvocationTargetException {
        final Method getHandle = player.getClass().getMethod("getHandle");
        final Object nmsPlayer = getHandle.invoke(player);
        final Field connectionField = nmsPlayer.getClass().getField("playerConnection");
        return connectionField.get(nmsPlayer);
    }

    private Field getAccessibleField(final Object object, final String fieldName) throws NoSuchFieldException {
        final Field field = object.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return field;
    }

}
