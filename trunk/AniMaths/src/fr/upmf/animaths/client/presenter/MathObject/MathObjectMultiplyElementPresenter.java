package fr.upmf.animaths.client.presenter.MathObject;

import com.google.gwt.dom.client.Element;

import fr.upmf.animaths.client.display.MathML.MathMLElement;
import fr.upmf.animaths.client.display.MathML.MathMLOperator;
import fr.upmf.animaths.client.display.MathObject.IMathObjectHasSign;
import fr.upmf.animaths.client.display.MathObject.MathObjectElementDisplay;
import fr.upmf.animaths.client.display.MathObject.MathObjectMultiplyElementDisplay;
import fr.upmf.animaths.client.presenter.MathObjectPresenter;
import fr.upmf.animaths.client.presenter.MathObject.Interfaces.IMathObjectHasOneChild;

public class MathObjectMultiplyElementPresenter extends MathObjectElementPresenter<MathObjectMultiplyElementPresenter.Display> implements IMathObjectHasOneChild {

	public static final short type = MathObjectElementPresenter.MATH_OBJECT_MULTIPLY_ELEMENT;
	@Override
	public short getType() {
		return type;
	}

	private boolean isDivided = false;
	private boolean needsSign = true;
	private MathObjectElementPresenter<?> child;

	public interface Display extends MathObjectElementDisplay, IMathObjectHasSign {
	}

	public MathObjectMultiplyElementPresenter() {
		super(new MathObjectMultiplyElementDisplay());
	}

	public MathObjectMultiplyElementPresenter(MathObjectElementPresenter<?> child) {
		this();
		setChild(child);
	}
	
	public MathObjectMultiplyElementPresenter(MathObjectElementPresenter<?> child, boolean isDivided) {
		this(child);
		setDivided(isDivided);
	}
	
	@Override
	public void pack(MathMLElement mathMLParent, MathObjectPresenter<?> presenter) {
		if(needsSign) {
			display.setSign(MathMLOperator.times());
			mathMLParent.appendChild(display.getSign());
			if(presenter!=null)
				presenter.putDOMElement(display.getSign().getElement(),this);
		}
		child.pack(mathMLParent, presenter);
	}

	@Override
	public MathObjectMultiplyElementPresenter clone() {
		return new MathObjectMultiplyElementPresenter(child.clone(),isDivided);
	}

	@Override
	public void setStyleClass(short styleClass) {
		this.styleClass = styleClass;
		if(display.getSign()!=null)
			display.getSign().setStyleClass(styleClass);
		child.setStyleClass(styleClass);
	}

	@Override
	public MathObjectElementPresenter<?> getMathObjectFirstSelectableChild() {
		if(child.getType()==MathObjectElementPresenter.MATH_OBJECT_NUMBER
				||child.getType()==MathObjectElementPresenter.MATH_OBJECT_IDENTIFIER)
			return this;
		else
			return child.getMathObjectFirstSelectableChild();
	}

	@Override
	public MathObjectElementPresenter<?> getMathObjectNextSelectableChild(MathObjectElementPresenter<?> child) {
		return mathObjectParent.getMathObjectNextSelectableChild(this);
	}

	@Override
	public MathObjectElementPresenter<?> getMathObjectPreviousSelectableChild(MathObjectElementPresenter<?> child) {
		return mathObjectParent.getMathObjectPreviousSelectableChild(this);
	}

	@Override
	public int getBoundingClientLeft() {
		if(display.getSign()!=null)
			return (int) display.getSign().getBoundingClientLeft();
		else
			return child.getBoundingClientLeft();
	}

	@Override
	public int getBoundingClientRight() {
		return child.getBoundingClientRight();
	}

	@Override
	public int getBoundingClientTop() {
		return child.getBoundingClientTop();
	}

	@Override
	public int getBoundingClientBottom() {
			return child.getBoundingClientBottom();
	}

	@Override
	public MathObjectElementPresenter<?> getChild() {
		return child;
	}
	
	@Override
	public void setChild(MathObjectElementPresenter<?> child) {
		child.setMathObjectParent(this);
		this.child = child;
	}

	public void setNeedsSign(boolean needsSign) {
		this.needsSign = needsSign;
	}

	public boolean needsSign() {
		return needsSign;
	}

	public boolean isDivided() {
		return isDivided;
	}

	public void setDivided(boolean isDivided) {
		this.isDivided = isDivided;
	}

	@Override
	public Element getFirstDOMElement() {
		if(display.getSign()!=null)
			return display.getSign().getElement();
		return child.getFirstDOMElement();
	}

	@Override
	public Element getLastDOMElement() {
		return child.getLastDOMElement();
	}		

}
