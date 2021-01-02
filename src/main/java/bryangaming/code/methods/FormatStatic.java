package bryangaming.code.methods;

import org.bukkit.ChatColor;

public class FormatStatic {


    public static String setColor(String path){
        return ChatColor.translateAlternateColorCodes('&', path);
    }


    public static boolean isNumber(String path){
        try{
            Integer.parseInt(path);
            return true;
        }catch (NumberFormatException numberFormatException){
            return false;
        }

    }

}
