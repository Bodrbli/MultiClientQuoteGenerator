import java.util.Objects;

public class UserAccount { // класс аккаунта клиента
    private String name;
    private String password;

    public UserAccount (String name, String password) { // конструктор
        this.name = name;
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserAccount that)) return false;
        return Objects.equals(name, that.name) && Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, password);
    }
}
