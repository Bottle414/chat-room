package common;

import java.io.Serializable;
import java.util.HashSet;

/*
    服务器端拥护账号管理
*/

public class User implements Serializable {
    private String username;
    private String password;
    private String request;
    private String headPath;//头像路径
    private HashSet<String> friends;//好友列表
    private String face;//聊天对象、在线状态

    public User() {
    }

    public User(String username, String password,HashSet<String> friends,String headPath) {
        this.username = username;
        this.password = password;
        this.friends = friends;
        this.headPath = headPath;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getFace() {
        return face;
    }

    public void setFace(String face) {
        this.face = face;
    }

    public String getHeadPath() {
        return headPath;
    }

    public void setHeadPath(String headPath) {
        this.headPath = headPath;
    }

    public HashSet<String> getFriends() {
        return friends;
    }

    public void setFriends(HashSet<String> friends) {
        this.friends = friends;
    }

    @Override
    public String toString() {
        return "User [username=" + username + ", password=" + password + ", headPath=" + headPath + ", friends="
                + friends + "]";
    }

}
