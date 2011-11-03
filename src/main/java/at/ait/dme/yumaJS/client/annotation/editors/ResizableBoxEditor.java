package at.ait.dme.yumaJS.client.annotation.editors;

import com.google.gwt.user.client.ui.AbsolutePanel;

import at.ait.dme.yumaJS.client.annotation.Annotatable;
import at.ait.dme.yumaJS.client.annotation.Annotation;
import at.ait.dme.yumaJS.client.annotation.editors.selection.BoundingBox;
import at.ait.dme.yumaJS.client.annotation.editors.selection.Range;
import at.ait.dme.yumaJS.client.annotation.editors.selection.ResizableBoxSelection;
import at.ait.dme.yumaJS.client.annotation.editors.selection.Selection;
import at.ait.dme.yumaJS.client.annotation.editors.selection.SelectionChangedHandler;
import at.ait.dme.yumaJS.client.annotation.widgets.CommentField;

/**
 * An {@link Editor} implementation for images and zoomable images,
 * using {@link ResizableBoxSelection}.
 *  
 * @author Rainer Simon <rainer.simon@ait.ac.at>
 */
public class ResizableBoxEditor extends Editor {
	
	private AbsolutePanel panel;

	public ResizableBoxEditor(Annotatable annotatable, AbsolutePanel panel) {
		this(annotatable, panel, null);
	}
	
	public ResizableBoxEditor(Annotatable annotatable, AbsolutePanel panel, Annotation initialValue) {		
		super(annotatable, initialValue);
		this.panel = panel;
		
		BoundingBox bbox = null;
		if (initialValue != null)
			bbox = annotatable.toBoundingBox(initialValue.getFragment()); 
				
		Selection selection = new ResizableBoxSelection(panel, annotatable.getLabels(), bbox);
		selection.setSelectionChangedHandler(new SelectionChangedHandler() {
			public void onBoundsChanged(BoundingBox bbox) {
				updateEditForm();
			}

			public void onRangeChanged(Range range) {
				// This editor does not have ranges
			}
		});
		setSelection(selection);
		
		CommentField commentField;
		if (initialValue == null) {
			commentField = new CommentField(annotatable.getLabels(), true);
		} else {
			commentField = new CommentField(initialValue.getText(), annotatable.getLabels(), true);
		}
		setCommentField(commentField);
		panel.add(commentField, 0, 0);
		updateEditForm();
	}
	
	private void updateEditForm() {
		BoundingBox bbox = selection.getSelectedBounds();
		panel.setWidgetPosition(commentField, bbox.getX(), bbox.getY() + bbox.getHeight() + 2);
	}
	
}
