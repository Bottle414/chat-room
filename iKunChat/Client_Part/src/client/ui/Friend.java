package Client_Part.src.client.ui;

import javax.swing.*;
import javax.swing.border.LineBorder;
import Client_Part.src.client.tools.AudioPlayer;
import Client_Part.src.client.tools.UserServe;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.HashSet;

/*
    用户好友列表界面，好友信息从服务器获取
*/

public class Friend extends JFrame implements MouseListener,WindowListener {

    String userName;

    JButton onlineButton,toAllButton;//拉取用户按钮，群聊按钮
    JPanel imgPanel,fixPanel,onliPanel,friendPanel;//背景面板,固定按钮面板，在线用户面板
    JScrollPane scrollPane,friendScrollPane;//在线用户滚动列表

    ImageIcon headIcon;//头像
    JLabel headLabel;
    String headPath;

    UserServe userServe;//服务后台
    HashSet<String> friends;
    int theme = 1;//默认白天模式

    public Friend(String userName,UserServe userServe) {// 传入自己的id

        this.userServe = userServe;
        userServe.startThread(userName, this);
        this.userName = userName;

        initFrame();

    }

    //初始化聊天界面
    private void initFrame(){

        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.setLocationRelativeTo(null);

        imgPanel = new JPanel(new GridLayout(3, 1));//最大面板,塞固定板和滚动板
        
        onliPanel = new JPanel(new GridLayout(20, 1, 4 ,4));//塞Jlabel
        onliPanel.setBackground(Color.WHITE);
        friendPanel = new JPanel(new GridLayout(10,1,4,4));
        friendPanel.setBackground(Color.WHITE);
        friendScrollPane = new JScrollPane(friendPanel);

        //在此处初始化了好友盘还有头像
        userServe.getFriends(userName);

        onlineButton = new JButton("拉取在线用户");
        onlineButton.addMouseListener(this);
        toAllButton = new JButton("进入群聊");
        toAllButton.addMouseListener(this);
        fixPanel = new JPanel();

        scrollPane = new JScrollPane(onliPanel);//把滚动板塞进在线用户
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        JLabel test = new JLabel(userName);
        test.addMouseListener(this);
        onliPanel.add(test);

        imgPanel.add(fixPanel);
        imgPanel.add(scrollPane);
        imgPanel.add(friendScrollPane);
        imgPanel.setBackground(Color.WHITE);

        this.add(imgPanel);

        this.addWindowListener(this);

        this.setSize(200, 300);
        this.setTitle(userName);
        this.setResizable(false);
        this.setVisible(true);
    }

    class Head extends JFrame implements MouseListener{//头像选择栏
        JPanel headPanel;
    
        public Head(){
            this.setLocationRelativeTo(this);
            this.setDefaultCloseOperation(HIDE_ON_CLOSE);
    
            headPanel = new JPanel(new GridLayout(2, 5, 5, 5));
            
            for (int i = 1;i <= 10;i++){
                JLabel head = new JLabel(new ImageIcon("Client_Part\\res\\image\\head" + i + ".jpg")); //显示头像
                head.addMouseListener(this);
                headPanel.add(head);
            }

            this.add(headPanel);
            this.setSize(250, 150);
            this.setVisible(true);
        }
    
        @Override
        public void mouseClicked(MouseEvent e) {
            JLabel label = (JLabel)e.getSource();
            headLabel.setIcon(label.getIcon());
            /*
                直接去服务器修改头像路径
                计算头像路径,默认鼠标位置相对于子组件:
                int row = (int)e.getX()/40,col = (int)e.getY()/40;
                System.out.println("x: " + row + "y: " + col);
            */

            Point p = label.getLocation();//相对于父组件的位置

            int row = (int)p.getX()/40,col = (int)p.getY()/40;
            int index = row  + col*5;//计算图像编号

            userServe.changeHead(userName,(index + 1) + "");//发送头像下标
            System.out.println("改后头像下标: " + headPath);
            headPath = "Client_Part\\res\\image\\head" + index + ".jpg";
            this.setVisible(false);
        }
    
        @Override
        public void mouseEntered(MouseEvent e) {
            ((JLabel)e.getSource()).setBorder(new LineBorder(Color.RED, 1));
        }
    
        @Override
        public void mouseExited(MouseEvent e) {
            ((JLabel)e.getSource()).setBorder(null);
        }
    
        @Override
        public void mousePressed(MouseEvent e) {
            // TODO Auto-generated method stub
            
        }
    
        @Override
        public void mouseReleased(MouseEvent e) {
            // TODO Auto-generated method stub
            
        }    
    }


    //更新在线好友

    @Override
    public void mouseClicked(MouseEvent e) {
        //为避免单击事件和双击事件发生冲突，设置双击判定
        //算了，改成右键，太麻烦了
        if (e.getButton() == MouseEvent.BUTTON1){//单击鼠标左键
            if (e.getSource() == onlineButton){
                userServe.getOnlineUser(userName);//获取在线用户
            }else if (e.getSource() == toAllButton){
                //群聊
                new Chat("toAll",userName,headPath);
                System.out.println("创建chat群聊时的path: " + headPath);
            }
            else if (e.getSource() == headLabel){//修改头像
                System.out.println("修改头像");
                new Head();
            }else if (e.getSource().getClass().equals(JLabel.class)){
                //私聊
                String[] friendName = ((JLabel)e.getSource()).getText().split(" \\(");
                new Chat(friendName[0],userName,headPath);
            }
        }else if (e.getButton() == MouseEvent.BUTTON3){//单击鼠标右键
           if (e.getSource() == headLabel){
                System.out.println("敲了敲坤坤");
                JOptionPane.showMessageDialog(null,"是不是有病!","抗议",JOptionPane.INFORMATION_MESSAGE);
                AudioPlayer.alertSound(AudioPlayer.KIK);
            }else if (e.getSource().getClass().equals(JLabel.class)){//放在后面，避免与前一个事件冲突
                //修改好友
                String[] friendName = ((JLabel)e.getSource()).getText().split(" \\(");//避免把好友两个字写进去,匹配括号需要转义.哇怎么还藏了一个空格
                userServe.addFriend(userName,friendName[0]);
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        Object obj = e.getSource();
        if (obj == headLabel){
            ((JLabel)obj).setBorder(new LineBorder(Color.RED, 1));
        }else if (obj.getClass().equals(JLabel.class)) {
            ((JLabel) obj).setForeground(Color.BLUE);
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        Object obj = e.getSource();
        if (obj == headLabel){
            ((JLabel)obj).setBorder(null);
        }else if (obj.getClass().equals(JLabel.class)) {
            ((JLabel) obj).setForeground(Color.BLACK);
        }
    }

    

    @Override
    public void windowActivated(WindowEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void windowClosed(WindowEvent e) {
        //关完了
    }

    @Override
    public void windowClosing(WindowEvent e) {
        //刚点击关闭
        ImageIcon image = new ImageIcon("Client_Part\\res\\image\\qq.gif");
        int result = JOptionPane.showConfirmDialog(this, "确认退出?","提示",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,image);
        Toolkit.getDefaultToolkit().beep();
        if (result == JOptionPane.YES_OPTION){
            userServe.exit(userName, "Online");
            System.exit(0);//关闭程序
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

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    public void getOnliner(String contain) {
        String[] name = contain.split(" ");
        onliPanel.removeAll();
        for (int i = 1;i < name.length;i++){
            System.out.println("name: " + name[i]);
            JLabel label = new JLabel(name[i]);
            label.addMouseListener(this);
            label.setIcon(new ImageIcon());
            label.setBackground(Color.WHITE);
            onliPanel.add(label);
        }
        onliPanel.repaint();
        onliPanel.updateUI();
    }

    public void updateFriend(String contain) {
        String[] name = contain.split("-");
        friendPanel.removeAll();
        System.out.println("修改了好友");
        for (int i = 0;i < name.length;i++){
            if (!name[i].equals("")){
                System.out.println("newName: " + name[i]);
                JLabel label = new JLabel(name[i] + " (好友)");
                label.addMouseListener(this);
                label.setIcon(new ImageIcon());
                label.setBackground(Color.WHITE);
                friendPanel.add(label);
            }
        }
        friendPanel.repaint();
        friendPanel.updateUI();
    }

    //绘制好友栏,同时得到之前的头像
    public void drawFriends(HashSet<String> friends,String path) {
        headPath = path;

        for (String s : friends){
            System.out.println("name: " + s);
            JLabel label = new JLabel(s + " (好友)");
            label.addMouseListener(this);
            label.setIcon(new ImageIcon());
            label.setBackground(Color.WHITE);
            friendPanel.add(label);
        }
        headLabel = new JLabel(new ImageIcon(headPath));
        headLabel.addMouseListener(this);
        fixPanel.add(headLabel);
        fixPanel.add(toAllButton);
        fixPanel.add(onlineButton);
        fixPanel.setBackground(Color.WHITE);
        fixPanel.repaint();
        fixPanel.updateUI();

        /*
            这多线程有毛病啊！！！！！！！！！！！！
            怎么乱插队！！！！！！！！！！！！！！！！！！
            害我改了好久的bug！！！！！！！！！！！！！！！！
            这个headPath！！！！！！！居然在构造函数完成以后才被收到！！！！！！！！！！！！！！！啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊！！！！！！！！！！！！！！！！！！！！
        */
    }

}
