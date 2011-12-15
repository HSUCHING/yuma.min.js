package at.ait.dme.yumaJS.client.auth;

import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.Exportable;

import at.ait.dme.yumaJS.client.YUMA;

import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.DOM;

@Export
@ExportPackage("YUMA")
public class AuthWidget implements Exportable {
	
	private Element el;
	
	public AuthWidget(String id) {
		el = DOM.getElementById(id);
		if (el == null)
			YUMA.fatalError("Error: no element with id '" + id + "' found on this page");
		
		el.setInnerHTML("You are not logged in. <a href=\"login\">Log In</a>");
	}

}
