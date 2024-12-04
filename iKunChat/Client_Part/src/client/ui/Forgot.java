package Client_Part.src.client.ui;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.*;
import Client_Part.src.client.tools.UserServe;

/*
    用户注册界面
*/

public class Forgot extends JFrame implements MouseListener{
    JLabel newNameLabel, newCodeLabel, checkLabel;
    JPanel ForgotPanel;
    JTextField newNameField, newCodeField, checkField;
    JButton confirmButton;

    UserServe userServe;
    Login login;

    public Forgot(Login login) {

        userServe = new UserServe();
        this.login = login;

        ForgotPanel = new JPanel(new GridLayout(3, 2, 4, 20));
        newNameLabel = new JLabel("用户名");
        newCodeLabel = new JLabel("新密码");
        checkLabel = new JLabel("确认新密码");
        newNameField = new JTextField(15);
        newCodeField = new JTextField(15);
        checkField = new JTextField(15);
        confirmButton = new JButton("修改密码");
        confirmButton.addMouseListener(this);
        ForgotPanel.add(newNameLabel);
        ForgotPanel.add(newNameField);
        ForgotPanel.add(newCodeLabel);
        ForgotPanel.add(newCodeField);
        ForgotPanel.add(checkLabel);
        ForgotPanel.add(checkField);
        this.add(ForgotPanel, "Center");
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
                Toolkit.getDefaultToolkit().beep();
                JOptionPane.showMessageDialog(null, "请输入用户名");
            }else if (code.equals("")){
                Toolkit.getDefaultToolkit().beep();
                JOptionPane.showMessageDialog(null, "请输入密码");
            }else if (check.equals("")){
                Toolkit.getDefaultToolkit().beep();
                JOptionPane.showMessageDialog(null, "请再次确认密码");
            }else if (!check.equals(code)){
                Toolkit.getDefaultToolkit().beep();
                JOptionPane.showMessageDialog(null, "密码不匹配");
            }else {
                if (userServe.change(userId,code)){
                    //修改密码
                    JOptionPane.showMessageDialog(this, "修改成功");
                    login.setVisible(true);
                    this.setVisible(false);
                }else {
                    Toolkit.getDefaultToolkit().beep();
                    JOptionPane.showMessageDialog(this, "修改失败");
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
