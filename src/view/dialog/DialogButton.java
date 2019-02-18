package view.dialog;

import view.MyFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class DialogButton extends JButton {
    private final int thisDialogButton;
    private final JDialog parentDialog;
    private final MyFrame mainFrame;

    final static int ok = 1;
    final static int cancel = 2;
    final static int restart = 3;
    final static int exit = 4;

    public DialogButton(String text, int thisDialogButton, JDialog parentDialog, MyFrame mainFrame) {
        super(text);
        this.thisDialogButton = thisDialogButton;
        setFont(new Font("Dialog", Font.PLAIN, 20));
        this.parentDialog = parentDialog;
        addMouseListener(new DialogButtonMouseListener());
        this.mainFrame = mainFrame;
    }

    class DialogButtonMouseListener implements MouseListener {
        private boolean entered;

        public DialogButtonMouseListener() {
            super();
            entered = false;
        }

        public void mouseClicked(MouseEvent e) {}
        public void mouseEntered(MouseEvent e) {
            entered = true;
        }
        public void mouseExited(MouseEvent e) {
            entered = false;
        }
        public void mousePressed(MouseEvent e) {}
        public void mouseReleased(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1 && entered) {
                switch (thisDialogButton) {
                    case ok:
                        parentDialog.dispose();
                        break;
                    case cancel:
                        parentDialog.dispose();
                        break;
                    case restart:
                        break;
                    case exit:
                        parentDialog.dispose();
                        break;
                }
            }
        }
    }
}
