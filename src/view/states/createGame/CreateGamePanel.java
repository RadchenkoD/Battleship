package view.states.createGame;

import view.MyButton;
import view.MyFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class CreateGamePanel extends JPanel {
    private JTextField gameName;
    private JPasswordField password;
    private JCheckBox passwordCheckBox;

    public CreateGamePanel(MyFrame parentFrame) {
        super(new GridBagLayout());
        super.setVisible(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = 1;
        gbc.gridx = 0;
        gbc.insets = new Insets(5,5,5,5);

        JLabel gameNameLabel = new JLabel("Название: ");
        gameNameLabel.setFont(MyFrame.textFont);
        passwordCheckBox = new JCheckBox("Пароль: ");
        passwordCheckBox.addItemListener(new PasswordItemListener());
        passwordCheckBox.setFont(MyFrame.textFont);

        gameName = new JTextField();
        gameName.setFont(MyFrame.textFont);
        password = new JPasswordField();
        password.setFont(MyFrame.textFont);
        password.setEditable(false);

        add(gameNameLabel, gbc);
        gbc.gridx++;
        add(gameName, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        add(passwordCheckBox, gbc);
        gbc.gridx++;
        add(password, gbc);
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;

        MyButton.changeDefaultTextSize(48);
        add(new MyButton("Продолжить", MyButton.sendRequestToCreateGameButton, this, parentFrame), gbc);
        gbc.gridy++;
        add(new MyButton("Назад", MyButton.backButton, this, parentFrame), gbc);
    }

    public String getGameName() {
        return gameName.getText();
    }

    public String getPassword() {
        return new String(password.getPassword());
    }

    public void restart() {
        //todo дописать
        gameName.setText("");
        password.setText("");
        password.setEditable(false);
        passwordCheckBox.setSelected(false);
    }

    @Override
    public void setVisible(boolean b) {
        super.setVisible(b);
        if (!b)
            restart();
    }

    private class PasswordItemListener implements ItemListener {

        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                password.setEditable(true);
            }
            else if (e.getStateChange() == ItemEvent.DESELECTED) {
                password.setText("");
                password.setEditable(false);
            }
        }
    }
}
