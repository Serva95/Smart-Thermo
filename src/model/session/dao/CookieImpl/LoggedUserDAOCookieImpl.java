package model.session.dao.CookieImpl;

import java.security.SecureRandom;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;
import model.session.mo.LoggedUser;
import model.session.dao.LoggedUserDAO;

public class LoggedUserDAOCookieImpl implements LoggedUserDAO {
    
    HttpServletRequest request;
    HttpServletResponse response;
    
    public LoggedUserDAOCookieImpl(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }
    
    @Override
    public LoggedUser create(String email, String username, boolean rememberMe, int days) {
        String uniqid = identify();
        LoggedUser loggedUser = new LoggedUser();
        if(uniqid == "" || uniqid.length()<128) {
            uniqid = createid();
        }
        loggedUser.setMail(email);
        loggedUser.setUsername(username);
        loggedUser.setUniqid(uniqid);
        Cookie cookie;
        cookie = new Cookie("loggedUser", encode(loggedUser));
        cookie.setPath("/");
        if(rememberMe && days>=0 && days<370){
            cookie.setMaxAge(days*24*3600); //days giorni di vita
        }else{
            cookie.setMaxAge(-1); //mettere -1 per lasciare in vita fino alla chiusura del browser
        }
        response.addCookie(cookie);
        return loggedUser;
    }
    
    @Override
    public void update(LoggedUser loggedUser) {
        Cookie cookie;
        cookie = new Cookie("loggedUser", encode(loggedUser));
        cookie.setPath("/");
        response.addCookie(cookie);
    }
    
    @Override
    public void destroy() {
        Cookie cookie;
        cookie = new Cookie("loggedUser", "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
    
    @Override
    public LoggedUser find() {
        Cookie[] cookies = request.getCookies();
        LoggedUser loggedUser = null;
        if (cookies != null) {
            for (int i = 0; i < cookies.length && loggedUser == null; i++) {
                if (cookies[i].getName().equals("loggedUser")) {
                    loggedUser = decode(cookies[i].getValue());
                }
            }
        }
        return loggedUser;
    }

    @Override
    public String identify(){
        Cookie[] cookies = request.getCookies();
        String uniqid = "";
        if (cookies != null) {
            for(Cookie cookie : cookies){
                if (cookie.getName().equals("uniqid")) {
                    uniqid = cookie.getValue();
                }
            }
        }
        return uniqid;
    }

    private String createid(){
        SecureRandom sr = new SecureRandom();
        byte[] salt = new byte[64];
        sr.nextBytes(salt);
        Cookie cookie;
        cookie = new Cookie("uniqid", DatatypeConverter.printHexBinary(salt));
        cookie.setPath("/");
        cookie.setMaxAge(86400*365*15); //15 anni di vita
        response.addCookie(cookie);
        return DatatypeConverter.printHexBinary(salt);
    }
    
    private String encode(LoggedUser loggedUser) {
        String encodedLoggedUser = loggedUser.getMail() + "#" + loggedUser.getUsername();
        return encodedLoggedUser;
    }
    
    private LoggedUser decode(String encodedLoggedUser) {
        LoggedUser loggedUser = new LoggedUser();
        String[] values = encodedLoggedUser.split("#");
        loggedUser.setMail(values[0]);
        loggedUser.setUsername(values[1]);
        return loggedUser;
    }
}