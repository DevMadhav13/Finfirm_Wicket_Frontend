package com.finfirm;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.WebPage;

public class HomePage extends WebPage {
	private static final long serialVersionUID = 1L;

	@Override
	protected void onConfigure(){
		setVisible(false);
		System.out.println("visibility set to false");
		if(Mysession.isAuthenticated()) {
			setVisible(true);
			System.out.println("visibility set to true as user is authenticated");
		}else{
			System.out.println("user is not autharized");
		}
		super.onConfigure();
	}

}
