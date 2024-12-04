package Client_Part.src.client.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import Client_Part.src.client.tools.MessSender;

/*
    这是表情盘，有emoji
*/
public class Emoji extends JFrame implements MouseListener{
    JPanel emojiPanel;
    MessSender messSender;
    JScrollPane scrollPane;
    JTextField field;
    String[] emojis = {"😊", "😂", "😍", "😎", "😜", "😅", "😢", "😱", "😘","🥺",
            "😉", "😜", "😏", "😬", "😷", "🤔", "😌", "😝", "😗", "🤩",
            "🥳", "😇", "🤗", "🥰", "😑", "😴", "😳", "😋", "🤪", "😈",
            "😓", "😭", "😫", "😤", "🤯", "😷", "🤧", "😬", "😶", "✌️",
            "💪", "🤩", "😅", "🤯", "🥶", "👻", "💀", "🎃", "🧛‍♂️", "🧟‍♀️",
            "🤡", "😺", "🐱", "🐶", "🦊", "🐰", "🐹", "🦁", "🐼", "🐯"
    };

    public Emoji(JTextField field){
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(HIDE_ON_CLOSE);

        this.field = field;

        //messSender = new MessSender(chat);
        emojiPanel = new JPanel(new GridLayout(15, 5, -2, 15));
        scrollPane = new JScrollPane(emojiPanel);

        for (int i = 0;i < emojis.length;i++){
            JLabel emoji = new JLabel(emojis[i], JLabel.CENTER); //居中显示
            emoji.setFont(new Font("Noto Color Emoji", Font.PLAIN, 16));//设置字体为支持表情符号的字体
            emoji.setForeground(Color.BLACK);
            emoji.addMouseListener(this);
            emojiPanel.add(emoji);
        }
        this.add(scrollPane);
        this.setSize(200, 300);
        this.setVisible(false);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        field.setText(field.getText() + ((JLabel)e.getSource()).getText());
        System.out.println("发送了emoji " + ((JLabel)e.getSource()).getText());
        this.setVisible(false);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        ((JLabel)e.getSource()).setForeground(Color.BLUE);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        ((JLabel)e.getSource()).setForeground(Color.BLACK);
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
