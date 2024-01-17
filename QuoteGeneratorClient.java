import java.io.*;
import java.net.Socket;

public class QuoteGeneratorClient {
    private Socket s;
    public void go() { // подключаемся к серверу
        try {
             s = new Socket("localhost", 8080);

            BufferedReader reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
            System.out.println(reader.readLine());
            new WriteMsg(s); // нить отправляющая сообщения серверу
            while (true) { // читаем цитаты
                String quote = reader.readLine();
                System.out.println(quote);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public class WriteMsg extends Thread { //нить читает сообщения от сервера
        private Socket socket;
        BufferedReader consoleReader;
        BufferedWriter writer;

        public WriteMsg(Socket socket) throws IOException { // конструктор
            this.socket = socket;
            consoleReader = new BufferedReader(new InputStreamReader(System.in));
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            start();
        }

        @Override
        public void run() { //нить отправляющая сообщения
            String userWord;
            while (true) {
                try {
                     userWord = consoleReader.readLine();
                     if (userWord.equals("stop")) {
                         writer.write("stop" + "\n");
                         break;
                     } else {
                         writer.write(userWord + "\n");
                     }
                    writer.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        QuoteGeneratorClient client = new QuoteGeneratorClient();
        client.go();
    }
}
