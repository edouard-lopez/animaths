package fr.upmf.animaths.client.mvp.MathObject;

import fr.upmf.animaths.client.mvp.widgets.MathML.MathMLElement;
import fr.upmf.animaths.client.mvp.widgets.MathML.MathMLOperator;

public class MathObjectEquationPresenter extends MathObjectElementPresenter<MathObjectEquationPresenter.Display> {

	public static short type = MathObjectElementPresenter.MATH_OBJECT_EQUATION;
	public short getType() {
		return type;
	}

	private MathObjectElementPresenter<?> leftHandSide;
	private MathObjectElementPresenter<?> rightHandSide;
	
	public interface Display extends MathObjectElementView {
		abstract public void setSign(MathMLOperator sign);
		abstract public MathMLOperator getSign();
	}

	public MathObjectEquationPresenter() {
		super(new MathObjectEquationView());
	}
	
	public MathObjectEquationPresenter(MathObjectElementPresenter<?> leftHandSide, MathObjectElementPresenter<?> rightHandSide) {
		this();
		setLeftHandSide(leftHandSide);
		setRightHandSide(rightHandSide);
	}

	@Override
	public void pack(MathMLElement mathMLParent) {
		leftHandSide.pack(mathMLParent);
		display.setSign(MathMLOperator.equality());
		mathMLParent.appendChild(display.getSign());
		map.put(display.getSign().getElement(),this);
		rightHandSide.pack(mathMLParent);
	}

	@Override
	public MathObjectEquationPresenter clone() {
		return new MathObjectEquationPresenter(leftHandSide.clone(),rightHandSide.clone());
	}

	@Override
	public void setState(short state) {
		this.state = state;
		leftHandSide.setState(state);
		display.getSign().setState(state);
		rightHandSide.setState(state);
	}

	@Override
	public MathObjectElementPresenter<?> getMathObjectFirstChild() {
		return leftHandSide;
	}

	@Override
	public MathObjectElementPresenter<?> getMathObjectNextChild(MathObjectElementPresenter<?> child) {
		return (child==leftHandSide)?rightHandSide:leftHandSide;
	}

	@Override
	public MathObjectElementPresenter<?> getMathObjectPreviousChild(MathObjectElementPresenter<?> child) {
		return (child==leftHandSide)?rightHandSide:leftHandSide;
	}

	@Override
	public int getBoundingClientBottom() {
		return Math.max(leftHandSide.getBoundingClientBottom(),rightHandSide.getBoundingClientBottom());
	}

	@Override
	public int getBoundingClientLeft() {
		return leftHandSide.getBoundingClientLeft();
	}

	@Override
	public int getBoundingClientTop() {
		return Math.min(leftHandSide.getBoundingClientTop(),rightHandSide.getBoundingClientTop());
	}

	@Override
	public int getBoundingClientRight() {
		return rightHandSide.getBoundingClientRight();
	}

	public MathObjectElementPresenter<?> getLeftHandSide() {
		return leftHandSide;
	}

	public MathObjectElementPresenter<?> getRightHandSide() {
		return rightHandSide;
	}

	public void setLeftHandSide(MathObjectElementPresenter<?> leftHandSide) {
		leftHandSide.setMathObjectParent(this);
		this.leftHandSide = leftHandSide;
	}

	public void setRightHandSide(MathObjectElementPresenter<?> rightHandSide) {
		rightHandSide.setMathObjectParent(this);
		this.rightHandSide = rightHandSide;
	}

}
