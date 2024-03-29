package com.finfirm;

import org.apache.wicket.Session;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.IRequestCycle;
import org.apache.wicket.request.handler.TextRequestHandler;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class LoginPage extends WebPage {
    private static final long serialVersionUID = 1L;

    private String username;
    private String password;
    private Label successMessage;

    public LoginPage(final PageParameters parameters) {
        super(parameters);
        Form<Void> loginForm = new Form<Void>("loginForm") {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onSubmit() {
                super.onSubmit();
                try {
                    makeAPICall(username,password);
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
                if(Mysession.isAuthenticated()) {
                    redirectToHelloPage();
                }else{
                    System.out.println("user is not autharized");
                }

            }
        };

        TextField<String> usernameField = new TextField<String>("username", new PropertyModel<String>(this, "username"));
        TextField<String> passwordField = new PasswordTextField("password", new PropertyModel<String>(this, "password"));
        loginForm.add(usernameField);
        loginForm.add(passwordField);
        add(loginForm);
    }


    private void makeAPICall(String username,String password) throws IOException, InterruptedException {
        String requestBody = "{\"username\": \"" + username + "\", \"password\": \"" + password + "\"}";
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:9091/auth/login"))
                .header("Content-Type", "application/json")  // Set the content type if sending JSON
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if(response.statusCode()==200){
            Mysession.setAuthenticated(true);
        }else{
            Mysession.setAuthenticated(false);
        }
        // Process the API response
        System.out.println(response.statusCode());
        System.out.println("API Response: " + response.body());
        String jwtToken = response.body();

        Session session = Session.get();
        session.setAttribute("JwtTokan", jwtToken);

    }


    private void redirectToHelloPage() {
        final int delayMillis = 1500;

        getRequestCycle().scheduleRequestHandlerAfterCurrent(new TextRequestHandler("Redirecting to HelloPage...") {
            @Override
            public void respond(org.apache.wicket.request.IRequestCycle requestCycle) {
                // Wait for the delay
                try {
                    Thread.sleep(delayMillis);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                setResponsePage(HomePage.class);
            }
        });
    }}
