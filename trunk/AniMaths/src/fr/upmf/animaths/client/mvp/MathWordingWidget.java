package fr.upmf.animaths.client.mvp;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;

import fr.upmf.animaths.client.mvp.MathObject.MathObjectElementPresenter;
import fr.upmf.animaths.client.mvp.widgets.MathML.MathMLMath;

/**
 * The wording of an exercise...
 * 
 * @author Maxime Lefran�ois
 *
 */
public class MathWordingWidget extends Composite {

	FlowPanel panel;
	
	public MathWordingWidget() {
		panel = new FlowPanel();
		this.initWidget(panel);
	}
	
	public void pack(Object... args) {
		
		for(Object arg : args) {
			if(arg instanceof String)
				panel.getElement().appendChild((new Label((String) arg).getElement()).getChildNodes().getItem(0));
			else if(arg instanceof MathObjectElementPresenter<?>) {
				MathMLMath wrapper = new MathMLMath(false);
				((MathObjectElementPresenter<?>) arg).clone().pack(wrapper,null);
				panel.add(wrapper);
			}
			else
				throw new IllegalArgumentException("Probl�me d'arguments, "+
						"seuls les chaines de caract�res et les objets math�matiques sont accept�s.");
		}
	}

}
