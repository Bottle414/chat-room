package Client_Part.src.client.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import javax.swing.*;

import Client_Part.src.client.tools.*;

/*
    用户聊天界面
*/

public class Chat extends JFrame implements MouseListener,WindowListener {

    JPanel textPanel;
    JScrollPane textScrollPane;
    JTextField textField;
    JButton sendButton;
    JButton fileSendButton;//发送文件
    JButton imageSendButton;//发送图片
    JButton emojiButton;
    JPanel sendPanel;

    Emoji emojiPane;

    UserServe userServe;//服务后台

    String fileName;//发送的文件名
    MessSender messSender;
    FileSender fileSender;
    ImageInsert imageInsert;//图片压缩工具

    String userName;
    String friend;

    ImageIcon headIcon;
    String headPath = "Client_Part\\res\\image\\head3.jpg";//defult

    public Chat(String friend, String userName,String headPath) {

        this.headPath = headPath;

        System.out.println(Thread.currentThread().getName()+" 已经创建");

        userServe = new UserServe();
        userServe.startThreadChat(userName,friend,this);

        //初始化
        this.messSender = new MessSender(this);
        messSender.setHeadPath(headPath);
        this.fileSender = new FileSender();

        this.userName = userName;
        this.friend = friend;

        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);
        sendPanel = new JPanel();
        textPanel = new JPanel(new GridLayout(100, 1,0,10));
        textPanel.setBackground(Color.WHITE);
        textScrollPane = new JScrollPane(textPanel);
        
        textField = new JTextField(15);
        textField.setFont(new Font("Noto Color Emoji", Font.PLAIN, 16));//设置成既能显示emoji又能显示中文的字体

        sendButton = new JButton("发送");
        sendButton.addMouseListener(this);
        imageSendButton = new JButton("发送图片");
        imageSendButton.addMouseListener(this);
        fileSendButton = new JButton("发送文件");
        fileSendButton.addMouseListener(this);
        emojiButton = new JButton("Emoji");
        emojiButton.addMouseListener(this);
        emojiPane = new Emoji(textField);

        sendPanel.add(textField);
        sendPanel.add(sendButton);
        sendPanel.add(imageSendButton);
        sendPanel.add(fileSendButton);
        sendPanel.add(emojiButton);
        this.add(textScrollPane, "Center");
        this.add(sendPanel, "South");
        if (friend.equals("toAll")){
            this.setTitle(userName + " 的群聊");
        }else {
             this.setTitle(userName+" 和 "+friend+"聊天");
        }

        this.imageInsert = new ImageInsert(textPanel);
        imageInsert.setMaxWidth(100);
        imageInsert.setMaxHeight(100);

        this.addWindowListener(this);
        this.setIconImage((new ImageIcon(headPath)).getImage());
        this.setSize(600, 400);
        this.setVisible(true);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == sendButton) {
            // 发送消息
            if (friend.equals("toAll")){
                //调用MessSender的群聊方法
                messSender.sendToAll(userName, textField.getText());
            }else{
                //调用私聊方法
                System.out.println("friend: " + friend);
                messSender.sendToSingle(userName, friend, textField.getText());
            }
            textField.setText("");//清空输入框
        }else if (e.getSource() == imageSendButton){//选择图片

            fileSender.setFileType("Image");//在里面绘制图片
            fileSend();
           
        }else if (e.getSource() == fileSendButton){//发送文件
            fileSender.setFileType("File");
            fileSend();
        }else if (e.getSource() == emojiButton){
            emojiPane.setVisible(true);
        }
    }

    public void fileSend(){
        //文件选择器
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

        int num = fileChooser.showOpenDialog(null);//确认返回值
        if (num == JFileChooser.APPROVE_OPTION){
            File file = fileChooser.getSelectedFile();//获取文件
            fileName = file.getName();//文件名
            String filePath = file.getAbsolutePath();//得到绝对路径

            //直接发送文件
            if (friend.equals("toAll")){//群发
                fileSender.sendToAll(userName, filePath, fileName, this);
            }else {
                fileSender.sendToSingle(userName, friend, filePath, fileName, this);
            }
                textField.setText("");
                fileName = null;
            }
    }

    public ImageInsert getImageInsert() {
        return imageInsert;
    }

    public void setImageInsert(ImageInsert imageInsert) {
        this.imageInsert = imageInsert;
    }

    @Override
    public void windowActivated(WindowEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void windowClosed(WindowEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void windowClosing(WindowEvent e) {
        ImageIcon image = new ImageIcon("Client_Part\\res\\image\\qq.gif");
        int result = JOptionPane.showConfirmDialog(this, "确认结束该聊天?","提示",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,image);
        
        Toolkit.getDefaultToolkit().beep();

        if (result == JOptionPane.YES_OPTION){
            userServe.exit(userName, friend);
            this.setVisible(false);//关闭后隐藏
        }
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void windowIconified(WindowEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void windowOpened(WindowEvent e) {
        // TODO Auto-generated method stub
        
    }

    public JPanel getTextPanel() {
        return textPanel;
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    public String getHeadPath() {
        return headPath;
    }

    public void setHeadPath(String headPath) {
        this.headPath = headPath;
    }

}
