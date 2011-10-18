package at.ait.dme.yumaJS.client.annotation.widgets;

import java.util.Date;

import at.ait.dme.yumaJS.client.annotation.Annotation;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineHTML;

public class AnnotationWidget extends Composite {
	
	private FlowPanel panel;
	
	public AnnotationWidget(Annotation a) {
		panel = new FlowPanel();
		panel.setStyleName("yuma-annotation");
		
		InlineHTML username = new InlineHTML();
		if (a.getUserRealName() == null) {
			username.setHTML(a.getUsername());
		} else {
			username.setHTML(a.getUserRealName());
		}
		username.setStyleName("yuma-annotation-username");

		
		InlineHTML timestamp = new InlineHTML();
		timestamp.setHTML(new Date((long) a.getModified()).toString());
		timestamp.setStyleName("yuma-annotation-modified");
		
		panel.add(username);
		panel.add(new InlineHTML(a.getText() + "<br/>"));
		panel.add(timestamp);
		
		initWidget(panel);
	}

}
