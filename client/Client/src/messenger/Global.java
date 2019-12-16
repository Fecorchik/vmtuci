package messenger;

import com.neovisionaries.ws.client.WebSocket;

public class Global {
    public static WebSocket ws;

    public static WebSocket getWs() {
        return ws;
    }
    public static void setWs(WebSocket ws) {
        Global.ws = ws;
    }

    public static String send(String sub, String payload){
        return  Encryption.toBase64(sub) +"."+ Encryption.toBase64(payload);
    }
    public static String send(String sub){
        return  Encryption.toBase64(sub);
    }

    public static String[] get(String text){
        String[] mas = text.split("\\.");
        mas[0] = Encryption.decodeBase64(mas[0]);
        mas[1] = Encryption.decodeBase64(mas[1]);
        return mas;
    }



    public static String access_token = "";

    public static String getAccess_token() {
        return access_token;
    }

    public static void setAccess_token(String access_token) {
        Global.access_token = access_token;
    }

    public static String getUser_id() {
        return user_id;
    }

    public static void setUser_id(String user_id) {
        Global.user_id = user_id;
    }

    public static String user_id = "";
}
