import java.io.FileInputStream;
import java.io.IOException;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import com.formdev.flatlaf.IntelliJTheme;

import Client_Part.src.client.ui.Login;

public class ClientTest {
    public static void main(String[] args) {

        //IntelliJTheme.setup( ClientTest.class.getResourceAsStream("Client_Part\\src\\GitHub.theme.json" ) );
            
        try {
            UIManager.setLookAndFeel(IntelliJTheme.createLaf(new IntelliJTheme(new FileInputStream("Client_Part\\src\\GitHub.theme.json"))));
        } catch (UnsupportedLookAndFeelException | IOException e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            new Login();
        });

    }
}
