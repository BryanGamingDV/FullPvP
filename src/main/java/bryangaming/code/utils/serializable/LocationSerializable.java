package bryangaming.code.utils.serializable;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class LocationSerializable {

    public static Location fromString(String location) {
        String[] split = location.split(",");

        return new Location(Bukkit.getWorld(split[0].trim()), Double.parseDouble(split[1]), Double.parseDouble(split[2]), Double.parseDouble(split[3]), Float.parseFloat(split[4]), Float.parseFloat(split[5]));
    }

    public static String toString(Location location){
        return location.getWorld().getName() + " ," + location.getX() + " ," + location.getY() + " ," + location.getZ() + " ," + location.getYaw() + " ," + location.getPitch();
    }

}