package Client_Part.src.client.tools;

import java.awt.Toolkit;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Date;

import javax.swing.JOptionPane;

import Client_Part.src.client.ui.Chat;
import Client_Part.src.client.ui.Friend;
import common.Message;
import common.MessageType;
import common.MyObjectInputStream;
import common.MyObjectOutputStream;
import common.User;

/*
    用户服务集成
    功能：
        1.登录验证
        2.注册
        3.获取在线用户列表
        4.退出
*/

public class UserServe {
    private User user = new User();
    private Socket socket;

    //去服务端验证账号密码
    public boolean check(String userId,String password){
        boolean success = false;//默认登录失败

        //打包
        user.setUsername(userId);
        user.setPassword(password);

        try {
            socket = new Socket(InetAddress.getLocalHost(), 8888);

            MyObjectOutputStream oos = new MyObjectOutputStream(socket.getOutputStream());//发送
            oos.writeObject(user);

            //接收并验证
            MyObjectInputStream ois = new MyObjectInputStream(socket.getInputStream());
            Message msg = (Message) ois.readObject();
            
            if (msg.getType().equals(MessageType.loginSuccess)){
                //登录成功
                System.out.println("登录成功");
                success = true;
            }
            else if (msg.getType().equals(MessageType.loginFail)){
                //登录失败
                Toolkit.getDefaultToolkit().beep();
                JOptionPane.showMessageDialog(null, "账号或密码错误！");
                socket.close();
            }
            else if (msg.getType().equals(MessageType.noUser)){
                //用户不存在
                Toolkit.getDefaultToolkit().beep();
                JOptionPane.showMessageDialog(null, "用户不存在！");
                socket.close();
            }
        }catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return success;
    }

    //注册
    public boolean register(String userId,String password){
        boolean success = false;//默认登录失败

        //打包
        user.setUsername(userId);
        user.setPassword(password);
        user.setRequest(MessageType.registerRequest);

        try {
            socket = new Socket(InetAddress.getLocalHost(), 8888);

            MyObjectOutputStream oos = new MyObjectOutputStream(socket.getOutputStream());//发送
            oos.writeObject(user);

            //接收并验证
            MyObjectInputStream ois = new MyObjectInputStream(socket.getInputStream());
            Message msg = (Message) ois.readObject();

            if (msg.getType().equals(MessageType.registerSuccess)){
                //注册成功
                success = true;
            }
            socket.close();
        }catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return success;
    }

    //修改密码
    public boolean change(String userId,String password){
        boolean success = false;//默认修改失败

        //打包
        user.setUsername(userId);
        user.setPassword(password);
        user.setRequest(MessageType.changeRequest);

        try {
            socket = new Socket(InetAddress.getLocalHost(), 8888);

            MyObjectOutputStream oos = new MyObjectOutputStream(socket.getOutputStream());//发送
            oos.writeObject(user);

            //接收并验证
            MyObjectInputStream ois = new MyObjectInputStream(socket.getInputStream());
            Message msg = (Message) ois.readObject();

            if (msg.getType().equals(MessageType.changeSuccess)){
                //修改
                System.out.println("修改成功");
                success = true;
            }
            socket.close();
        }catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return success;
    }

    //为在线用户窗口启动线程
    public void startThread(String userId,Friend friend){
        ConnectThread thread = new ConnectThread(socket, friend);
        thread.start();
        ThreadManner.addThread(userId, "Online", thread);
    }
    //为聊天对象开启线程
    public void startThreadChat(String userId,String friend,Chat chat){
        //打包
        user.setUsername(userId);
        user.setFace(friend);

        try {
            socket = new Socket(InetAddress.getLocalHost(), 8888);

            MyObjectOutputStream oos = new MyObjectOutputStream(socket.getOutputStream());//发送
            oos.writeObject(user);

        }catch (IOException e) {
            e.printStackTrace();
        }

        //开启线程并塞进对应线程池
        ConnectThread thread = new ConnectThread(socket, chat);
        thread.start();
        ThreadManner.addThread(userId, friend, thread);
    }

    //获取在线人员
    public void getOnlineUser(String userId){
        //打包
        Message msg = new Message();
        msg.setTime(new Date().toString());
        msg.setType(MessageType.getFriend);
        System.out.println("请求获取在线人员: " + msg.getType());
        msg.setSender(userId);

        ConnectThread thread = ThreadManner.getThread(userId, "Online");

        //发送
        try {
            MyObjectOutputStream oos = new MyObjectOutputStream(thread.getSocket().getOutputStream());
            oos.writeObject(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //获取好友
    public void getFriends(String userId){
        //打包
        Message msg = new Message();
        msg.setTime(new Date().toString());
        msg.setType(MessageType.partnerRequest);
        System.out.println("请求获取好友: " + msg.getType());
        msg.setSender(userId);

        ConnectThread thread = ThreadManner.getThread(userId, "Online");

        //发送
        try {
            MyObjectOutputStream oos = new MyObjectOutputStream(thread.getSocket().getOutputStream());
            oos.writeObject(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //切换头像
    public void changeHead(String userId ,String headPath){
        Message msg = new Message();
        msg.setTime(new Date().toString());
        msg.setType(MessageType.headRequest);
        System.out.println("请求将头像换为: " + headPath);
        msg.setContain(headPath);
        msg.setSender(userId);

        ConnectThread thread = ThreadManner.getThread(userId, "Online");

        //发送
        try {
            MyObjectOutputStream oos = new MyObjectOutputStream(thread.getSocket().getOutputStream());
            oos.writeObject(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //添加、删除好友--->已经是好友就删除，不是就添加，由服务器判断
    public void addFriend(String userId ,String friendId){
        Message msg = new Message();
        msg.setTime(new Date().toString());
        msg.setType(MessageType.addFriend);
        System.out.println(userId + "请求添加" + friendId + "为好友");
        msg.setContain(friendId);
        msg.setSender(userId);

        ConnectThread thread = ThreadManner.getThread(userId, "Online");

        //发送
        try {
            MyObjectOutputStream oos = new MyObjectOutputStream(thread.getSocket().getOutputStream());
            oos.writeObject(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //杀死对应线程
    public void exit(String userId,String face){
        Message msg = new Message();
        msg.setSender(userId);
        msg.setType(MessageType.Exit);
        msg.setGetter(face);

        ConnectThread thread = ThreadManner.getThread(userId, face);

        //发送
        try {
            MyObjectOutputStream oos = new MyObjectOutputStream(thread.getSocket().getOutputStream());
            oos.writeObject(msg);

            System.out.println(userId+"退出了系统");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
