package view.states.main;

import view.MyButton;
import view.MyFrame;
import trash.MyPanel;

import javax.swing.*;
import java.awt.*;

public class MainPanel extends JPanel {
    public MainPanel(MyFrame parentFrame) {
        super(new GridBagLayout());
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        //gbc.anchor = GridBagConstraints.NORTH;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = 1;
        gbc.gridx = 0;
        gbc.insets = new Insets(5,5,5,5);
        gbc.gridwidth = 2;

        add(new MyButton("Игра с другим игроком", MyButton.gameVsPlayerButton, this, parentFrame), gbc);
        gbc.gridy++;
        add(new MyButton("Игра с компьютером", MyButton.gameVsBotButton, this, parentFrame), gbc);
        gbc.gridy++;
        //add(new MyButton("Правила игры", MyButton.helpButton, this, parentFrame), gbc);
        //gbc.gridy++;
        add(new MyButton("Выход", MyButton.exitButton, this, parentFrame), gbc);
    }
}
