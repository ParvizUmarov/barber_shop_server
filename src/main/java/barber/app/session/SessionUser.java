package barber.app.session;

public class SessionUser {
    private final int id;
    private final String mail;
    private final String token;

    public SessionUser(int id, String mail, String token) {
        this.mail = mail;
        this.id = id;
        this.token = token;
    }

    public String getMail() {
        return mail;
    }

    public int getId() {
        return id;
    }

    public String getToken() {
        return token;
    }
}
