package at.ait.dme.yumaJS.client.annotation.widgets;

import at.ait.dme.yumaJS.client.annotation.Annotation;
import at.ait.dme.yumaJS.client.annotation.editors.selection.Selection;
import at.ait.dme.yumaJS.client.init.Labels;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.TextArea;

/**
 * The overlay element implementing the annotation editing form.
 * 
 * @author Rainer Simon <rainer.simon@ait.ac.at>
 */
public class EditForm extends Composite {

	private FlowPanel container;
	
	private TextArea textArea;
	
	private FlowPanel buttonContainer;
	
	private PushButton btnSave;
	
	private PushButton btnCancel;
	
	public EditForm(Selection selection, Labels labels, Annotation initialValue) {
		// Note: selection is for future use - e.g. when properties
		// of the selection (start/end time) should be displayed in 
		// the edit form
		
		textArea = new TextArea();
		
		if (initialValue != null)
			textArea.setText(initialValue.getText());
		
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
		    public void execute () {
		        textArea.setFocus(true);
		    }
		});
		
		if (labels == null) {
			btnSave = new PushButton("SAVE");
			btnCancel = new PushButton("CANCEL");
		} else {
			btnSave = new PushButton(labels.save());
			btnCancel = new PushButton(labels.cancel());			
		}

		btnSave.setStyleName("button");
		btnSave.addStyleName("button-save");

		btnCancel.setStyleName("button");
		btnCancel.addStyleName("button-cancel");
		
		buttonContainer = new FlowPanel();
		buttonContainer.setStyleName("yuma-editform-buttons");
		buttonContainer.add(btnSave);
		buttonContainer.add(btnCancel);
		
		container = new FlowPanel();
		container.setStyleName("yuma-editform");
		container.add(textArea);
		container.add(buttonContainer);
		initWidget(container);
	}
	
	public String getText() {
		return textArea.getText();
	}
	
	public HandlerRegistration addSaveClickHandler(ClickHandler handler) {
		return btnSave.addClickHandler(handler);
	}
	
	public HandlerRegistration addCancelClickHandler(ClickHandler handler) {
		return btnCancel.addClickHandler(handler);
	}

}
