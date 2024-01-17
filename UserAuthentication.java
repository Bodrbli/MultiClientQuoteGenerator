import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class UserAuthentication { // класс авторизации клиента
    UserAccount account;
    Socket socket;
    String name;
    String password;
    private BufferedReader in;
    private PrintWriter out;
    private static ArrayList<UserAccount> userAccounts = new ArrayList<>(); // список аккаунтов
    {
        userAccounts.add(new UserAccount("Oleg", "23")); // статическая инициализация
    }

    public UserAuthentication(Socket socket) throws IOException { //конструктор
        this.socket = socket;
        out = new PrintWriter(socket.getOutputStream());
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        go();
    }

    public void go() { // метод авторизации пользователя
        try {
            while (true) {
                out.println("Введите имя: ");
                out.flush();
                name = in.readLine();
                out.println("Введите пароль: ");
                out.flush();
                password = in.readLine();
                account = new UserAccount(name, password);
                if (userAccounts.contains(account)) {
                    break; // если аккаунт подтвержден выходим из цикла
                } else {
                    System.out.println("Неверное имя пользователя или пароль!");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
