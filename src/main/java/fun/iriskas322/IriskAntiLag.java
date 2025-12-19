package fun.iriskas322;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.vehicle.VehicleCreateEvent;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.HashMap;
import java.util.Map;
public final class IriskAntiLag extends JavaPlugin implements Listener {
    private final Map<Chunk, Integer> entityCountPerChunk = new HashMap<>();
    private final String limitMessage = "§cЛимит чанка превышен! Максимум 8 сущностей на чанк.";
    private final int MAX_ENTITIES_PER_CHUNK = 8;
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        getLogger().info("ирискантелаг включен васап мабои by iriskas322.fun");
    }
    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        Chunk chunk = event.getEntity().getLocation().getChunk();
        int entityCount = entityCountPerChunk.getOrDefault(chunk, 0);
        if (entityCount >= MAX_ENTITIES_PER_CHUNK) {
            event.setCancelled(true);
            return;
        }
        entityCountPerChunk.put(chunk, entityCount + 1);
    }
    @EventHandler
    public void onItemSpawn(ItemSpawnEvent event) {
        return;
    }
    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        Chunk chunk = event.getEntity().getLocation().getChunk();
        int entityCount = entityCountPerChunk.getOrDefault(chunk, 0);
        if (entityCount >= MAX_ENTITIES_PER_CHUNK) {
            event.setCancelled(true);
            return;
        }
        entityCountPerChunk.put(chunk, entityCount + 1);
    }
    @EventHandler
    public void onVehicleCreate(VehicleCreateEvent event) {
        Chunk chunk = event.getVehicle().getLocation().getChunk();
        int entityCount = entityCountPerChunk.getOrDefault(chunk, 0);
        if (entityCount >= MAX_ENTITIES_PER_CHUNK) {
            event.setCancelled(true);
            event.getVehicle().remove();
            return;
        }
        entityCountPerChunk.put(chunk, entityCount + 1);
    }
    @EventHandler
    public void onEntityDeath(org.bukkit.event.entity.EntityDeathEvent event) {
        removeEntityFromCount(event.getEntity());
    }
    @EventHandler
    public void onProjectileHit(org.bukkit.event.entity.ProjectileHitEvent event) {
        removeEntityFromCount(event.getEntity());
    }
    @EventHandler
    public void onItemDespawn(org.bukkit.event.entity.ItemDespawnEvent event) {
        if (!(event.getEntity() instanceof Item)) {
            removeEntityFromCount(event.getEntity());
        }
    }
    @EventHandler
    public void onVehicleDestroy(VehicleDestroyEvent event) {
        removeEntityFromCount(event.getVehicle());
    }
    @EventHandler
    public void onEntityExplode(org.bukkit.event.entity.EntityExplodeEvent event) {
        for (Entity entity : event.getEntity().getNearbyEntities(10, 10, 10)) {
            if (entity.getLocation().getChunk().equals(event.getEntity().getLocation().getChunk())) {
                removeEntityFromCount(entity);
            }
        }
        removeEntityFromCount(event.getEntity());
    }
    private void removeEntityFromCount(Entity entity) {
        if (entity instanceof Item) {
            return;
        }
        Chunk chunk = entity.getLocation().getChunk();
        int currentCount = entityCountPerChunk.getOrDefault(chunk, 0);
        if (currentCount > 0) {
            entityCountPerChunk.put(chunk, currentCount - 1);
        } else {
            entityCountPerChunk.remove(chunk);
        }
    }
    private void sendLimitMessage(Player player) {
        player.sendActionBar(limitMessage);
    }
}