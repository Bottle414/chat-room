package Client_Part.src.client.tools;

import java.util.HashMap;

/*
    线程池
*/

public class ThreadManner {
    //线程和它的状态
    private static HashMap<String,ConnectThread> stateMap = new HashMap<>();

    //用户和它所有的聊天对象
    private static HashMap<String,HashMap<String,ConnectThread>> chatMap = new HashMap<>();

    public static void addThread(String userId,String state,ConnectThread thread){
        stateMap.put(state, thread);
        chatMap.put(userId, stateMap);
    }

    public static HashMap<String, ConnectThread> getStateMap() {
        return stateMap;
    }

    public static ConnectThread getThread(String userId,String state){
        return chatMap.get(userId).get(state);
    }

    public static void removeThread(String userId){
        chatMap.remove(userId);
    }

    public static HashMap<String, HashMap<String, ConnectThread>> getChatMap() {
        return chatMap;
    }
    
}
