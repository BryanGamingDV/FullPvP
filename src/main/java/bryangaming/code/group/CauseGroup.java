package bryangaming.code.group;

import bryangaming.code.methods.FormatStatic;
import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import java.util.HashMap;

public class CauseGroup {
    private static final HashMap<DamageCause, String> causeGroup = new HashMap<>();

    public CauseGroup() {
        setup();
    }

    private void setup() {
        causeGroup.put(DamageCause.ENTITY_ATTACK, "on-kill");
        causeGroup.put(DamageCause.PROJECTILE, "on-shoot");
        causeGroup.put(DamageCause.FALL, "on-fall");
        causeGroup.put(DamageCause.FIRE, "on-lava");
        causeGroup.put(DamageCause.FIRE_TICK, "on-burn");
        causeGroup.put(DamageCause.POISON, "on-poison");
        causeGroup.put(DamageCause.DROWNING, "on-drown");
    }

    public static String getCause (DamageCause cause){
            return causeGroup.get(cause);
    }

}