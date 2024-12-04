package Client_Part.src.client.tools;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import Client_Part.src.client.ui.Chat;
import Client_Part.src.client.ui.Friend;
import common.*;

/*
    与服务器连接以后，保持联系的线程
    功能：
        1.获取在线用户列表
        2.接收文字消息、文件、表情等
*/
public class ConnectThread extends Thread {
    public Socket socket;

    private Chat chat;
    private Friend friend = null;

    public ConnectThread(Socket socket,Chat chat) {
        this.socket = socket;
        this.chat = chat;
    }

    public ConnectThread(Socket socket,Friend friend){
        this.socket = socket;
        this.friend = friend;
    }

    public void run() {
        while (true) {
            // 不停读取服务器发来的消息 可以获取好友上线消息
            try {
                MyObjectInputStream ois = new MyObjectInputStream(socket.getInputStream());
                Message msg = (Message) ois.readObject();

                System.out.println("客户端收到了: " + msg.getType());

                //文字消息-群聊私聊通用
                if (msg.getType().equals(MessageType.commonMessAll) || msg.getType().equals(MessageType.commonMessOne)) {
                    printNewMess(msg);

                    //如果是上线消息就播放提示音 笑嘻了
                    if (msg.getSender().equals("server")){
                        AudioPlayer.onlineSound();
                    }else{
                        AudioPlayer.messSound();
                    }
                    System.out.println(msg.getContain()+"\n");
                }
                else if (msg.getType().equals(MessageType.retFriend)) {// 请求好友列表
                    friend.getOnliner(msg.getContain());//得到在线列表
                    System.out.println("好友列表更新了\n");

                }else if (msg.getType().equals(MessageType.fileMessOne) || msg.getType().equals(MessageType.fileMessAll)){//获取文件

                    drawFileLabel(msg);
                    AudioPlayer.messSound();

                    System.out.println("收到的文件名：" + msg.getFileName());
                    String dest = saveFile(msg.getGetter(),msg.getSender(),msg.getFileName());

                    if (dest != null){
                        FileOutputStream fis = new FileOutputStream(dest);
                        try {
                            fis.write(msg.getFileBytes());//保存文件
                            fis.close();
                        } catch (FileNotFoundException e) {
                            AudioPlayer.alertSound(0);
                            JOptionPane.showMessageDialog(chat, "文件保存失败！",msg.getFileName(),JOptionPane.INFORMATION_MESSAGE);
                            e.printStackTrace();
                        }
                        AudioPlayer.alertSound(1);
                        JOptionPane.showMessageDialog(chat, "文件已保存", msg.getFileName(),JOptionPane.INFORMATION_MESSAGE);
                    }

                }else if (msg.getType().equals(MessageType.imageOne) || msg.getType().equals(MessageType.imageAll)){//获取图片

                    drawFileLabel(msg);

                    String dest = "Client_Part\\res\\history" + msg.getFileName();

                    if (dest != null){
                        FileOutputStream fis = new FileOutputStream(dest);
                        try {
                            fis.write(msg.getFileBytes());   
                            fis.close();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        //显示图片,发过来的其实是原图，需要再次压缩显示
                        new ImageInsert(chat.getTextPanel()).insertImage(dest);
                        System.out.println("connect显示了图片");
                    }
                }else if (msg.getType() != null && msg.getType().equals(MessageType.friendSuccess)){

                    //好友改变成功了
                    System.out.println("服务器返回的好友是：" + msg.getContain());

                    friend.updateFriend(msg.getContain());//得到修改以后的好友，进行重绘

                    AudioPlayer.alertSound(1);
                    JOptionPane.showMessageDialog(chat, "好友修改成功！","好友修改提示",JOptionPane.INFORMATION_MESSAGE);

                }else if (msg.getType() != null && msg.getType().equals(MessageType.beRemove)){

                    //好友改变成功了
                    System.out.println("服务器返回的好友是：" + msg.getContain());

                    friend.updateFriend(msg.getContain());//得到修改以后的好友，进行重绘

                    AudioPlayer.alertSound(1);
                    JOptionPane.showMessageDialog(chat, "你被别人添加或删除了好友","好友修改提示",JOptionPane.INFORMATION_MESSAGE);

                }
                else if (msg.getType() != null && msg.getType().equals(MessageType.friendFail)){
                    
                    AudioPlayer.alertSound(0);
                    JOptionPane.showMessageDialog(chat, "好友修改失败！","好友修改提示",JOptionPane.INFORMATION_MESSAGE);
                }
                else if (msg.getType().equals(MessageType.headSuccess)){//头像切换成功

                    AudioPlayer.alertSound(1);
                    JOptionPane.showMessageDialog(chat, "头像修改成功！","头像修改提示",JOptionPane.INFORMATION_MESSAGE);

                }else if (msg.getType().equals(MessageType.partnerSuccess)){

                    System.out.println("好友接收成功");
                    //插入好友、显示头像

                    System.out.println("收到被初始化的头像" + msg.getSenderHead());
                    friend.drawFriends(msg.getFriends(),msg.getSenderHead());
                }
                else if (msg.getType().equals(MessageType.Exit)){//退出
                    socket.close();
                    break;
                }

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void drawFileLabel(Message msg){
        //绘制文件接收提示
        JPanel area = chat.getTextPanel();
        JLabel nameLabel = new JLabel(msg.getTime()+" 收到了来自小黑子  "+msg.getSender()+"的文件 ",SwingConstants.RIGHT);
        nameLabel.setBorder(BorderFactory.createLineBorder(Color.ORANGE,1,true));
        area.add(nameLabel);
        area.repaint();
        area.updateUI();
    }

    private void printNewMess(Message msg){

        JPanel area = chat.getTextPanel();
        JPanel messPanel = new JPanel(new BorderLayout());
        JLabel headLabel = new JLabel(new ImageIcon(msg.getSenderHead()),SwingConstants.RIGHT);//不能用原来那个头像，不然只能显示在最后添加的组件上
        JLabel nameLabel = new JLabel("小黑子 " + msg.getSender() + ":",SwingConstants.RIGHT);
        JLabel messLabel = new JLabel(msg.getContain(),SwingConstants.RIGHT);//设置文字右对齐,不要写EAST
        messLabel.setFont(new Font("Noto Color Emoji", Font.PLAIN, 14));
        messPanel.add(headLabel,BorderLayout.EAST);
        messPanel.add(nameLabel,BorderLayout.NORTH);
        messPanel.add(messLabel,BorderLayout.CENTER);
        messPanel.setBackground(Color.WHITE);
        messPanel.setBorder(BorderFactory.createLineBorder(Color.ORANGE,1,true));
        area.add(messPanel,BorderLayout.CENTER);
        area.repaint();
        area.updateUI();

    }

    public String saveFile(String userId,String sender,String fileName){
        String dest = null;

        JOptionPane.showMessageDialog(chat,"请选择文件保存路径：","提示",JOptionPane.INFORMATION_MESSAGE);

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        int num = fileChooser.showOpenDialog(null);//确认返回值
        if (num == JFileChooser.APPROVE_OPTION){
            File file = fileChooser.getSelectedFile();//获取文件夹路径
            System.out.println("文件名: " + file + " " + fileName);
            dest = file +"\\" + fileName;//得到绝对路径
        }
        System.out.println("文件保存路径: " + dest);
        return dest;
    }

    public Socket getSocket() {
        return socket;
    }

    public Friend getFriend() {
        return friend;
    }
    
}
