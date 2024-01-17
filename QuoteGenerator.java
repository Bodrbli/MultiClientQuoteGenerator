import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class QuoteGenerator extends Thread {
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
    private int quoteCount; // количество отправленных цитат
    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;
    // цитаты
    private String[] quoteList = {"Ничто так не выдает человека, как то, над чем он смеётся.", "Иногда момент, который ты так долго ждал, приходит в самое неподходящее время...",
            "Каждый живет, как хочет, и расплачивается за это сам.", "Если вы хотите иметь то, что никогда не имели, вам придётся делать то, что никогда не делали.",
            "Самой большой ошибкой, которую вы можете совершить в своей жизни, является постоянная боязнь ошибаться.", "Проще расстаться с человеком, чем с иллюзиями на его счёт.",
            "Думайте о прошлом, только если воспоминания приятны вам.", "Никто не ценит того, чего слишком много.", "Всегда очень тягостно новыми глазами увидеть то, с чем успел так или иначе сжиться.",
            "Улыбайся, не доставляй беде удовольствия.", "Я обо всем подумаю потом, когда найду в себе силы это выдержать.", "Кто вопросов не задает, тот лжи не слышит.",
            "Лучше быть оптимистом и ошибаться, чем оставаться вечно правым пессимистом.", "Если соблюдаешь мелкие правила, можно нарушать большие.",
            "Надо бы так устроить жизнь, чтобы каждое мгновение в ней было значительно.", "Гордые люди сами выкармливают свои злые печали.",
            "Победить этот мир можно только неслыханной наглостью.", "Чтобы дойти до цели, человеку нужно только одно – идти."};

    public QuoteGenerator(Socket socket) throws IOException { // конструктор
        this.quoteCount = 0;
        this.socket = socket;
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        start();
    }

    @Override
    public void run() { // нить читает любые запросы от клиента пока количество выданных цитат меньше 3
        String quote;
        String word;
        try {
            while (quoteCount < 3) {
                word = in.readLine();
                if (word.equals("stop")) {
                    break;
                } else {
                    quote = getQuote();
                    send(quote);
                    quoteCount++;
                    System.out.println(socket.getInetAddress() + " - отправлено - " + quote);
                }
            }
            System.out.println(dtf.format(LocalDateTime.now()) + " " + socket.getInetAddress() + " отключился");
        }   catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getQuote() { // метод получения цитаты
        int random = (int) (Math.random() * quoteList.length);
        return quoteList[random];
    }
    private void send(String msg) { // метод отправки цитаты клиенту
        try {
            out.write(msg + "\n");
            out.flush();
        } catch (IOException ignored) {}
    }
}
