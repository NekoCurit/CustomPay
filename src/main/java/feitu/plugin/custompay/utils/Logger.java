package feitu.plugin.custompay.utils;
import feitu.plugin.custompay.Main;

public class Logger {

    public enum LogType {
        INFO, ERROR, WARNING;
    }

    public static void log(LogType type, String msg) {
        Main.getInstance().getServer().getConsoleSender().sendMessage(Colors.format("&8[&a"+ type.name() +" &8] &7- &a"+ msg));
    }

}
