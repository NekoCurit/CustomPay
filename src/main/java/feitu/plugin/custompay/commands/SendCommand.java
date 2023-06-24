package feitu.plugin.custompay.commands;
import feitu.epay.NewPayInfo;
import feitu.epay.epay;
import feitu.plugin.custompay.Main;
import feitu.plugin.custompay.utils.CoreManager;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

public class SendCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            String playerName = p.getName();
            if (args.length == 0) {
                p.sendMessage(CoreManager.getPrefix() + CoreManager.getText("title-help"));
                return false;
            }
            /* Ö§¸¶ÃüÁî */
            if (Objects.equals(args[0], "pay")) {
                if (args.length == 1) {
                    p.sendMessage(CoreManager.getPrefix() + CoreManager.getText("title-pay-less0"));
                    return false;
                }
                int PayMoney = Integer.parseInt(args[1]);
                if (0 > PayMoney) {
                    p.sendMessage(CoreManager.getPrefix() + CoreManager.getText("title-pay-less0"));
                    return false;
                }
                if (PayMoney > 2000) {
                    p.sendMessage(CoreManager.getPrefix() + CoreManager.getText("title-pay-toomore"));
                    return false;
                }
                NewPayInfo info = epay.NewPay(CoreManager.getText("pay-mode"), PayMoney, CoreManager.getText("order-name").replace("%s", p.getName()));
                if (!info.IsSucess()) {
                    p.sendMessage(CoreManager.getPrefix() + CoreManager.getText("title-pay-err-create"));
                    return false;
                }
                p.sendMessage(CoreManager.getPrefix() + CoreManager.getText("title-pay-clickpay").replace("%s", info.Get_PayUrl()));
                Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < 120; i++) {
                            if (epay.GetIsPay(info.Get_Out_Trade_No())) {
                                Bukkit.getScheduler().runTask(Main.getInstance(), new Runnable() {
                                    @Override
                                    public void run() {
                                        ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
                                        Bukkit.getServer().dispatchCommand(console, "points give " + playerName + " " + String.valueOf(PayMoney * Main.getInstance().getConfig().getLong("ratio")));
                                        p.sendMessage(CoreManager.getPrefix() + CoreManager.getText("title-pay-sucess")
                                                .replace("%points", String.valueOf(PayMoney * Main.getInstance().getConfig().getLong("ratio")))
                                                .replace("%money", String.valueOf(PayMoney)));
                                    }
                                });
                                return;
                            }
                            try {
                                Thread.sleep(1000); // ÔÝÍ£ 1 ÃëÖÓ
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                                Bukkit.getScheduler().runTask(Main.getInstance(), new Runnable() {
                                    @Override
                                    public void run() {
                                        p.sendMessage(CoreManager.getPrefix() + CoreManager.getText("title-pay-err-throw1"));
                                    }
                                });
                                return;
                            }
                        }
                        Bukkit.getScheduler().runTask(Main.getInstance(), new Runnable() {
                            @Override
                            public void run() {
                                p.sendMessage(CoreManager.getPrefix() + CoreManager.getText("title-pay-timeout"));
                            }
                        });
                    }
                });
                return true;
            } else if (Objects.equals(args[0], "info")) {
                p.sendMessage(CoreManager.getPrefix() + CoreManager.getText("title-info").replace("%s","CatX_feitu" ));
                return true;
            }
        } else {
            if (args.length == 0) {
                sender.sendMessage(CoreManager.getPrefix() + CoreManager.getText("title-console-nosupport"));
                return false;
            }
        }
        return true;
    }
}
