package view.states.game;

import model.Player;
import network.Response;
import view.states.setShips.FieldPanel;
import view.MyButton;
import view.MyFrame;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {
    private MyFrame parentFrame;
    private JPanel panelWithFields;
    private JPanel messagePanel;

    private JLabel turnLabel;
    private JPanel fields;
    private FieldPanel myField;
    private FieldPanel opponentsField;

    private boolean turn;

    public GamePanel(MyFrame parentFrame) {
        super(new GridBagLayout());
        super.setVisible(false);

        this.parentFrame = parentFrame;
        turn = false;

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.insets = new Insets(5, 5, 5, 5);

        panelWithFields = new JPanel(new BorderLayout());

        fields = new JPanel(new GridBagLayout());
        //myField = new FieldPanel(new Player());
        opponentsField = new FieldPanel(new Player(), parentFrame);
        opponentsField.setField(FieldPanel.forPlaying);
        JLabel myFieldLabel = new JLabel("Мой флот", JLabel.CENTER);
        myFieldLabel.setFont(MyFrame.textFont);
        JLabel opponentsFieldLabel = new JLabel("Флот противника", JLabel.CENTER);
        opponentsFieldLabel.setFont(MyFrame.textFont);
        //Font font = MyFrame.textFont.deriveFont((float) (MyFrame.textSize + 8));

        gbc.gridy++;
        fields.add(myFieldLabel, gbc);
        gbc.gridy = 0;
        gbc.gridx++;
        fields.add(opponentsField, gbc);
        gbc.gridy++;
        fields.add(opponentsFieldLabel, gbc);

        turnLabel = new JLabel("", JLabel.CENTER);
        turnLabel.setFont(MyFrame.textFont);

        MyButton.changeDefaultTextSize(MyFrame.textSize / 2);
        MyButton exitButton = new MyButton("Покинуть игру", MyButton.quitGameButton, this, parentFrame);
        //todo добавить на панель с gridBagLayout
        JPanel exitPanel = new JPanel(new GridBagLayout());
        gbc.gridx = 0;
        gbc.gridy = 0;
        exitPanel.add(exitButton, gbc);

        panelWithFields.add(turnLabel, BorderLayout.NORTH);
        panelWithFields.add(fields, BorderLayout.CENTER);
        panelWithFields.add(exitPanel, BorderLayout.SOUTH);

        add(panelWithFields, gbc);
    }

    public void setMyField(Player player) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.insets = new Insets(5, 5, 5, 5);

        myField = new FieldPanel(player);
        myField.setField(FieldPanel.forPlayingWithoutListener);
        fields.add(myField, gbc);
    }

    public void setTurn(boolean turn) {
        opponentsField.setCanDoShot(turn);
        this.turn = turn;
        if (turn)
            turnLabel.setText("Ваш ход");
        else
            turnLabel.setText("Ход противника");
    }

    public boolean getTurn() {
        return turn;
    }

    public void processResponse(Response.GameEvent response) {
        if (turn) {
            opponentsField.processResponse(response);
        }
        else {
            myField.processResponse(response);
        }
        setTurn(response.isTurn());
    }

    public void processResponse(Response.OpponentQuitGame response) {
        turnLabel.setText("Игра окончена: противник покинул игру");
    }

    public void processResponse(Response.GameOver response) {
        turnLabel.setText("Игра окончена: все корабли уничтожены");
    }

    public void restart() {
        fields.remove(myField);
        fields.remove(opponentsField);
        opponentsField = new FieldPanel(new Player(), parentFrame);
        opponentsField.setField(FieldPanel.forPlaying);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = 0;
        gbc.gridx = 1;
        gbc.insets = new Insets(5, 5, 5, 5);
        fields.add(opponentsField, gbc);
    }

    @Override
    public void setVisible(boolean b) {
        super.setVisible(b);
        if (!b)
            restart();
    }
}
