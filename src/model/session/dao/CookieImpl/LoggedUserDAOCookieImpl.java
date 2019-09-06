package model.session.dao.CookieImpl;

import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.*;
import javax.crypto.spec.*;
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
        LoggedUser loggedUser = new LoggedUser();
        loggedUser.setMail(email);
        loggedUser.setUsername(username);
        Cookie cookie;
        cookie = new Cookie("loggedUser", encode(loggedUser));
        cookie.setPath("/");
        if(rememberMe){
            cookie.setMaxAge(days*24*60*60); //una settimana di vita
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
    
    private String encode(LoggedUser loggedUser) {
        String encodedLoggedUser = loggedUser.getMail() + "#" + loggedUser.getUsername();
        /*try {
            byte[] keyBytes = "123456789abcdefg".getBytes();
            byte[] ivBytes = "123456789abcdefg".getBytes();
            SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
            byte[] plainText = encodedLoggedUser.getBytes();
            byte[] cipherText = cipher.doFinal(plainText);
            encodedLoggedUser = DatatypeConverter.printHexBinary(cipherText);
        } catch (Exception ex) {ex.printStackTrace(); }*/
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