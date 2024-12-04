package Client_Part.src.client.ui;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.*;
import Client_Part.src.client.tools.UserServe;


/*
    登陆界面
*/

public class Login extends JFrame implements MouseListener {
    JLabel broadLabel;// 标题板
    JPanel loginPanel;// 登录面板
    JTabbedPane loginTablPane;// 登陆方式选项卡，管理三个登陆面板
    JPanel ipPanel, telePanel, mailPanel;// ip登录、电话登录、邮箱登录
    JLabel numLabel, codeLabel, forgotLabel;// 提示输入号码、密码，忘记密码选项
    JTextField numField, codeField;// 号码、密码填写区
    JCheckBox remBox;// 记住密码选框
    JButton loginButton, registerButton;// 登录、注册按钮

    UserServe userServe;

    public Login() {
        userServe = new UserServe();
        initFrame();
        this.setVisible(true);
    }

    private void initFrame() {
        // 初始化窗体
        this.setSize(350, 240);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(3);

        // 标题板
        broadLabel = new JLabel(new ImageIcon("Client_Part\\res\\image\\broad.png"));
        this.add(broadLabel, "North");

        // 登录方式
        // ip
        ipPanel = new JPanel(new GridLayout(3, 3));
        numLabel = new JLabel("iKun编号");
        numLabel.setFont(new Font(null, 1, 14));
        codeLabel = new JLabel("密码");
        codeLabel.setFont(new Font(null, 1, 14));
        forgotLabel = new JLabel("忘记密码");
        forgotLabel.setForeground(Color.BLUE);
        forgotLabel.addMouseListener(this);
        numField = new JTextField();
        codeField = new JTextField();

        //开发者通道
        // numField.setText("1");
        // codeField.setText("1");

        remBox = new JCheckBox("记住密码");
        // tele
        telePanel = new JPanel();
        // mail
        mailPanel = new JPanel();
        ipPanel.add(numLabel);
        ipPanel.add(numField);
        ipPanel.add(codeLabel);
        ipPanel.add(codeField);
        ipPanel.add(remBox);
        ipPanel.add(forgotLabel);

        loginTablPane = new JTabbedPane();
        loginTablPane.add("ikun编号", ipPanel);
        loginTablPane.add("电话号码", telePanel);
        loginTablPane.add("邮箱地址", mailPanel);

        this.add(loginTablPane);

        // 登录或注册
        loginPanel = new JPanel();
        loginButton = new JButton("登录");
        loginButton.addMouseListener(this);
        registerButton = new JButton("注册");
        registerButton.addMouseListener(this);
        loginPanel.add(loginButton);
        loginPanel.add(registerButton);
        this.add(loginPanel, "South");
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == loginButton){
            //确认登陆密码
            if (userServe.check(numField.getText(), codeField.getText())){
                //账号密码正确
                new Friend(numField.getText(),userServe);
                this.setVisible(false);
            }
        }else if (e.getSource() == registerButton){//注册
            new Register(this);
            this.setVisible(false);
        }else if (e.getSource() == forgotLabel){//忘记密码
            new Forgot(this);
            this.setVisible(false);
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if (e.getSource() == forgotLabel){
            ((JLabel)e.getSource()).setForeground(Color.RED);
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if (e.getSource() == forgotLabel){
            ((JLabel)e.getSource()).setForeground(Color.BLUE);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

}