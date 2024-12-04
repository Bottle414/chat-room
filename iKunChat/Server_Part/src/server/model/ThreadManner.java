package Server_Part.src.server.model;

import java.util.HashMap;
import java.util.Iterator;

/*
    管理所有线程
*/

public class ThreadManner {
    public static HashMap<String,HashMap<String, ClientsThread>> map = new HashMap<>();

    public static void addCientThread(String userId, String state,ClientsThread thread) {
        if (map.get(userId)!=null){
            map.get(userId).put(state,thread);
        }else{//用户不存在，创建对象哈希表
            HashMap<String,ClientsThread> stateMap = new HashMap<>();
            stateMap.put(state, thread);
            map.put(userId, stateMap);
        }
    }

    public static ClientsThread getClientThread(String userId,String state) {
        return map.get(userId).get(state);
    }

    //删除用户的某个聊天对象
    public static void removeThread(String userId,String state){
        map.get(userId).remove(state);
    }

    //删除用户
    public static void removeUser(String userId){
        map.remove(userId);
    }

    //返回那些人的好友
    public static String getOnlineUser(){
        System.out.println("返回在线人员");
        Iterator<String> it = map.keySet().iterator();
        String res = " ";
        while(it.hasNext()){
            res += it.next().toString()+" ";
        }
        return res;
    }

    public static HashMap<String,HashMap<String, ClientsThread>> getMap() {
        return map;
    } 
}
