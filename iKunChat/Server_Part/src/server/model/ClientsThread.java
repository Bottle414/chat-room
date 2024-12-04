package Server_Part.src.server.model;

import java.io.IOException;
import java.net.Socket;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import common.*;

/*
    接收用户请求
    功能：
        1.返回在线用户
        2.发送私聊消息、群聊消息
        3.发送文件
        4.接收退出请求
*/
public class ClientsThread extends Thread {
    private Socket socket;
    private String userId;//发出请求的客户

    public ClientsThread(Socket socket,String userId) {
        this.socket = socket;// 把服务器与该客户端的连接给soket
        this.userId = userId;
    }

    public Socket getSocket() {
        return socket;
    }

    public void run() {
        while (true) {
            // 该线程可以接收客户端的信息了
            try {
                MyObjectInputStream ois = new MyObjectInputStream(socket.getInputStream());
                Message msg = (Message) ois.readObject();
                System.out.println("收到了客户端的要求: "+msg.getType());

                if (msg.getType().equals(MessageType.getFriend)){
                    //用户请求获取在线列表
                    getOnliner(msg.getSender());
                }else if (msg.getType().equals(MessageType.commonMessOne)){
                    //私聊
                    ClientsThread thread = ThreadManner.getClientThread(msg.getGetter(),msg.getSender());
                    if (thread!=null){
                        //发送者头像在发送时被一并传入了
                        MyObjectOutputStream oos = new MyObjectOutputStream(thread.getSocket().getOutputStream());
                        oos.writeObject(msg);
                    }else{
                        System.out.println("发送失败");
                    }
                }else if (msg.getType().equals(MessageType.commonMessAll)){
                    //群聊
                    sendToAll(msg);
                }else if (msg.getType().equals(MessageType.fileMessOne) || msg.getType().equals(MessageType.imageOne)){
                    //私发文件
                    ClientsThread thread = ThreadManner.getClientThread(msg.getGetter(),msg.getSender());
                    if (thread!=null){
                        MyObjectOutputStream oos = new MyObjectOutputStream(thread.getSocket().getOutputStream());
                        oos.writeObject(msg);
                        System.out.println(msg.getSender()+"发送了文件");
                    }else{
                        System.out.println("文件发送失败");
                    }
                }else if (msg.getType().equals(MessageType.fileMessAll) || msg.getType().equals(MessageType.imageAll)){
                    sendToAll(msg);
                }
                else if (msg.getType()!=null && msg.getType().equals(MessageType.partnerRequest)){
                    //获取了好友
                    getFriend(msg.getSender());
                }else if (msg.getType()!=null && msg.getType().equals(MessageType.headRequest)){//修改了头像
                    changeHead(msg);
                }else if (msg.getType()!=null && msg.getType().equals(MessageType.addFriend)){//增减了好友

                    String sender = msg.getSender(),friend = msg.getContain();
                    
                    if (Server.userData.get(msg.getSender()).getFriends().contains(msg.getContain())){//如果好友在这个人的好友数据库里
                        System.out.println("删除好友");
                        //删除好友，同时对另一个好友进行
                        removeFriend(sender,friend);
                        //要拿到好友的socket,所以得叫好友发
                    }else {//不在数据库，加上
                        System.out.println("添加好友");
                        addFriend(sender, friend);
                    }
                }
                else if (msg.getType().equals(MessageType.Exit)){
                    //退出聊天
                    if (msg.getGetter().equals("Online")){
                        ThreadManner.removeUser(userId);//删除对应线程
                        System.out.println(userId+"退出了服务器");
                    }else {//群聊或者私聊下线
                        ThreadManner.removeThread(msg.getSender(),msg.getGetter());
                        System.out.println(userId+"退出了和"+msg.getGetter()+"的聊天");
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void getOnliner(String sender){
        String onliner = ThreadManner.getOnlineUser();
        Message msgSend = new Message();
        msgSend.setContain(onliner);
        msgSend.setTime(new Date().toString());//打错字了厚礼蟹！
        msgSend.setType(MessageType.retFriend);
        msgSend.setGetter(sender);//发给原来那个客户 本来写的setSender

        System.out.println(sender+"拉取了在线人员");
        
        try {
            MyObjectOutputStream oos = new MyObjectOutputStream(socket.getOutputStream());
            oos.writeObject(msgSend);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendToAll(Message msg){

        HashMap<String,HashMap<String,ClientsThread>> map = ThreadManner.getMap();
        Iterator<String> it = map.keySet().iterator();
        String onlineUser;
        while (it.hasNext()) {
            onlineUser = it.next().toString();
            if (!onlineUser.equals(msg.getSender())){//用户是接收方
                msg.setGetter(onlineUser);
                ClientsThread thread = map.get(onlineUser).get("toAll");
                if (thread!=null){
                    try {
                        MyObjectOutputStream oos = new MyObjectOutputStream(thread.getSocket().getOutputStream());
                        oos.writeObject(msg);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.out.println(msg.getSender()+"发送给所有人");
                }else{
                    System.out.println("发送失败");
                }
            }
        }
    }

    private void changeHead(Message msg){
        Message sendMsg = new Message();
        sendMsg.setType(MessageType.headSuccess);
        sendMsg.setTime(new Date().toString());
        sendMsg.setGetter(msg.getSender());

        System.out.println("用户传来的新头像为：" + msg.getContain());

        Server.changeHead(msg);
       
        try {
            MyObjectOutputStream oos = new MyObjectOutputStream(socket.getOutputStream());
            oos.writeObject(sendMsg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getFriend(String sender){
        Message sendMsg = new Message();
        sendMsg.setType(MessageType.partnerSuccess);
        sendMsg.setTime(new Date().toString());
        sendMsg.setGetter(sender);
        sendMsg.setFriends(getPartner(sender));
        sendMsg.setSenderHead(Server.userData.get(sender).getHeadPath());
        System.out.println("初始化头像为：" + sendMsg.getSenderHead());//厚礼蟹，又打错字了,叫你堆史山，功能分区乱的一批
        
        System.out.println(sender+"拉取了好友");
        try {
            MyObjectOutputStream oos = new MyObjectOutputStream(socket.getOutputStream());
            oos.writeObject(sendMsg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addFriend(String sender,String friend){
        Message sendMsg = new Message();
        sendMsg.setTime(new Date().toString());
        sendMsg.setGetter(sender);

        String newFriend = Server.addFriend(sender,friend);
        sendMsg.setType(MessageType.friendSuccess);
        sendMsg.setContain(newFriend);
        try {
            MyObjectOutputStream oos = new MyObjectOutputStream(socket.getOutputStream());
            oos.writeObject(sendMsg);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //通知它的对象自己被添加或删除了好友
        ClientsThread thread = ThreadManner.getClientThread(friend,"Online");
        Message msg = new Message("server", MessageType.beRemove, Server.addFriend(friend, sender), friend);
        if (thread!=null){
            System.out.println("给对方发送了被添加消息");
            try {
                MyObjectOutputStream oos = new MyObjectOutputStream(thread.getSocket().getOutputStream());
                oos.writeObject(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
           
        }else{
            System.out.println("被添加消息发送失败");
        }
    }

    private void removeFriend(String sender,String friend){
        String newFriend = Server.removeFriend(sender,friend);
        Message sendMsg = new Message();
        sendMsg.setTime(new Date().toString());
        sendMsg.setGetter(sender);
        System.out.println("数据库返回的好友: " + newFriend);
        if ( newFriend != null){
            //发送删除成功的消息
            sendMsg.setContain(newFriend);
            sendMsg.setType(MessageType.friendSuccess);
            System.out.println("数据库说删除成功了");
        }else {
            sendMsg.setType(MessageType.friendFail);
            System.out.println("数据库说删除失败了");
        }
        
        //单独一个oos和sendMsg，避免反序列化流出错
        try {
            MyObjectOutputStream oos = new MyObjectOutputStream(socket.getOutputStream());
            oos.writeObject(sendMsg);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ClientsThread thread = ThreadManner.getClientThread(friend,"Online");
        Message msg = new Message("server", MessageType.beRemove, Server.removeFriend(friend, sender), friend);
        if (thread!=null){
            System.out.println("给对方发送了被删除消息");
            try {
                MyObjectOutputStream oos = new MyObjectOutputStream(thread.getSocket().getOutputStream());
                oos.writeObject(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
           
        }else{
            System.out.println("被删除消息发送失败");
        }
    }

        //返回用户的好友哈希表
    private HashSet<String> getPartner(String userId){
        return Server.userData.get(userId).getFriends();
    }
}
