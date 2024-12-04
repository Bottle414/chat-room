package Client_Part.src.client.tools;

import java.awt.Color;
import java.awt.Image;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ImageInsert {
    JPanel textPanel;
    int maxWidth = 50,maxHeight = 50;

    public ImageInsert(JPanel textPanel){
        this.textPanel = textPanel;
    }

    public void insertImage(String imagePath) throws IOException {
        
        ImageIcon imageIcon = new ImageIcon(imagePath);
        
        // 缩放图片
        Image img = resizeImage(imageIcon.getImage(),maxWidth,maxHeight);
        img = img.getScaledInstance(maxWidth, maxHeight, Image.SCALE_SMOOTH);
        imageIcon = new ImageIcon(img);

        //加入文本域
        JLabel imageLabel = new JLabel(imageIcon);
        imageLabel.setBorder(BorderFactory.createLineBorder(Color.RED,1,true));
        imageLabel.setVisible(true);
        textPanel.add(imageLabel);
        textPanel.repaint();
        textPanel.updateUI();
    }

    private Image resizeImage(Image originalImage, int maxWidth, int maxHeight) {
        // 获取原始图片的宽高
        int originalWidth = originalImage.getWidth(null);
        int originalHeight = originalImage.getHeight(null);

        // 计算宽高比
        double widthRatio = (double) maxWidth / originalWidth;
        double heightRatio = (double) maxHeight / originalHeight;

        // 选择更小的比例，确保图片不会超出指定的最大宽高
        double ratio = Math.min(widthRatio, heightRatio);

        // 计算新的宽高
        int newWidth = (int) (originalWidth * ratio);
        int newHeight = (int) (originalHeight * ratio);

        System.out.println("压缩完毕");

        // 返回缩放后的图片
        return originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
    }

    public int getMaxWidth() {
        return maxWidth;
    }

    public void setMaxWidth(int maxWidth) {
        this.maxWidth = maxWidth;
    }

    public int getMaxHeight() {
        return maxHeight;
    }

    public void setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
    }

    
}
