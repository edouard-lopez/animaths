package fr.upmf.animaths.client.mvp.events;

import com.google.gwt.event.shared.EventHandler;

public interface GrabHandler extends EventHandler {

	void onGrab( GrabEvent event );
	
}
