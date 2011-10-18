package at.ait.dme.yumaJS.client.annotation.widgets;

import at.ait.dme.yumaJS.client.YUMA;
import at.ait.dme.yumaJS.client.annotation.Annotatable;
import at.ait.dme.yumaJS.client.annotation.Annotation;
import at.ait.dme.yumaJS.client.init.Labels;
import at.ait.dme.yumaJS.client.io.Delete;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.PushButton;

/**
 * The overlay element used to show the details of an {@link Annotation}.
 * The overlay is composed of a container DIV (CSS class 'annotation-popup')
 * and the following nested elements:
 * <p>
 * <li>
 * <ul>A DIV containing the text content</ul>
 * <ul>A 'REPLY' button</ul> 
 * <ul>An 'EDIT' button</ul>
 * <ul>A 'DELETE' button</ul>
 * </li>
 * 
 * @author Rainer Simon <rainer.simon@ait.ac.at>
 */
public class SimpleDetailsPopup extends DetailsPopup {
	
	private PushButton btnEdit, btnDelete;
	
	public SimpleDetailsPopup(final Annotatable annotatable, final Annotation a, Labels labels) {
		FlowPanel content = new FlowPanel();
		content.setStyleName("annotation-popup-content");
		content.add(new InlineHTML(a.getText()));
		
		if (labels == null) {
			btnEdit = new PushButton("EDIT");
			btnDelete = new PushButton("DELETE");
		} else {
			btnEdit = new PushButton(labels.edit());
			btnDelete = new PushButton(labels.delete());			
		}

		btnEdit.setStyleName("annotation-popup-button");		
		btnEdit.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				annotatable.removeAnnotation(a);
				annotatable.editAnnotation(a);
			}
		});
		
		btnDelete.setStyleName("annotation-popup-button");
		btnDelete.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (annotatable.getServerURL() == null) {
					annotatable.removeAnnotation(a);
				} else {
					Delete.executeJSONP(annotatable.getServerURL(), a.getID(), new AsyncCallback<Void>() {
						public void onSuccess(Void result) {
							annotatable.removeAnnotation(a);
						}			
	
						public void onFailure(Throwable t) {
							YUMA.nonFatalError(t.getMessage());
						}
					});
				}				
			}
		});
		
		showButtons(false);
		
		content.addDomHandler(new MouseOverHandler() {
			public void onMouseOver(MouseOverEvent event) {
				showButtons(true);
			}
		}, MouseOverEvent.getType());
		
		container = new FlowPanel();
		container.setStyleName("annotation-popup");		
		container.add(content);
		container.add(btnEdit);
		container.add(btnDelete);		
		setVisible(false);
		
		initWidget(container);
		
		addDomHandler(new MouseOutHandler() {
			public void onMouseOut(MouseOutEvent event) {
				setVisible(false);
				showButtons(false);
			}
		}, MouseOutEvent.getType());
	}
	
	private void showButtons(boolean show) {
		btnEdit.setVisible(show);
		btnDelete.setVisible(show);
	}

}
