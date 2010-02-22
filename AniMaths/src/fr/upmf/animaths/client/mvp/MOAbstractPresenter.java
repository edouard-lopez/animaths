package fr.upmf.animaths.client.mvp;

import java.util.HashMap;
import java.util.Map;

import net.customware.gwt.presenter.client.place.Place;
import net.customware.gwt.presenter.client.place.PlaceRequest;
import net.customware.gwt.presenter.client.widget.WidgetDisplay;
import net.customware.gwt.presenter.client.widget.WidgetPresenter;

import com.google.gwt.dom.client.Element;

import fr.upmf.animaths.client.mvp.MathML.MMLMath;
import fr.upmf.animaths.client.mvp.MathObject.MOElement;

public abstract class MOAbstractPresenter<D extends MOAbstractPresenter.Display> extends WidgetPresenter<D>  {

	public Map<Element,MOElement<?>> map = new HashMap<Element,MOElement<?>>();
	public void putDOMElement(Element domElement, MOElement<?> mathObjectElement) {
		map.put(domElement, mathObjectElement);
	}
	
	public interface Display extends WidgetDisplay {
		public MMLMath asWrapper();
		public void clear();
	}

	protected MOElement<?> element;
	protected String id;

	public MOAbstractPresenter(String id, D display) {
		super(display, AniMathsPresenter.eventBus);
		this.id = id;
		display.asWrapper().getElement().setId(id);
	}

	public MOAbstractPresenter(String id,MOElement<?> element,D display) {
		this(id, display);
		init(element);
	}

	public MOElement<?> getElement() {
		return element;
	}
	
	public void init(MOElement<?> element) {
		unbind();
		element.pack(display.asWrapper(),this);
		this.element = element;
		bind();
	}
	
	@Override
	protected void onUnbind() {
		display.clear();
	}

	public static final Place PLACE = new Place("s");
	@Override
	public Place getPlace() {
		return PLACE;
	}

	@Override
	protected void onPlaceRequest(final PlaceRequest request) {
	}

	public void refreshDisplay() {
		// This is called when the presenter should pull the latest data
		// from the server, etc. In this case, there is nothing to do.
	}

	public void revealDisplay() {
		// Nothing to do. This is more useful in UI which may be buried
		// in a tab bar, tree, etc.
	}

}