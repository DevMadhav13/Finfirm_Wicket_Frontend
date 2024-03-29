package com.finfirm;
import org.apache.wicket.Session;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.request.Request;

public class Mysession extends WebSession {
    private static final long serialVersionUID = 1L;

    public static boolean isAuthenticated = false;

    public Mysession(Request request) {
        super(request);
    }

    public static Mysession get() {
        return (Mysession) Session.get();
    }

    public static boolean isAuthenticated() {
        return isAuthenticated;
    }

    public static void setAuthenticated(boolean authenticated) {
        isAuthenticated = authenticated;
    }
}
