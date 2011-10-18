package at.ait.dme.yumaJS.client.annotation.widgets;

import at.ait.dme.yumaJS.client.annotation.Annotation;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineHTML;

public class AnnotationWidget extends Composite {
	
	private FlowPanel panel;
	
	public AnnotationWidget(Annotation a) {
		panel = new FlowPanel();
		panel.setStyleName("yuma-annotation");
		panel.add(new InlineHTML(a.getText()));
		
		initWidget(panel);
	}

}
