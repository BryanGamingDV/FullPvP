package bryangaming.code.service;

import bryangaming.code.group.CauseGroup;
import bryangaming.code.group.EnchantmentGroup;

public class GroupServices {


    private final PluginService pluginService;

    private EnchantmentGroup enchantmentGroup;
    private CauseGroup causeGroup;

    public GroupServices(PluginService pluginService){
        this.pluginService = pluginService;
        setup();
    }

    private void setup(){
        enchantmentGroup = new EnchantmentGroup();
        causeGroup = new CauseGroup();
    }

    private EnchantmentGroup getEnchantment(){
        return enchantmentGroup;
    }

    private CauseGroup getDamageCause(){
        return causeGroup;
    }
}
