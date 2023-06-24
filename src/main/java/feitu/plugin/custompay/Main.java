package feitu.plugin.custompay;

import feitu.plugin.custompay.commands.SendCommand;
import org.bukkit.plugin.java.JavaPlugin;

import feitu.plugin.custompay.utils.Logger;
import feitu.epay.epay;
public final class Main extends JavaPlugin {
    private static Main instance;

    public static Main getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        // systems & instance

        instance = this;
        saveDefaultConfig();
        // commands
        getCommand("cpay").setExecutor(new SendCommand());
        getCommand("custompay").setExecutor(new SendCommand());
        // logger
        Logger.log(Logger.LogType.INFO, "pay-url:" + Main.getInstance().getConfig().getString("pay-url"));
        if (!epay.SetInfo(
                Main.getInstance().getConfig().getString("pay-url"),
                Main.getInstance().getConfig().getInt("pay-pid"),
                Main.getInstance().getConfig().getString("pay-key")
        )) {
            Logger.log(Logger.LogType.INFO, "验证充值信息失败");
        }
        Logger.log(Logger.LogType.INFO, "CustomPay 充值插件已启动");
    }
    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Logger.log(Logger.LogType.INFO, "CustomPay 充值插件已卸载");
    }
}
