package view.states.gameVsPlayer;

import view.MyButton;
import view.MyFrame;
import trash.MyPanel;

import javax.swing.*;
import java.awt.*;

public class GameVsPlayerPanel extends JPanel {
    public GameVsPlayerPanel(MyFrame parentFrame) {
        super(new GridBagLayout());
        setVisible(false);

        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = 1;
        gbc.gridx = 0;
        gbc.insets = new Insets(5,5,5,5);
        gbc.gridwidth = 2;

        add(new MyButton("Быстрая игра", MyButton.quickGameButton, this, parentFrame), gbc);
        gbc.gridy++;
        add(new MyButton("Создать игру", MyButton.createGameButton, this, parentFrame), gbc);
        gbc.gridy++;
        add(new MyButton("Присоединиться", MyButton.connectionButton, this, parentFrame), gbc);
        gbc.gridy++;
        add(new MyButton("Назад", MyButton.backButton, this, parentFrame), gbc);
    }
}
