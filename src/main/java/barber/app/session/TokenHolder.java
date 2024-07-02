package barber.app.session;

import java.util.UUID;

public class TokenHolder {
    private String token = "";

    public TokenHolder(){}

    public void create(){
        token = UUID.randomUUID().toString();
    }

    public String getToken() {
        return token;
    }
}
