package feitu.epay;

import java.util.Objects;

public class NewPayInfo {
    private static String PayUrl;
    private static String Out_Trade_No;

    public NewPayInfo(String In_PayUrl, String In_Out_Trade_No) {
        PayUrl = In_PayUrl;
        Out_Trade_No = In_Out_Trade_No;
    }

    /*
    public void setOut_Trade_No(String In_Out_Trade_No) {
        this.Out_Trade_No = In_Out_Trade_No;
    }
    public void setPayUrl(String In_PayUrl) {
        this.PayUrl = In_PayUrl;
    }
    */
    public static boolean IsSucess() {
        return !Objects.equals(PayUrl, "");
    }

    public static String Get_PayUrl() {
        return PayUrl;
    }

    public static String Get_Out_Trade_No() {
        return Out_Trade_No;
    }
}
