package fr.upmf.animaths.client.mvp.MathObject;

import fr.upmf.animaths.client.mvp.widgets.MathML.MathMLElement;
import fr.upmf.animaths.client.mvp.widgets.MathML.MathMLOperator;

public class MathObjectSignedElementPresenter extends MathObjectElementPresenter<MathObjectSignedElementPresenter.Display> {

	public static final short type = MathObjectElementPresenter.MATH_OBJECT_SIGNED_ELEMENT;
	public short getType() {
		return type;
	}
	
	private boolean isMinus = false;
	private boolean needsSign = true;
	private MathObjectElementPresenter<?> child;
	
	public interface Display extends MathObjectElementView {
		abstract public void setLFence(MathMLOperator lFence);
		abstract public void setRFence(MathMLOperator rFence);
		abstract public void setSign(MathMLOperator sign);
		abstract public MathMLOperator getLFence();
		abstract public MathMLOperator getRFence();
		abstract public MathMLOperator getSign();
	}

	public MathObjectSignedElementPresenter() {
		super(new MathObjectSignedElementView());
	}
	
	public MathObjectSignedElementPresenter(MathObjectElementPresenter<?> child) {
		this();
		setChild(child);
	}
	
	public MathObjectSignedElementPresenter(MathObjectElementPresenter<?> child, boolean isMinus) {
		this(child);
		setMinus(isMinus);
	}
	
	@Override
	public void pack(MathMLElement mathMLParent) {
		boolean needsFence = needsFence();
		if(needsFence) {
			display.setLFence(MathMLOperator.lFence());
			mathMLParent.appendChild(display.getLFence());
			map.put(display.getLFence().getElement(),this);
		}
		if(needsSign||isMinus) {
			display.setSign(new MathMLOperator(isMinus?"-":"+"));
			mathMLParent.appendChild(display.getSign());
			map.put(display.getSign().getElement(),this);
		}
		child.pack(mathMLParent);
		if(needsFence) {
			display.setRFence(MathMLOperator.rFence());
			mathMLParent.appendChild(display.getRFence());
			map.put(display.getRFence().getElement(),this);
		}
	}

	@Override
	public MathObjectSignedElementPresenter clone() {
		return new MathObjectSignedElementPresenter(child.clone(),isMinus);
	}

	@Override
	public void setState(short state) {
		this.state = state;
		if(display.getLFence()!=null)
			display.getLFence().setState(state);
		if(display.getRFence()!=null)
			display.getRFence().setState(state);
		if(display.getSign()!=null)
			display.getSign().setState(state);
		child.setState(state);
	}

	@Override
	public  MathObjectElementPresenter<?> getMathObjectFirstChild() {
		if(child.getType()==MathObjectElementPresenter.MATH_OBJECT_NUMBER
				||child.getType()==MathObjectElementPresenter.MATH_OBJECT_IDENTIFIER)
			return this;
		else
			return child.getMathObjectFirstChild();
	}

	@Override
	public MathObjectElementPresenter<?> getMathObjectNextChild(MathObjectElementPresenter<?> child) {
		return mathObjectParent.getMathObjectNextChild(this);
	}

	@Override
	public MathObjectElementPresenter<?> getMathObjectPreviousChild(MathObjectElementPresenter<?> child) {
		return mathObjectParent.getMathObjectPreviousChild(this);
	}

	@Override
	public int getBoundingClientBottom() {
		if(display.getLFence()!=null)
			return (int) (display.getLFence().getBoundingClientTop() + display.getLFence().getBoundingClientHeight());
		else 
			return child.getBoundingClientBottom();
	}

	@Override
	public int getBoundingClientLeft() {
		if(display.getLFence()!=null)
			return (int) display.getLFence().getBoundingClientLeft();
		else if(display.getSign()!=null)
			return (int) display.getSign().getBoundingClientLeft();
		else 
			return child.getBoundingClientLeft();
	}

	@Override
	public int getBoundingClientRight() {
		if(display.getRFence()!=null)
			return (int) (display.getRFence().getBoundingClientLeft() + display.getRFence().getBoundingClientWidth());
		else
			return child.getBoundingClientRight();
	}

	@Override
	public int getBoundingClientTop() {
		if(display.getLFence()!=null)
			return (int) display.getLFence().getBoundingClientTop();
		else
			return child.getBoundingClientTop();
	}

	public boolean isMinus() {
		return isMinus;
	}

	public void setMinus(boolean isMinus) {
		this.isMinus = isMinus;
	}		

	public boolean needsSign() {
		return needsSign;
	}

	public void setNeedsSign(boolean needsSign) {
		this.needsSign = needsSign;
	}

	public MathObjectElementPresenter<?> getChild() {
		return child;
	}
	
	public void setChild(MathObjectElementPresenter<?> child) {
		child.setMathObjectParent(this);
		this.child = child;
	}

}
