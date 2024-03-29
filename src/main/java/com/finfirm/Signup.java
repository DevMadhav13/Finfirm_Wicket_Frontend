package com.finfirm;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.handler.TextRequestHandler;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


public class Signup extends WebPage {

    private static final long serialVersionUID = 1L;

    private String username;
    private String password;

    public Signup (final PageParameters parameters) {
        super(parameters);

        Form<Void> SignupForm = new Form<Void>("SignupForm") {
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
                    redirectToLoginPage();
                }else{
                    System.out.println("user is not autharized");
                }

                }
        };
        TextField<String> usernameField = new TextField<String>("username", new PropertyModel<String>(this, "username"));
        TextField<String> passwordField = new PasswordTextField("password", new PropertyModel<String>(this, "password"));
        SignupForm.add(usernameField);
        SignupForm.add(passwordField);
        add(SignupForm);

    }

    private void makeAPICall(String username,String password) throws IOException, InterruptedException {
        String requestBody = "{\"username\": \"" + username + "\", \"password\": \"" + password + "\"}";
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:9091/auth/register"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());


        if(response.statusCode()==201){
            Mysession.setAuthenticated(true);
        }else{
            Mysession.setAuthenticated(false);
        }
        // Process the API response
        System.out.println(response.statusCode());
        System.out.println("API Response: " + response.body());
    }


    private void redirectToLoginPage() {
        final int delayMillis = 1500;

        // Schedule a text request handler after the specified delay
        getRequestCycle().scheduleRequestHandlerAfterCurrent(new TextRequestHandler("Redirecting to HelloPage...") {
            @Override
            public void respond(org.apache.wicket.request.IRequestCycle requestCycle) {
                // Wait for the delay
                try {
                    Thread.sleep(delayMillis);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                setResponsePage(LoginPage.class);
            }
        });
    }


}
