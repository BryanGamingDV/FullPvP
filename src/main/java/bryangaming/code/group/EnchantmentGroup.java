package bryangaming.code.group;

import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;

import java.util.HashMap;

public class EnchantmentGroup {

    private static final HashMap<String, Enchantment> enchantmentList = new HashMap<>();

    public EnchantmentGroup(){
        setup();
    }

    private void setup() {

        if (Bukkit.getVersion().equalsIgnoreCase("1.16")){
            enchantmentList.put("soul_speed", Enchantment.SOUL_SPEED);
        }

        if (Bukkit.getVersion().equalsIgnoreCase("1.14")){
            enchantmentList.put("multi_shot", Enchantment.MULTISHOT);
            enchantmentList.put("quick_charge", Enchantment.QUICK_CHARGE);
        }

        if (Bukkit.getVersion().equalsIgnoreCase("1.9")){
            enchantmentList.put("mending", Enchantment.MENDING);
        }

        enchantmentList.put("protection", Enchantment.PROTECTION_ENVIRONMENTAL);
        enchantmentList.put("fire_protection", Enchantment.PROTECTION_FIRE);
        enchantmentList.put("projectile_protection", Enchantment.PROTECTION_PROJECTILE);
        enchantmentList.put("blast_protection", Enchantment.PROTECTION_EXPLOSIONS);
        enchantmentList.put("feather_falling", Enchantment.PROTECTION_FALL);
        enchantmentList.put("thorns", Enchantment.THORNS);
        enchantmentList.put("unbreaking", Enchantment.DURABILITY);
        enchantmentList.put("fire", Enchantment.ARROW_FIRE);
        enchantmentList.put("sharpness", Enchantment.DAMAGE_ALL);
        enchantmentList.put("knockback", Enchantment.KNOCKBACK);
        enchantmentList.put("fortune", Enchantment.LUCK);
        enchantmentList.put("fire_aspect", Enchantment.FIRE_ASPECT);
        enchantmentList.put("loot_blocks", Enchantment.LOOT_BONUS_BLOCKS);
        enchantmentList.put("loot_mobs", Enchantment.LOOT_BONUS_MOBS);
        enchantmentList.put("silk_touch", Enchantment.SILK_TOUCH);
        enchantmentList.put("efficiency", Enchantment.DIG_SPEED);
        enchantmentList.put("luck_of_the_sea", Enchantment.LURE);
        enchantmentList.put("punch", Enchantment.ARROW_KNOCKBACK);
        enchantmentList.put("power", Enchantment.ARROW_DAMAGE);
        enchantmentList.put("infinity", Enchantment.ARROW_INFINITE);
    }

    public static Enchantment getEnchantment(String enchantment){
        return enchantmentList.get(enchantment);
    }
}
