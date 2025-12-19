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
    private final Map<Chunk, Integer> huy = new HashMap<>();
    private final String pidoras = "§cЛимит чанка превышен! Максимум 8 сущностей на чанк.";
    private final int EBANYY_LIMIT = 8;
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        getLogger().info("ирискантелаг включен васап мабои by iriskas322.fun");
    }
    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        shluha(event.getEntity());
    }
    @EventHandler
    public void onItemSpawn(ItemSpawnEvent event) {
        return;
    }
    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        shluha(event.getEntity());
    }
    @EventHandler
    public void onVehicleCreate(VehicleCreateEvent event) {
        Chunk chunk = event.getVehicle().getLocation().getChunk();
        int eblan = huy.getOrDefault(chunk, 0);
        if (eblan >= EBANYY_LIMIT) {
            event.setCancelled(true);
            event.getVehicle().remove();
            return;
        }
        huy.put(chunk, eblan + 1);
    }
    @EventHandler
    public void onEntityDeath(org.bukkit.event.entity.EntityDeathEvent event) {
        pohuy(event.getEntity());
    }
    @EventHandler
    public void onProjectileHit(org.bukkit.event.entity.ProjectileHitEvent event) {
        pohuy(event.getEntity());
    }
    @EventHandler
    public void onItemDespawn(org.bukkit.event.entity.ItemDespawnEvent event) {
        if (!(event.getEntity() instanceof Item)) {
            pohuy(event.getEntity());
        }
    }
    @EventHandler
    public void onVehicleDestroy(VehicleDestroyEvent event) {
        pohuy(event.getVehicle());
    }
    @EventHandler
    public void onEntityExplode(org.bukkit.event.entity.EntityExplodeEvent event) {
        for (Entity entity : event.getEntity().getNearbyEntities(10, 10, 10)) {
            if (entity.getLocation().getChunk().equals(event.getEntity().getLocation().getChunk())) {
                pohuy(entity);
            }
        }
        pohuy(event.getEntity());
    }
    private void shluha(Entity entity) {
        Chunk chunk = entity.getLocation().getChunk();
        int pizdets = huy.getOrDefault(chunk, 0);
        if (pizdets >= EBANYY_LIMIT) {
            if (entity.getType() == EntityType.DROPPED_ITEM) {
                return;
            }
            entity.remove();
            return;
        }
        huy.put(chunk, pizdets + 1);
    }
    private void pohuy(Entity entity) {
        if (entity instanceof Item) {
            return;
        }
        Chunk chunk = entity.getLocation().getChunk();
        int nahuy = huy.getOrDefault(chunk, 0);
        if (nahuy > 0) {
            huy.put(chunk, nahuy - 1);
        } else {
            huy.remove(chunk);
        }
    }
    private void otvali(Player player) {
        player.sendActionBar(pidoras);
    }
}
