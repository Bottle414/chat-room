package Server_Part.src.server.model;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import common.*;

/*
    服务器后台管理
    功能:
        1.连接客户端
        2.验证账号密码
        3.用户注册
        4.修改和读取数据库
*/

public class Server {
    // 服务器
    ServerSocket serverSocket;
    Socket socket;
    public static ConcurrentHashMap<String,User> userData = new ConcurrentHashMap<>();

    static Properties dataBase = new Properties();//开启数据库
    
        public Server() {
    
            System.out.println("服务器已打开");
    
            initUser();//加载初始数据
    
            try {
                serverSocket = new ServerSocket(8888);// 只能在本地监听，所以不需要ip
                while (true) {
                    socket = serverSocket.accept();
    
                    //接收客户端的消息
                    MyObjectInputStream ois = new MyObjectInputStream(socket.getInputStream());
                    User user = (User)ois.readObject();
    
                    //发送回应
                    Message msg = new Message();
                    MyObjectOutputStream oos = new MyObjectOutputStream(socket.getOutputStream());
                    if (user.getRequest() != null && user.getRequest().equals(MessageType.registerRequest)){
                        if (!haveRegister(user.getUsername(),user)){
                            //未被注册过
                            System.out.println("注册成功");
                            addUser(user);
                            msg.setType(MessageType.registerSuccess);
                            oos.writeObject(msg);
                        }else{
                            msg.setType(MessageType.registerFail);
                            oos.writeObject(msg);
                        }
                        user = null;
                        socket.close();
                    }else if (user.getRequest() != null && user.getRequest().equals(MessageType.changeRequest)){
                        if (userData.get(user.getUsername())!=null){//用户存在
                            System.out.println("修改密码");
                            removeUser(user.getUsername(),user.getPassword());
                            //addUser(user);
                            msg.setType(MessageType.changeSuccess);
                            oos.writeObject(msg);
                        }
                        user = null;
                    }
                    else if (user.getFace()!=null){//聊天
                        ClientsThread thread = new ClientsThread(socket,user.getUsername());//为用户新建线程
                        thread.start();
    
                        //塞入线程池
                        ThreadManner.addCientThread(user.getUsername(),user.getFace(), thread);
                        if (user.getFace().equals("toAll")){
                            System.out.println("群聊");
                        }else {
                            System.out.println("私聊");
                        }
                    }else {
                        //登录
                        String status = check(user.getUsername(),user.getPassword());
                        if (status.equals(MessageType.loginSuccess)){
                            //登录成功
                            System.out.println("登陆成功");
                            ClientsThread thread = new ClientsThread(socket,user.getUsername());
    
                            //给当前所有人发送上线提示
                            sendOnlineWarning(user);
    
                            msg.setType(MessageType.loginSuccess);
                            oos.writeObject(msg);
                            thread.start();
                            
                            ThreadManner.addCientThread(user.getUsername(), "Online",thread);
                        }else if (status.equals(MessageType.loginFail)) {
                            //登录失败
                            msg.setType(MessageType.loginFail);
                            oos.writeObject(msg);
                            socket.close();
                        }else if (status.equals(MessageType.noUser)){//用户不存在
                            msg.setType(MessageType.noUser);
                            oos.writeObject(msg);
                            socket.close();
                        }
                    }
                }
            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
            }
        }
    
        private String check(String userName,String password){
            User user = userData.get(userName);
    
            if (user == null){
                System.out.println("用户不存在");
                return MessageType.noUser;
            }else if (!(user.getPassword().equals(password))){
                System.out.println("账号或密码错误");
                return MessageType.loginFail;
            }
            return MessageType.loginSuccess;
        }
    
        //用户是否注册过
        private boolean haveRegister(String userName,User user){
            User haveUser = userData.get(userName);
            if (haveUser!=null){//这个用户注册过了
                System.out.println("该用户已经注册！");
                return true;
            }
    
            userData.put(userName, user);//未注册，塞入数据库
            return false;
        }
    
        //初始化数据库
        private void initUser(){
            try {
                FileInputStream fis = new FileInputStream("Server_Part\\src\\server\\database\\UserData.properties");
                dataBase.load(fis);//加载配置文件
                Set<String> keys = dataBase.stringPropertyNames();//获取所有用户键值
    
                //加载进用户哈希表
                /*
                    如果配置文件格式一样，会导致数据被覆盖，毕竟是set存储
                */
                String name = "",code = "",head="";
                String[] friendTemp;
                for (String key : keys){
                    if (key.endsWith(".username")) {
                        name = dataBase.getProperty(key);
                        code = dataBase.getProperty(key.replace(".username", ".password"));//直接找到对应的password
                        friendTemp = dataBase.getProperty(key.replace(".username", ".friend")).split("-");//读出来是null 又打错字了尼玛啊！
                        head = dataBase.getProperty(key.replace(".username", ".head"));
    
                        //将好友加入哈希表
                        HashSet<String> friends = new HashSet<>();
                        for (String s : friendTemp){
                            friends.add(s);
                        }
    
                        // 输出用户名和密码
                        userData.put(name, new User(name, code,friends,"Client_Part\\res\\image\\head" + head + ".jpg"));
                        System.out.println(userData.get(name).toString());
                        System.out.println("username: " + name + " password: " + code + "friends num: " + friends.toString() + "head: " + head);
                        //friend的获取有问题，暂时放下
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    
        public static void writeToBase(String name,String code,String head,String friend){
            try {
                FileOutputStream fos = new FileOutputStream("Server_Part\\src\\server\\database\\UserData.properties");
                String value,newMess;
                for (Object key : dataBase.keySet()){
                    value = (String)key;
                    if (value.endsWith(".username")) {
                        name = dataBase.getProperty(value);
                        code = dataBase.getProperty(value.replace(".username", ".password"));//直接找到对应的password
                        head = dataBase.getProperty(value.replace(".username", ".head"));
                        friend = dataBase.getProperty(value.replace(".username", ".friend"));
    
                        newMess = "\n" + name + ".username=" + name + "\n" + name + ".password=" +code + "\n" + name + ".friend=" + friend + "\n" + name + ".head=" + head;
                        fos.write(newMess.getBytes());
                    }
                }
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    
        //修改头像
        public static void changeHead(Message msg){
            User user = userData.get(msg.getSender());
            String name = user.getUsername();
            String code = user.getPassword();
            String head = msg.getContain();
            String friend = "";
            System.out.println("new head " + head);
            //dataBase.remove(user + ".head");会覆盖，不用移除了
            dataBase.put(name + ".head", head);
        
            writeToBase(name,code,head,friend);
    }

    //用户注册
    private void addUser(User user){
        String name = user.getUsername();
        String code = user.getPassword();
        String head = "3";//default head
        try {
            FileOutputStream fos = new FileOutputStream("Server_Part\\src\\server\\database\\UserData.properties",true);
            String buff = "\n" + name + ".username=" + name + "\n" + name + ".password=" + code + "\n" + name + ".friend=" + "\n" + name + ".head=" + head;
            fos.write(buff.getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        dataBase.setProperty(name + ".username", name);
        dataBase.setProperty(name + ".password", code);
        dataBase.setProperty(name + ".friend", name);//第一个好友设为自己，空指针会炸
        dataBase.setProperty(name + ".head", head);
        HashSet<String> friendSet = new HashSet<>();
        friendSet.add(name);
        userData.put(name, new User(name, code, friendSet, "Client_Part\\res\\image\\head" + head + ".jpg"));
        System.out.println(name + "的信息已加入数据库");
    }

    private void removeUser(String user,String newCode){//修改用户信息
        dataBase.setProperty(user + ".password", newCode);
        writeToBase(user, newCode, "", "");
        userData.put(user, new User(user, newCode, userData.get(user).getFriends(), userData.get(user).getHeadPath()));
        System.out.println(user + "修改了密码");
    }

    public static String addFriend(String userId,String friendId){//为用户添加好友,返回新的好友列表
        String code = dataBase.getProperty(userId + ".password");
        String head = dataBase.getProperty(userId + ".head");
        String curFriend = dataBase.getProperty(userId + ".friend");
        System.out.println("当前好友: " + curFriend);
        String newFriend = curFriend + "-" + friendId;
        dataBase.put(userId + ".friend", newFriend);

        //更新userData数据库
        HashSet<String> friendSet = new HashSet<>();
        String[] update = newFriend.split("-");
        for (String s : update){
            friendSet.add(s);
        }
        userData.put(userId, new User(userId, userData.get(userId).getPassword(), friendSet, head));

        writeToBase(userId, code, head, newFriend);
        System.out.println(userId + "添加了好友");
        return newFriend;
    }

    public static String removeFriend(String userId,String friendId){//为用户减少好友
        String code = dataBase.getProperty(userId + ".password");
        String head = dataBase.getProperty(userId + ".head");
        String[] curFriend = dataBase.getProperty(userId + ".friend").split("-");
        if (curFriend.length == 0){
            return null;
        }
        System.out.println("当前好友: " + dataBase.getProperty(userId + ".friend"));
        String newFriend = "";
        //由于存储形式的原因，第一个需要单独处理
        if (!curFriend[0].equals(friendId)){//要删的是第一个，newFriend初始值不变
            System.out.println("friend[0]: " + curFriend[0]);
            newFriend = curFriend[0];//不然就初始化为第一个。免得数据库存储结构改变。-也带上了，需要删去。
        }
        for (int i = 1; i < curFriend.length ;i++){
            if (!curFriend[i].equals(friendId)){//是它就跳过
                newFriend = newFriend + "-" + curFriend[i];
            }
        }
        dataBase.put(userId + ".friend", newFriend);

        //更新userData数据库
        HashSet<String> friendSet = new HashSet<>();
        String[] update = newFriend.split("-");
        for (String s : update){
            friendSet.add(s);
        }
        userData.put(userId, new User(userId, userData.get(userId).getPassword(), friendSet, head));

        writeToBase(userId, code, head, newFriend);
        System.out.println(userId + "减少了好友");
        return newFriend;
    }

    private void sendOnlineWarning(User user){
        //给所有人发上线消息
        Message onlineWarning = new Message();
        onlineWarning.setType(MessageType.commonMessAll);//共用群发消息
        onlineWarning.setSender("server");
        onlineWarning.setContain("---------------------------🎉-----------小黑子  " + user.getUsername() + "  上线了!!!!!!!!!!-----------🎊---------------------------");

        HashMap<String,HashMap<String,ClientsThread>> map = ThreadManner.getMap();
        Iterator<String> it = map.keySet().iterator();
        String onlineUser;
        while (it.hasNext()) {
            onlineUser = it.next().toString();
            if (!onlineUser.equals(onlineWarning.getSender())){//用户是接收方
                onlineWarning.setGetter(onlineUser);
                ClientsThread t = map.get(onlineUser).get("toAll");
                if (t!=null){
                    try {
                        MyObjectOutputStream oos = new MyObjectOutputStream(t.getSocket().getOutputStream());
                        oos.writeObject(onlineWarning);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.out.println("发送了上线消息给: " + onlineUser);
                }else{
                    System.out.println("上线消息发送失败");
                }
            }
        }
    }
}
