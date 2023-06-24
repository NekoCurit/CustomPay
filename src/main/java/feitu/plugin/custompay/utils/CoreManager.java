package feitu.plugin.custompay.utils;
import feitu.plugin.custompay.Main;
public class CoreManager {

    public static String getPrefix() {
        return Colors.format(Main.getInstance().getConfig().getString("prefix"));
    }
    public static String getText(String info) {
        return Colors.format(Main.getInstance().getConfig().getString(info));
    }
}
