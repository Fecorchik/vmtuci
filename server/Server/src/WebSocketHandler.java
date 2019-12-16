import DataBase.DataBaseHandler;
import DataBase.People;
import auth.Encryption;
import auth.User;
import com.google.gson.Gson;
import msg.Global;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

import javax.swing.text.html.ListView;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class WebSocketHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {

        if (msg instanceof WebSocketFrame) {
            System.out.println("This is a WebSocket frame");
            System.out.println("Client Channel : " + ctx.channel());
            if (msg instanceof BinaryWebSocketFrame) {
                System.out.println("BinaryWebSocketFrame Received : ");
                System.out.println(((BinaryWebSocketFrame) msg).content());
            } else if (msg instanceof TextWebSocketFrame) {

                System.out.println("TextWebSocketFrame Received : ");
                String textMessage = ((TextWebSocketFrame) msg).text();
                String[] mas = Global.get(textMessage);
                User user;
                switch (mas[0]) {
                    case "auth":{
                        user = new Gson().fromJson(mas[1], User.class);
                        auth(ctx, user);
                        break;
                    }
                    case "authvk":{
                        user = new Gson().fromJson(mas[1], User.class);
                        authvk(ctx, user);
                        break;
                    }
                    case "reg":{
                        user = new Gson().fromJson(mas[1], User.class);
                        reg(ctx, user);
                        break;
                    }
                    case "chat": {
                        chat(ctx, textMessage, mas);
                        break;
                    }
                    case "getAllUsers":{
                        getAllUsers(ctx);
                        break;
                    }
                    case "findePeople":{
                        user = new Gson().fromJson(mas[1], User.class);
                        findePeople(ctx, user);
                        break;
                    }
                    case "messageAllUsers":{
                        Msg message  = new Gson().fromJson(mas[1], Msg.class);
                        messageAllUsers(ctx, message);
                        break;
                    }
                }

            } else if (msg instanceof CloseWebSocketFrame) {
                System.out.println("CloseWebSocketFrame Received : ");
                System.out.println("ReasonText :" + ((CloseWebSocketFrame) msg).reasonText());
                System.out.println("StatusCode : " + ((CloseWebSocketFrame) msg).statusCode());
            } else {
                System.out.println("Unsupported WebSocketFrame");
            }
        }
    }

    private void chat(ChannelHandlerContext ctx, String textMessage, String[] mas) {

    }
    private void getAllUsers(ChannelHandlerContext ctx){
        DataBaseHandler dblogin = new DataBaseHandler();
        ArrayList<People> listUser = null;
        listUser = dblogin.getAllUser();
        String str = Global.send("AllUsers", new Gson().toJson(listUser));
        ctx.writeAndFlush(new TextWebSocketFrame(str));
    }
    private void findePeople(ChannelHandlerContext ctx, User user){
        DataBaseHandler dblogin = new DataBaseHandler();
        ArrayList<People> listUser = null;
        listUser = dblogin.FindePeople(user);
        String str = Global.send("findePeople", new Gson().toJson(listUser));
        ctx.writeAndFlush(new TextWebSocketFrame(str));
    }
    private void messageAllUsers(ChannelHandlerContext ctx, Msg message){
        User user = Global.getUser(Global.getid(Global.getPort(ctx)));
        System.out.println(user);
        String str = Global.send("msgAllUsers", new Gson().toJson(message));

        for(Map.Entry<Integer,User> u : Global.getuser().entrySet()){
            u.getValue().getCtx().writeAndFlush(new TextWebSocketFrame(str));
        }
   }

    private void auth(ChannelHandlerContext ctx, User user) {
        System.out.println("Authorization");
        System.out.println(user);
        DataBaseHandler dblogin = new DataBaseHandler();
        ArrayList<People> listUser = null;
        try {
            user = dblogin.getInfoUser(user.getLogin(), Encryption.toSha256(user.getPass()));
            listUser = dblogin.getAllUser();

            if (user != null) {
                user.setCtx(ctx);
                Global.addUser(user);
                Global.addPort(Global.getPort(ctx),user.getId());
            }

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        if (user.getId() != 0) {
            String str = Global.send("UserIsFound", new Gson().toJson(user));
            ctx.write(new TextWebSocketFrame(str));
            str = Global.send("AllUsers", new Gson().toJson(listUser));
            ctx.writeAndFlush(new TextWebSocketFrame(str));

        } else
            ctx.writeAndFlush(new TextWebSocketFrame("UserNotFound"));
    }

    private void authvk(ChannelHandlerContext ctx, User user) {
        DataBaseHandler dblogin = new DataBaseHandler();

        User us = user;
        try {
            us = dblogin.getInfoUser(us.getLogin(), Encryption.toSha256(us.getPass()));
            if(us.getLogin() == null && us.getPass() == null && us.getFirstname() ==null && us.getLastname() == null && us.getDateofBirth() == null){
                if (dblogin.CheckLogin(user.getLogin())) {
                    dblogin.signUpUser(user);
                }
            }
            user = dblogin.getInfoUserVk(user.getLogin(), user.getFirstname(), user.getLastname());

            if (user.getLogin() != null && user.getPass() != null && user.getFirstname() !=null && user.getLastname() != null && user.getDateofBirth() != null) {
                user.setCtx(ctx);
                Global.addUser(user);
                Global.addPort(Global.getPort(ctx),user.getId());

            }

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        if (user.getId() != 0) {
            ArrayList<People> listUser = dblogin.getAllUser();
            System.out.println(user);
            String str = Global.send("UserIsFound", new Gson().toJson(user));
            ctx.write(new TextWebSocketFrame(str));
            str = Global.send("AllUsers", new Gson().toJson(listUser));
            ctx.writeAndFlush(new TextWebSocketFrame(str));

        } else
            ctx.writeAndFlush(new TextWebSocketFrame("UserNotFound"));
    }
    private void reg(ChannelHandlerContext ctx, User user) {
        try {
            user.setPass(Encryption.toSha256(user.getPass()));
            DataBaseHandler dblogin = new DataBaseHandler();

            if (dblogin.CheckLogin(user.getLogin())) {
                dblogin.signUpUser(user);
                ctx.writeAndFlush(new TextWebSocketFrame("LoginAvailable"));
            } else
                ctx.writeAndFlush(new TextWebSocketFrame("LoginUnavailable"));

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

}