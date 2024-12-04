package Client_Part.src.client.tools;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Client_Part.src.client.ui.Chat;
import common.Message;
import common.MessageType;
import common.MyObjectOutputStream;

/*
    发送文件、图片消息
*/

public class FileSender {
    private Chat chat;
    String fileType;//是文件还是图片

    //群发文件
    public void sendToAll(String sender,String dest,String fileName,Chat chat){

        this.chat = chat;

        if (fileName == null){
            return;
        }

        //先绘制提示消息
        paintNewMess(sender, dest);

        Message msg = new Message();

        if (fileType.equals("Image")){
            msg.setType(MessageType.imageAll);
        }else {
            msg.setType(MessageType.fileMessAll);
        }
        
        msg.setSender(sender);
        msg.setFileName(fileName);
        System.out.println("发送的文件名： " + fileName);
        msg.setTime(new Date().toString());

        FileInputStream fis = null;//源文件路径
        byte[] buff = new byte[(int)new File(dest).length()];

        try {
            fis = new FileInputStream(dest);
            fis.read(buff);//将原文件转换成字节流

            msg.setFileBytes(buff);

            //发送给服务器
            ConnectThread thread = ThreadManner.getThread(sender,"toAll");
            MyObjectOutputStream oos = new MyObjectOutputStream(thread.getSocket().getOutputStream());
            oos.writeObject(msg);

            //关闭文件流
            fis.close();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendToSingle(String sender,String getter,String dest,String fileName,Chat chat){
        this.chat = chat;

        if (fileName == null){
            return;
        }

        //先绘制提示消息
        paintNewMess(sender, dest);

        Message msg = new Message();
        if (fileType.equals("Image")){
            msg.setType(MessageType.imageOne);
        }else {
            msg.setType(MessageType.fileMessOne);
        }
        msg.setSender(sender);
        msg.setGetter(getter);
        msg.setFileName(fileName);
        msg.setTime(new Date().toString());

        FileInputStream fis = null;//源文件路径
        byte[] buff = new byte[(int)new File(dest).length()];

        try {
            fis = new FileInputStream(dest);
            fis.read(buff);//将原文件转换成字节流读入

            msg.setFileBytes(buff);//塞入消息

            //发送给服务器
            ConnectThread thread = ThreadManner.getThread(sender, msg.getGetter());
            MyObjectOutputStream oos = new MyObjectOutputStream(thread.getSocket().getOutputStream());
            System.out.println(sender + " 发送了文件给 " + getter);
            oos.writeObject(msg);

            //关闭文件流
            fis.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void paintNewMess(String sender,String dest){
        JPanel textPanel = chat.getTextPanel();

        //绘制消息提示栏
        JLabel fileLabel = new JLabel(new Date().toString()+"(我)\n  "+sender+"  发送了文件 ");
        fileLabel.setBorder(BorderFactory.createLineBorder(Color.RED,1,true));
        textPanel.add(fileLabel);

        if (fileType.equals("Image")){
            //绘制图像
            System.out.println("filesender绘制了图像");
            try {
                new ImageInsert(textPanel).insertImage(dest);//在此处绘制图片
                System.out.println("绘制");// 在此处，又进去了一遍，此时文件路径为空
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            textPanel.repaint();
            textPanel.updateUI();
        }
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    
}
