package fr.upmf.animaths.client.mvp.modele.process;

import fr.upmf.animaths.client.mvp.MathObject.MathObjectElementPresenter;

public class EquationAdd implements Process {
	
	MathObjectElementPresenter<?> element;


	
	public boolean canWeAdd() {
		return false;
	}

	public void replay() {
		
	}
}
