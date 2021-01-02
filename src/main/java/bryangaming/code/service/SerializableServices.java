package bryangaming.code.service;

import bryangaming.code.utils.serializable.UtilsSerializable;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;

public class SerializableServices {

    private PluginService pluginService;

    private UtilsSerializable utilsSerializable;

    public SerializableServices(PluginService pluginService){
        this.pluginService = pluginService;
        setup();
    }


    public void setup(){
        utilsSerializable = new UtilsSerializable(pluginService);
    }

    public UtilsSerializable getUtilsSerializable() {
        return utilsSerializable;
    }
}
