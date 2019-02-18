package network.client;

import view.MyFrame;

import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;

public class Client implements MyClient {
    private Socket socket;
    //private BufferedReader in;
    //private BufferedWriter out;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    //private String name;
    private MyFrame frame;
    private String hostName;

    public Client() {
        try {
            socket = null;
            in = null;
            out = null;

            BufferedReader fileReader = new BufferedReader(new FileReader("resources\\HostIP.txt"));
            hostName = fileReader.readLine();
            fileReader.close();

            frame = new MyFrame("Морской бой",this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendRequest(Object request) {
        try {
            if (socket != null) {
                out.writeObject(request);
                out.flush();
            }
        } catch (IOException e) {
            System.out.println("Не удалось отправить запрос");
        }
    }

    public Object receiveResponse() {
        Object response = null;
        try {
            response = in.readObject();
        } /*catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }*/ catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    public void connectToServer() {
        new Thread(new Runnable() {
            public void run() {
                try {
                    socket = new Socket(hostName, 12999);

                    in = new ObjectInputStream(socket.getInputStream());
                    out = new ObjectOutputStream(socket.getOutputStream());

                    while (true) {
                        Object response = in.readObject();
                        frame.processResponse(response);
                    }
                }
                catch (ConnectException e) {
                    //todo при возникновении ошибки подключения сказать фрейму, чтобы он сообщил клиенту
                    System.out.println("Ошибка подключения к серверу, попробуйте подключится позже.");
                }
                catch (SocketException e) { //когда сокет закрыт in/out бросают это исключение
                    //System.out.println(e.getMessage());
                }
                catch (IOException e) {
                    System.out.println("Ошибка ввода/вывода");
                    //e.printStackTrace();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        Thread thread = Thread.currentThread();
        try {
            thread.join(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void leaveServer() {
        try {
            if (!socket.isClosed())
                socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Client client = new Client();
    }
}
