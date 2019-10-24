package model.mo;

import java.time.LocalDate;

public class Sessione {
    private String sessionId;
    private LocalDate rememberUntil;
    private LocalDate loginDate;

    public Sessione(String sessionId, LocalDate rememberUntil, LocalDate loginDate) {
        this.sessionId = sessionId;
        this.rememberUntil = rememberUntil;
        this.loginDate = loginDate;
    }

    public String getSessionId() { return sessionId; }

    public void setSessionId(String sessionId) { this.sessionId = sessionId; }

    public LocalDate getLoginDate() { return loginDate; }

    public void setLoginDate(LocalDate loginDate) { this.loginDate = loginDate; }

    public LocalDate getRememberUntil() { return rememberUntil; }

    public void setRememberUntil(LocalDate rememberUntil) { this.rememberUntil = rememberUntil; }
}
