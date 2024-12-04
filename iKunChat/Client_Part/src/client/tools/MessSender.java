package Client_Part.src.client.tools;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Client_Part.src.client.ui.Chat;
import common.Message;
import common.MessageType;
import common.MyObjectOutputStream;

public class MessSender {
    private Chat chat;
    ImageIcon headIcon;
    String headPath;

    public MessSender(Chat chat){
        this.chat = chat;
    }

    //私发消息
    public void sendToSingle(String sender,String getter,String contain){
        Message msg = new Message();

        msg.setType(MessageType.commonMessOne);
        msg.setSender(sender);
        msg.setGetter(getter);
        msg.setContain(contain);
        msg.setSenderHead(headPath);//相对路径
        msg.setTime(new Date().toString());

        //绘制新消息
        paintNewMess(msg);

        System.out.println(msg.getContain()+"\n");

        ConnectThread thread = ThreadManner.getThread(sender, getter);
        try {
            MyObjectOutputStream oos = new MyObjectOutputStream(thread.getSocket().getOutputStream());
            oos.writeObject(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //群聊消息
    public void sendToAll(String sender,String contain){
        Message msg = new Message();

        msg.setType(MessageType.commonMessAll);
        msg.setSender(sender);
        msg.setContain(contain);
        msg.setSenderHead(headPath);
        msg.setTime(new Date().toString());

        //绘制新消息
        paintNewMess(msg);

        ConnectThread thread = ThreadManner.getThread(sender,"toAll");
        try {
            MyObjectOutputStream oos = new MyObjectOutputStream(thread.getSocket().getOutputStream());
            oos.writeObject(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void paintNewMess(Message msg){
        JPanel area = chat.getTextPanel();
        JPanel messPanel = new JPanel(new BorderLayout());
        JLabel headLabel = new JLabel(new ImageIcon(chat.getHeadPath()));//不能用原来那个头像，不然只能显示在最后添加的组件上
        JLabel nameLabel = new JLabel(msg.getTime()+"(我)");
        JLabel messLabel = new JLabel(msg.getContain());
        messLabel.setFont(new Font("Noto Color Emoji", Font.PLAIN, 14));
        messPanel.add(headLabel,BorderLayout.WEST);
        messPanel.add(nameLabel,BorderLayout.NORTH);
        messPanel.add(messLabel,BorderLayout.CENTER);
        messPanel.setBackground(Color.WHITE);
        messPanel.setBorder(BorderFactory.createLineBorder(Color.BLUE,1,true));
        area.add(messPanel,BorderLayout.CENTER);
        area.repaint();
        area.updateUI();
    }

    public ImageIcon getHeadIcon() {
        return headIcon;
    }

    public void setHeadIcon(ImageIcon headIcon) {
        this.headIcon = headIcon;
    }

    public String getHeadPath() {
        return headPath;
    }

    public void setHeadPath(String headPath) {
        this.headPath = headPath;
    }

    
}
