package common;

import java.io.Serializable;
import java.util.HashSet;

/*
    消息类，包含消息的信息
*/

public class Message implements Serializable {

    boolean valid = true;//是否发送过,默认没有

    private String type;// 消息类型
    private String sender;// 发送者
    private String getter;// 接收者
    private String senderHead;//发送者头像
    private String getterHead;//收信人头像

    private String contain;// 消息内容
    private String time;// 消息发送时间
    private String fileName;//文件名

    private byte[] fileBytes;
    private HashSet<String> friends;

    public Message() {
    }

    public Message(String sender, String type, String contain, String getter) {
        this.sender = sender;
        this.type = type;
        this.contain = contain;
        this.getter = getter;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getGetter() {
        return getter;
    }

    public void setGetter(String getter) {
        this.getter = getter;
    }

    public String getContain() {
        return contain;
    }

    public void setContain(String contain) {
        this.contain = contain;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getFileBytes() {
        return fileBytes;
    }

    public void setFileBytes(byte[] fileBytes) {
        this.fileBytes = fileBytes;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public String getSenderHead() {
        return senderHead;
    }

    public void setSenderHead(String senderHead) {
        this.senderHead = senderHead;
    }

    public String getGetterHead() {
        return getterHead;
    }

    public void setGetterHead(String getterHead) {
        this.getterHead = getterHead;
    }

    public HashSet<String> getFriends() {
        return friends;
    }

    public void setFriends(HashSet<String> friends) {
        this.friends = friends;
    }

    
}
