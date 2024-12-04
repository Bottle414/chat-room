package Client_Part.src.client.tools;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AudioPlayer {

    public static final int SUCCESS = 1;
    public static final int FAIL = 0;
    public static final int KIK = 2;

    public static void onlineSound(){
        //播放上线音乐
        try {
            // 加载音频文件
            File soundFile = new File("Client_Part\\res\\audio\\phonegan.wav");
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);

            // 获取音频格式
            AudioFormat format = audioStream.getFormat();
            DataLine.Info info = new DataLine.Info(Clip.class, format);

            // 获取音频播放器（Clip）
            Clip clip = (Clip) AudioSystem.getLine(info);
            clip.open(audioStream);

            // 播放声音
            clip.start();
            System.out.println("播放了上线音乐");

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public static void messSound(){
        //播放接收消息提示音，也可以直接使用系统自带的声音 Toolkit.getDefaultToolkit().beep();
        try {
            // 加载音频文件
            File soundFile = new File("Client_Part\\res\\audio\\jiji.wav");
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);

            // 获取音频格式
            AudioFormat format = audioStream.getFormat();
            DataLine.Info info = new DataLine.Info(Clip.class, format);

            // 获取音频播放器（Clip）
            Clip clip = (Clip) AudioSystem.getLine(info);
            clip.open(audioStream);

            // 播放声音
            clip.start();
            System.out.println("播放了消息提示音");

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public static void alertSound(int type){
        //播放弹窗提示音，也可以直接使用系统自带的声音 Toolkit.getDefaultToolkit().beep();
        try {
            // 加载音频文件
            File soundFile = null;
            if (type == SUCCESS){//成功的提示音
                soundFile = new File("Client_Part\\res\\audio\\gan.wav");
            }else if (type == FAIL) {
                soundFile = new File("Client_Part\\res\\audio\\ganf.wav");
            }else {
                soundFile = new File("Client_Part\\res\\audio\\aiyo.wav");
            }
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);

            // 获取音频格式
            AudioFormat format = audioStream.getFormat();
            DataLine.Info info = new DataLine.Info(Clip.class, format);

            // 获取音频播放器（Clip）
            Clip clip = (Clip) AudioSystem.getLine(info);
            clip.open(audioStream);

            // 播放声音
            clip.start();
            System.out.println("播放了弹窗提示音");

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

}
