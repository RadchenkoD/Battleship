package trash;

import javax.swing.*;
import java.awt.*;

public class MyPanel extends JPanel {
    public MyPanel() {
        super(new GridBagLayout());
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        //gbc.anchor = GridBagConstraints.NORTH;
        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.insets = new Insets(5,5,15,5);
        gbc.gridwidth = 2;

        JLabel gameName = new JLabel("Морской бой");
        gameName.setHorizontalAlignment(JLabel.CENTER);
        gameName.setFont(new Font("Serif", Font.BOLD, 72));

        add(gameName, gbc);
    }
}
