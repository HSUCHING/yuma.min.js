package at.ait.dme.yumaJS.client.auth;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.PushButton;

public class AuthDialog extends Composite {
	
	private FlowPanel container = new FlowPanel();
	
	public AuthDialog() {
		PushButton btnMyOpenID = new PushButton("My OpenID");
		container.add(btnMyOpenID);
		
		PushButton btnGoogle = new PushButton("Google");
		container.add(btnGoogle);
		
		PushButton btnYahoo = new PushButton("Yahoo!");
		container.add(btnYahoo);
		
		initWidget(container);
	}

}
