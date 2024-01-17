import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;

public class QuoteGeneratorServer { // класс сервер приниающий запросы от клиентов, создающий нить QuoteGenerator и добавляющий их в список
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss"); //формат времени
    private static final int PORT = 8080; //порт сервера
    private static LinkedList<QuoteGenerator> socketList = new LinkedList<QuoteGenerator>(); // список подключенных клиентов

    public void go() throws IOException { //метод принимающий входящие запросы от клиентов
        ServerSocket serverSocket = new ServerSocket(PORT); // инициализация сервера
        String time; // переменная для отображения времени подключения, отключения клиента
        try {
            while (true) {
                Socket socket = serverSocket.accept(); // захватываем сокет
                new UserAuthentication(socket); // авторизация клиента
                time = dtf.format(LocalDateTime.now());
                System.out.println(time + " " + socket.getInetAddress() + " - подключился.");
                try {
                    socketList.add(new QuoteGenerator(socket));
                } catch (IOException e) {
                    socket.close();
                }
            }
        }   finally {
            serverSocket.close();
        }
    }

    public static void main(String[] args) throws IOException {
        QuoteGeneratorServer generator = new QuoteGeneratorServer();
        generator.go();

    }
}
