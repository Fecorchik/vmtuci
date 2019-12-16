package msg;

import auth.Encryption;
import auth.User;
import io.netty.channel.ChannelHandlerContext;

import java.net.InetSocketAddress;
import java.util.HashMap;


public class Global {
    private static HashMap<Integer, User> users = new HashMap<>();

    public static HashMap<Integer,User> getuser(){
        return users;
    }

    public static User getUser(Integer id) {
        return users.get(id);
    }


    private static HashMap<Integer, Integer> ports = new HashMap<>();
    public static int getid(int port) {
        return ports.get(port);
    }
    public static void addPort(int port, int id) {
        ports.put(port, id);
    }

    public static int getPort(ChannelHandlerContext ctx){
       return ((InetSocketAddress)ctx.channel().remoteAddress()).getPort();
    }


    public static void addUser(User user) {
        users.put(user.getId(), user);
    }

    public static void removeUser(Integer id){ users.remove(id);}

    public static String send(String sub, String payload) {
        return Encryption.toBase64(sub) + "." + Encryption.toBase64(payload);
    }

    public static String[] get(String text) {
        String[] mas = text.split("\\.");
        mas[0] = Encryption.decodeBase64(mas[0]);
        mas[1] = Encryption.decodeBase64(mas[1]);
        return mas;
    }

}
