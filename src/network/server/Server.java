package network.server;

import model.Battleship;
import model.Bot;
import model.Player;
import network.Request;
import network.Response;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

public class Server {
    private ServerSocket serverSocket;
    private int nameCount;
    //private ArrayList<ClientThread> clientThreads;
    private ClientThread quickGameSlot;
    //массив ожидающих (создающих игру)
    private ArrayList<ClientThread> createGameClients;

    public Server() {
        try {
            serverSocket = new ServerSocket(12999);
            //clientThreads = new ArrayList<>();
            quickGameSlot = null;
            createGameClients = new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
        }
        nameCount = 1;
    }

    public void run() {
        while (true) {
            try {
                Socket client = serverSocket.accept();
                //String clientName = "Player" + nameCount++;
                ClientThread thread = new ClientThread(client);
                //clientThreads.add(thread);
                thread.start();
                //System.out.println(clientName + " has joined the chat.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class ClientThread extends Thread {
        //TODO пароль
        private Socket client;
        private String name;
        //private BufferedWriter out;
        private ObjectOutputStream out;
        private ObjectInputStream in;
        private Player player;

        private String gameName;
        private String password;
        private boolean busy;

        public ClientThread(Socket client) {
            this.client = client;
            this.name = name;
            player = null;

            gameName = null;
            password = null;
            busy = false;

            try {
                out = new ObjectOutputStream(client.getOutputStream());
                in = new ObjectInputStream(client.getInputStream());
                //out = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public String getClientName() {
            return name;
        }

        public void run() {
            try {
                /*out.write(name);
                out.newLine();
                out.flush();*/
                /*out.writeUTF(name);
                out.flush();*/

                Object request = in.readObject();
                //if (request instanceof String)
                if (request instanceof Request.PlayVsBot) {
                    startGameVsBot();
                } else if (request instanceof Request.PlayVsRandomPlayer) {
                    startGameVsRandomPlayer();
                } else if (request instanceof Request.PlayVsPlayer) {
                    startGameVsPlayer((Request.PlayVsPlayer)request);
                }
            }
            catch (SocketException e) {
                System.out.println("Клиент покинул сервер");
            }
            catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        private void sendResponse(Object response) throws IOException{
            sendResponse(out, response);
        }

        private void sendResponse(ObjectOutputStream out, Object response) throws IOException{
            out.writeObject(response);
            out.flush();
        }

        private void startGameVsBot() throws IOException, ClassNotFoundException {
            Object request = in.readObject();
            //Player player;
            if (request instanceof Request.StartGame) {
                player = ((Request.StartGame) request).player;
                if (!player.isReady()) {
                    sendResponse(new Response.IncorrectPlayer());
                    //корректный выход
                    if (!client.isClosed())
                        client.close();
                    return;
                }
            }
            else if (request instanceof Request.QuitGame) {
                return;
            }
            else {
                //неверный запрос
                sendResponse(new Response.IncorrectRequest("Неверный запрос"));
                return;
            }

            Bot bot = new Bot();
            bot.setShipsRandomly();
            Battleship game = new Battleship();
            game.setPlayers(player, bot);
            sendResponse(new Response.StartGame(true, Response.StartGame.bot));

            while(true) {

                boolean turn = game.getTurn();
                int res;
                Response.GameEvent event = null;

                int x;
                int y;
                if (turn) {
                    request = in.readObject();
                    if (!(request instanceof Request.DoShot)) {
                        if (request instanceof Request.QuitGame) {
                            break;
                        }
                        sendResponse(new Response.IncorrectRequest("Неверный запрос"));
                        break;
                        //throw new RuntimeException("Неверный запрос");
                        //todo обработать неизвестный запрос
                    }
                    x = ((Request.DoShot) request).x;
                    y = ((Request.DoShot) request).y;

                    res = game.doShotAtBot(x, y);
                }
                else {
                    res = game.doShotAtPlayer();
                    x = game.getX();
                    y = game.getY();
                }

                //System.out.println("x = " + x + "\ty = " + y);
                switch (res) {
                    case Player.usedCell:
                        //?
                        break;
                    case Player.missed:
                        event = new Response.GameEvent(game.getTurn(), x, y, false);
                        break;
                    case Player.hit:
                        event = new Response.GameEvent(game.getTurn(), x, y, true);
                        break;
                    case Player.gameOver:
                    case Player.destroyed:
                        ArrayList<Integer> coordinates = game.getDestroyedCoordinates();
                        event = new Response.GameEvent(game.getTurn(), coordinates);
                        break;
                }

                sendResponse(event);

                try {
                    this.join(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (res == Player.gameOver) {
                    sendResponse(new Response.GameOver(turn));
                    break;
                }
            }

            if (!client.isClosed())
                client.close();
        }

        private void startGameVsRandomPlayer() throws IOException, ClassNotFoundException {
            ClientThread clientThread1 = null;

            /*String name1;
            Player player1;
            ObjectOutputStream out1;
            ObjectInputStream in1;
            Socket client1;*/
            Object request = in.readObject();
            //Player player;
            if (request instanceof Request.StartGame) {
                player = ((Request.StartGame) request).player;
                if (!player.isReady()) {
                    sendResponse(new Response.IncorrectPlayer());
                    //корректный выход
                    if (!client.isClosed())
                        client.close();
                    return;
                }
            }
            else if (request instanceof Request.QuitGame) {
                return;
            }
            else {
                //неверный запрос
                sendResponse(new Response.IncorrectRequest("Неверный запрос"));
                return;
            }
            synchronized (Server.this) {
                if (quickGameSlot == null) {
                    quickGameSlot = this;
                    //todo возможно продолжить слушать запрос на выход
                    sendResponse(new Response.WaitOpponent());
                    return;
                }
                else {
                    /*name1 = quickGameSlot.name;
                    player1 = quickGameSlot.player;
                    out1 = quickGameSlot.out;
                    in1 = quickGameSlot.in;
                    client1 = quickGameSlot.client;*/
                    clientThread1 = quickGameSlot;
                    try {
                        /*out1.writeObject(new Response.Ping());
                        out1.flush();*/
                        sendResponse(clientThread1.out, new Response.Ping());
                    }
                    catch (SocketException e) {
                        quickGameSlot = this;
                        sendResponse(new Response.WaitOpponent());
                        return;
                    }
                    quickGameSlot = null;
                }
            }

            /*try {
                this.join(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
            //Начало игры
            gameVsPlayer(clientThread1, this, Response.StartGame.randomPlayer);
        }

        private void startGameVsPlayer(Request.PlayVsPlayer request) throws IOException, ClassNotFoundException {
            gameName = request.gameName;
            password = request.password;
            ClientThread clientForConnection = null;

            if (request.isWantToCreate()) {
                boolean alreadyExist = false;
                synchronized (Server.this) {
                    for (int i = createGameClients.size() - 1; i >= 0; i--) {
                        try {
                            sendResponse(createGameClients.get(i).out, new Response.Ping());
                        } catch (SocketException e) {
                            createGameClients.remove(i);
                            continue;
                        }
                        if (gameName.equals(createGameClients.get(i).gameName)) {
                            sendResponse(new Response.CreateResponse(Response.CreateResponse.nameAlreadyExist));
                            alreadyExist = true;
                        }
                    }
                }
                if (!alreadyExist) {
                    sendResponse(new Response.CreateResponse(null));
                    synchronized (Server.this) {
                        createGameClients.add(this);
                    }

                    //todo перенести в поток 2-го игрока

                }
                //return;
            }
            else {
                synchronized (Server.this) {
                    for (int i = createGameClients.size() - 1; i >= 0; i--) {
                        try {
                            sendResponse(createGameClients.get(i).out, new Response.Ping());
                        } catch (SocketException e) {
                            createGameClients.remove(i);
                            continue;
                        }
                        if (gameName.equals(createGameClients.get(i).gameName)) {
                            clientForConnection = createGameClients.get(i);
                            createGameClients.remove(i);
                        }
                    }
                }
                if (clientForConnection != null) {
                    if (password.equals(clientForConnection.password) || clientForConnection.password.isEmpty()) {
                        sendResponse(new Response.ConnectToGameResponse(null));
                        sendResponse(clientForConnection.out, new Response.Continue());
                    }
                    else {
                        sendResponse(new Response.ConnectToGameResponse(Response.ConnectToGameResponse.incorrectPassword));
                        return;
                    }
                } else {
                    sendResponse(new Response.ConnectToGameResponse(Response.ConnectToGameResponse.notExist));
                    return;
                }

                //todo обернуть приём 1-го игрока и его ожидание в thread

                PlayerRequestThread thread = new PlayerRequestThread(clientForConnection);
                thread.start();

                Object playerRequest = in.readObject();
                if (playerRequest instanceof Request.StartGame) {
                    player = ((Request.StartGame) playerRequest).player;
                    if (!player.isReady()) {
                        sendResponse(new Response.IncorrectPlayer());
                        //корректный выход
                    }
                }
                else if (playerRequest instanceof Request.QuitGame) {
                }
                else {
                    //неверный запрос
                    sendResponse(new Response.IncorrectRequest("Неверный запрос"));
                }

                sendResponse(new Response.WaitOpponent());

                try {
                    thread.join();
                    clientForConnection = thread.getClientForConnection();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (clientForConnection.player == null) {
                    sendResponse(out, new Response.OpponentQuitGame(Response.OpponentQuitGame.setShipsStage));
                    if (!client.isClosed())
                        client.close();
                    return;
                }

                if (player == null && !client.isClosed()) {
                    client.close();
                    sendResponse(clientForConnection.out, new Response.OpponentQuitGame(Response.OpponentQuitGame.setShipsStage));
                    if (!clientForConnection.client.isClosed())
                        clientForConnection.client.close();
                    return;
                }

                gameVsPlayer(clientForConnection, this, Response.StartGame.player);
            }
        }

        private class PlayerRequestThread extends Thread{
            private ClientThread clientForConnection;

            public PlayerRequestThread(ClientThread clientForConnection) {
                this.clientForConnection = clientForConnection;
            }

            public ClientThread getClientForConnection() {
                return clientForConnection;
            }

            public void run() {
                try {
                    //Object playerRequest = null;
                    Object playerRequest = clientForConnection.in.readObject();

                    if (playerRequest instanceof Request.StartGame) {
                        clientForConnection.player = ((Request.StartGame) playerRequest).player;
                        if (!clientForConnection.player.isReady()) {
                            sendResponse(clientForConnection.out, new Response.IncorrectPlayer());
                            //корректный выход
                        }
                    } else if (playerRequest instanceof Request.QuitGame) {
                    } else {
                        //неверный запрос
                        sendResponse(clientForConnection.out, new Response.IncorrectRequest("Неверный запрос"));
                    }
                    if (clientForConnection.player == null && !clientForConnection.client.isClosed()) {
                        clientForConnection.client.close();
                        return;
                    }

                    sendResponse(clientForConnection.out, new Response.WaitOpponent());
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

        public void gameVsPlayer(ClientThread client1, ClientThread client2, String PlayerVs) throws IOException, ClassNotFoundException {
            String name1 = client1.name;
            Player player1 = client1.player;
            ObjectOutputStream out1 = client1.out;
            ObjectInputStream in1 = client1.in;
            Socket socket1 = client1.client;

            String name2 = client2.name;
            Player player2 = client2.player;
            ObjectOutputStream out2 = client2.out;
            ObjectInputStream in2 = client2.in;
            Socket socket2 = client2.client;

            Battleship game = new Battleship();
            game.setPlayers(player1, player2);
            sendResponse(out1, new Response.StartGame(true, PlayerVs));
            sendResponse(out2, new Response.StartGame(false, PlayerVs));

            while(true) {
                boolean turn = game.getTurn();
                int res;
                Response.GameEvent event = null;

                int x;
                int y;
                Object request;
                if (turn) {
                    try {
                        request = in1.readObject();
                    } catch (SocketException e) {
                        sendResponse(out2, new Response.OpponentQuitGame(Response.OpponentQuitGame.playGameStage));
                        break;
                    }
                    if (!(request instanceof Request.DoShot)) {
                        //throw new RuntimeException("Неверный запрос");
                        //todo обработать неизвестный запрос
                        if (request instanceof Request.QuitGame) {
                            sendResponse(out2, new Response.OpponentQuitGame(Response.OpponentQuitGame.playGameStage));
                            break;
                        }
                        sendResponse(out1, new Response.IncorrectRequest("Неверный запрос"));
                        sendResponse(out2, new Response.OpponentQuitGame(Response.OpponentQuitGame.playGameStage));
                        break;
                    }
                } else {
                    //request = in.readObject();
                    try {
                        request = in2.readObject();
                    } catch (SocketException e) {
                        sendResponse(out1, new Response.OpponentQuitGame(Response.OpponentQuitGame.playGameStage));
                        break;
                    }
                    if (!(request instanceof Request.DoShot)) {
                        //throw new RuntimeException("Неверный запрос");
                        if (request instanceof Request.QuitGame) {
                            sendResponse(out1, new Response.OpponentQuitGame(Response.OpponentQuitGame.playGameStage));
                            break;
                        }
                        sendResponse(out2, new Response.IncorrectRequest("Неверный запрос"));
                        sendResponse(out1, new Response.OpponentQuitGame(Response.OpponentQuitGame.playGameStage));
                        break;
                        //todo обработать неизвестный запрос
                    }
                }
                x = ((Request.DoShot) request).x;
                y = ((Request.DoShot) request).y;
                res = game.doShotPvsP(x, y);

                switch (res) {
                    case Player.missed:
                        event = new Response.GameEvent(game.getTurn(), x, y, false);
                        break;
                    case Player.hit:
                        event = new Response.GameEvent(game.getTurn(), x, y, true);
                        break;
                    case Player.gameOver:
                    case Player.destroyed:
                        ArrayList<Integer> coordinates = game.getDestroyedCoordinates();
                        event = new Response.GameEvent(game.getTurn(), coordinates);
                        break;
                }

                try {
                    sendResponse(out1, event);
                } catch (SocketException e) {
                }
                if (event != null) //todo написать адекватно
                    event.changeTurn();
                try {
                    sendResponse(out, event);
                } catch (SocketException e) {
                }

                if (res == Player.gameOver) {
                    try {
                        sendResponse(out1, new Response.GameOver(turn));
                    } catch (SocketException e) {
                    }
                    try {
                        sendResponse(out2, new Response.GameOver(!turn));
                    } catch (SocketException e) {
                    }
                    break;
                }
            }

            if (!socket1.isClosed())
                socket1.close();
            if (!socket2.isClosed())
                socket2.close();
        }
    }

    /*private class Slot {
        private String name;
        private Player player;
        private Socket socket;

        public Slot(String name, Player player, Socket socket) {
            this.name = name;
            this.player = player;
            this.socket = socket;
        }

        public String getName() {
            return name;
        }

        public Player getPlayer() {
            return player;
        }

        public Socket getSocket() {
            return socket;
        }
    }*/

    public static void main(String[] args) {
        Server server = new Server();
        server.run();
    }
}
