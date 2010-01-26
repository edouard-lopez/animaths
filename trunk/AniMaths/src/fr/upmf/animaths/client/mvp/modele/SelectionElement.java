package fr.upmf.animaths.client.mvp.modele;

import net.customware.gwt.presenter.client.EventBus;

import com.google.gwt.event.shared.HandlerRegistration;

import fr.upmf.animaths.client.mvp.events.SelectionChangeEvent;
import fr.upmf.animaths.client.mvp.events.SelectionChangeHandler;
import fr.upmf.animaths.client.mvp.events.SelectionEvent;
import fr.upmf.animaths.client.mvp.events.SelectionHandler;
import fr.upmf.animaths.client.mvp.modele.MathObject.MathObjectElement;

public class SelectionElement implements SelectionHandler, SelectionChangeHandler {

	private EventBus eventBus = null;
	private MathObjectElement element = null;
	private HandlerRegistration hrSelection;
	private HandlerRegistration hrSelectionChange;
	private boolean enabled = false;

	public SelectionElement(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	public MathObjectElement getElement() {
		return element;
	}

	public void setElement(MathObjectElement element) {
		if(this.element==element)
			return;
		if(this.element!=null)
			this.element.setState(MathObjectElement.STATE_NONE);
		this.element = element;
		if(this.element!=null)
			this.element.setState(MathObjectElement.STATE_SELECTED);
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		if(enabled==false) {
			if(this.element!=null)
				element.setState(MathObjectElement.STATE_SELECTABLE);
			element = null;
			hrSelection.removeHandler();
			hrSelectionChange.removeHandler();
		}
		else {
			hrSelection = eventBus.addHandler(SelectionEvent.getType(), this);
			hrSelectionChange = eventBus.addHandler(SelectionChangeEvent.getType(), this);
		}
		this.enabled = enabled;
	}

	@Override
	public void onSelect(SelectionEvent event) {
		SelectableElement selectable = event.getSelectableElement();
		switch(event.getState()) {
		case MathObjectElement.STATE_SELECTABLE:
			MathObjectElement element = selectable.getElement();
			selectable.setEnabled(false);
			setElement(element);
			break;
		case MathObjectElement.STATE_SELECTED:
			selectable.setElement(this.element);
			setEnabled(false);
			selectable.setEnabled(true);
			break;
		}
	}

	@Override
	public void onSelectionChange(SelectionChangeEvent event) {
		System.out.println(event.getDirection());
		switch(event.getDirection()) {
		case SelectionChangeEvent.CHANGE_TO_PARENT:
			setElement(element.getMathObjectParent());
			break;
		case SelectionChangeEvent.CHANGE_TO_FIRST_CHILD:
			setElement(element.getMathObjectFirstChild());
			break;
		case SelectionChangeEvent.CHANGE_TO_PREVIOUS_SIBLING:
			setElement(element.getMathObjectParent().getMathObjectPreviousChild(element));
			break;
		case SelectionChangeEvent.CHANGE_TO_NEXT_SIBLING:
			setElement(element.getMathObjectParent().getMathObjectNextChild(element));
			break;
		}
	}
}