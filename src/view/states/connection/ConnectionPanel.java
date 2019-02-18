package view.states.connection;

import view.MyButton;
import view.MyFrame;
import trash.MyPanel;

import javax.swing.*;
import java.awt.*;

public class ConnectionPanel extends JPanel {
    private JTextField gameName;
    private JTextField password;

    public ConnectionPanel(MyFrame parentFrame) {
        super(new GridBagLayout());
        super.setVisible(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = 1;
        gbc.gridx = 0;
        gbc.insets = new Insets(5,5,5,5);
        gbc.gridwidth = 2;

        JLabel gameNameLabel = new JLabel("Введите название игры:", JLabel.CENTER);
        gameNameLabel.setFont(MyFrame.textFont);
        JLabel passwordLabel = new JLabel("Введите пароль (если есть):", JLabel.CENTER);
        passwordLabel.setFont(MyFrame.textFont);

        gameName = new JTextField();
        gameName.setFont(MyFrame.textFont);
        password = new JTextField();
        password.setFont(MyFrame.textFont);

        add(gameNameLabel, gbc);
        gbc.gridy++;
        add(gameName, gbc);
        gbc.gridy++;
        add(passwordLabel, gbc);
        gbc.gridy++;
        add(password, gbc);
        gbc.gridy++;

        MyButton.changeDefaultTextSize(48);
        add(new MyButton("Продолжить", MyButton.sendRequestToConnectionButton, this, parentFrame), gbc);
        gbc.gridy++;
        add(new MyButton("Назад", MyButton.backButton, this, parentFrame), gbc);
    }

    public String getGameName() {
        return gameName.getText();
    }

    public String getPassword() {
        return password.getText();
    }

    public void restart() {
        gameName.setText("");
        password.setText("");
    }

    @Override
    public void setVisible(boolean b) {
        super.setVisible(b);
        if (!b)
            restart();
    }
}
