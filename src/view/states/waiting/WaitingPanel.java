package view.states.waiting;

import view.MyButton;
import view.MyFrame;

import javax.swing.*;
import java.awt.*;

public class WaitingPanel extends JPanel {
    private JLabel label;

    public WaitingPanel(MyFrame parentFrame) {
        super(new BorderLayout());
        setVisible(false);

        label = new JLabel("", JLabel.CENTER);
        Font font = MyFrame.textFont.deriveFont((float) (MyFrame.textSize + 8));
        label.setFont(font);

        MyButton.changeDefaultTextSize(24);
        MyButton exitGameButton = new MyButton("Отмена", MyButton.quitGameButton, this, parentFrame);
        JPanel panel = new JPanel(new GridBagLayout());

        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = 1;
        gbc.gridx = 0;
        gbc.insets = new Insets(5,5,5,5);

        panel.add(exitGameButton, gbc);

        add(label, BorderLayout.CENTER);
        add(panel, BorderLayout.SOUTH);
    }

    public void setText(String text) {
        label.setText(text);
    }
}
