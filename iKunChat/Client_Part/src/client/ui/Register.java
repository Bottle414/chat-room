package Client_Part.src.client.ui;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;

import Client_Part.src.client.tools.UserServe;

/*
    用户注册界面
*/

public class Register extends JFrame implements MouseListener{
    JLabel newNameLabel, newCodeLabel, checkLabel;
    JPanel registerPanel;
    JTextField newNameField, newCodeField, checkField;
    JButton confirmButton;

    UserServe userServe;
    Login login;

    public Register(Login login) {

        userServe = new UserServe();
        this.login = login;

        registerPanel = new JPanel(new GridLayout(3, 2, 4, 20));
        newNameLabel = new JLabel("新用户名");
        newCodeLabel = new JLabel("新密码");
        checkLabel = new JLabel("确认新密码");
        newNameField = new JTextField(15);
        newCodeField = new JTextField(15);
        checkField = new JTextField(15);
        confirmButton = new JButton("注册");
        confirmButton.addMouseListener(this);
        registerPanel.add(newNameLabel);
        registerPanel.add(newNameField);
        registerPanel.add(newCodeLabel);
        registerPanel.add(newCodeField);
        registerPanel.add(checkLabel);
        registerPanel.add(checkField);
        this.add(registerPanel, "Center");
        this.add(confirmButton, "South");
        this.setSize(350, 240);
        this.setDefaultCloseOperation(3);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == confirmButton){

            String userId = newNameField.getText();
            String code = newCodeField.getText();
            String check = checkField.getText();

            System.out.println(userId+" "+code+" "+check);

            if (userId.equals("")){
                JOptionPane.showMessageDialog(null, "请输入用户名");
                Toolkit.getDefaultToolkit().beep();
            }else if (code.equals("")){
                JOptionPane.showMessageDialog(null, "请输入密码");
                Toolkit.getDefaultToolkit().beep();
            }else if (check.equals("")){
                JOptionPane.showMessageDialog(null, "请再次确认密码");
                Toolkit.getDefaultToolkit().beep();
            }else if (!check.equals(code)){
                JOptionPane.showMessageDialog(null, "密码不匹配");
                Toolkit.getDefaultToolkit().beep();
            }else {
                if (userServe.register(userId, code)){
                    //注册成功
                    JOptionPane.showMessageDialog(this, "注册成功");
                    new Login();
                    this.setVisible(false);
                }else {
                    JOptionPane.showMessageDialog(this, "用户已存在");
                    Toolkit.getDefaultToolkit().beep();
                }
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub
        
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
