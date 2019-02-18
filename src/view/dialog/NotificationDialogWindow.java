package view.dialog;

import view.MyFrame;
import view.dialog.DialogButton;

import javax.swing.*;
import java.awt.*;

public class NotificationDialogWindow extends JDialog {
    private MyFrame mainFrame;

    public static final String win = "Победа";
    public static final String lose = "Поражение";
    public static final String error = "Ошибка";

    public NotificationDialogWindow(Frame owner, String tittle, String message) {
        super(owner, tittle, true);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);

        if (owner instanceof MyFrame)
            mainFrame = (MyFrame) owner;

        Container c = getContentPane();
        c.setLayout(new GridBagLayout());

        Font textFont = new Font("Dialog", Font.PLAIN, 32);
        JLabel text = new JLabel("", JLabel.CENTER);
        if (tittle.equals(lose))
            text.setText("Вы проиграли :(");
        else if (tittle.equals(win))
            text.setText("Вы выиграли :)");
        else if (tittle.equals(error))
            text.setText(message);
        text.setFont(textFont);
        DialogButton okButton = new DialogButton("Оk", DialogButton.ok, this, mainFrame);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(5,5,5,5);

        c.add(text, gbc);
        gbc.gridy++;
        c.add(okButton, gbc);

        pack();
        Point fLoc = mainFrame.getLocation();
        setLocation(fLoc.x + (mainFrame.getWidth() - getWidth()) / 2, fLoc.y + (mainFrame.getHeight() - getHeight()) / 2);
        setVisible(true);
    }
}
