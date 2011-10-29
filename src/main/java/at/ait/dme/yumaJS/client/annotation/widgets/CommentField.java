package at.ait.dme.yumaJS.client.annotation.widgets;

import at.ait.dme.yumaJS.client.init.Labels;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.TextArea;

public class CommentField extends Composite {
		
	private FlowPanel container = new FlowPanel();
	
	private TextArea textArea = new TextArea();
	
	private FlowPanel buttonContainer = new FlowPanel();
	
	private boolean hasFocus = false;
	
	public CommentField(Labels labels) {
		this(null, labels);
	}

	public CommentField(String text, Labels labels) {
		container.setStyleName("yuma-comment");
		textArea.setStyleName("yuma-comment-textarea");
		
		if (text == null) {
			textArea.getElement().setAttribute("placeholder", "Add a Comment...");
		} else {
			textArea.setText(text);
		}
		
		textArea.addFocusHandler(new FocusHandler() {
			public void onFocus(FocusEvent event) {
				hasFocus = true;
			}
		});
		
		textArea.addBlurHandler(new BlurHandler() {
			public void onBlur(BlurEvent event) {
				hasFocus = false;
			}
		});
		
		PushButton btnSave, btnCancel;
		if (labels == null) {
			btnSave = new PushButton("SAVE");
			btnCancel = new PushButton("CANCEL");
		} else {
			btnSave = new PushButton(labels.save());
			btnCancel = new PushButton(labels.cancel());			
		}
		
		btnSave.setStyleName("yuma-button");
		btnSave.addStyleName("yuma-button-save");
		
		btnCancel.setStyleName("yuma-button");
		btnCancel.addStyleName("yuma-button-cancel");

		buttonContainer.setStyleName("yuma-comment-buttons");
		buttonContainer.add(btnSave);
		buttonContainer.add(btnCancel);

		container.add(textArea);
		container.add(buttonContainer);
		initWidget(container);
	}

	
	public boolean hasFocus() {
		return hasFocus;
	}
	
	public void setText(String text) {
		textArea.setText(text);
	}
	
	public void setFocus(boolean focused) {
		textArea.setFocus(focused);
	}
	
	public void showButtons() {
		
	}
	
	public void hideButtons() {
		
	}
	
}
