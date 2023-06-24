package feitu.epay;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import feitu.plugin.custompay.utils.Logger;
import org.apache.commons.codec.digest.DigestUtils;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

//Using GBK have some wrong :D
public class epay {
    public static String API_Url ;
    public static String API_Pid;
    public static String API_Key;
    public static boolean SetInfo(String Url, int Pid,String Key) {
        String info = HttpFix.Get(Url + "api.php?act=query&pid=" + String.valueOf(Pid) + "&key=" + Key);
        if (info.equals("")) {
            return false;
        }
        JsonElement element = JsonParser.parseString(info);
        if (element.getAsJsonObject().get("code").getAsInt() != 1){
            return false;
        }
        API_Url = Url;
        API_Pid = String.valueOf(Pid);
        API_Key = Key;
        return true;
    }
    public static NewPayInfo NewPay(String PayMode, double Money, String Name) {
        // Create out_trade_no
        Date currentDate = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMdhhmmss" + String.valueOf((int)(Math.random() * (9999 - 1000 + 1) + 1000)));
        String formattedDate = formatter.format(currentDate);

        String[] members = {
                "clientip=0.0.0.0",
                "money=" + Double.toString(Money),
                "name=" + Name,
                "notify_url=http://127.0.0.1/SDK/notify_url.php",//回调地址乱填即可
                "out_trade_no=" + formattedDate,
                "pid=" + API_Pid,
                "type=" + PayMode
        };
        //Fucking epay sign
        Logger.log(Logger.LogType.INFO, String.join("|||", members) + API_Key);
        String sign = DigestUtils.md5Hex(String.join("&", members) + API_Key);
        members = Arrays.copyOf(members, members.length + 2);
        members[members.length - 2] = "sign=" + sign;
        members[members.length - 1] = "sign_type=MD5";
        String info = HttpFix.Get(API_Url + "mapi.php?" + String.join("&", members));
        if (info.equals("")) {
            return new NewPayInfo("", "");
        }
        JsonElement element = JsonParser.parseString(info);
        if (element.getAsJsonObject().get("code").getAsInt() == -1) {
            return new NewPayInfo("", "");
        }
        if (Objects.equals(element.getAsJsonObject().get("payurl").getAsString(), "")) {
            return new NewPayInfo("", "");
        }
        return new NewPayInfo(element.getAsJsonObject().get("payurl").getAsString(), formattedDate);
    }
    public static boolean GetIsPay(String Out_Trade_No) {
        String info = HttpFix.Get(API_Url + "api.php?act=order&pid=" + API_Pid +"&key=" + API_Key + "&out_trade_no=" + Out_Trade_No);
        if (info.equals("")) {
            return false;
        }
        JsonElement element = JsonParser.parseString(info);
        if (element.getAsJsonObject().get("code").getAsInt() != 1){
            return false;
        }
        return element.getAsJsonObject().get("status").getAsInt() == 1;
    }

}
