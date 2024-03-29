package com.finfirm;

import org.apache.wicket.Session;
import org.apache.wicket.csp.CSPDirective;
import org.apache.wicket.csp.CSPDirectiveSrcValue;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.Request;

public class WicketApplication extends WebApplication
{

	@Override
	public Class<? extends WebPage> getHomePage()
	{
		return Signup.class;
	}


	@Override
	public void init()
	{
		super.init();

		mountPage("/signup", Signup.class);
		mountPage("/login", LoginPage.class);
		mountPage("/home", HomePage.class);

	}

}
