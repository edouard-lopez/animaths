package fr.upmf.animaths.client.interaction.process;

import java.util.HashMap;
import java.util.Map;

import net.customware.gwt.presenter.client.EventBus;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.user.client.Timer;

import fr.upmf.animaths.client.interaction.MessageBox;
import fr.upmf.animaths.client.interaction.process.event.DragSelectedEvent;
import fr.upmf.animaths.client.interaction.process.event.DragSelectedHandler;
import fr.upmf.animaths.client.interaction.process.event.DropSelectedEvent;
import fr.upmf.animaths.client.interaction.process.event.DropSelectedHandler;
import fr.upmf.animaths.client.interaction.process.event.GrabSelectedEvent;
import fr.upmf.animaths.client.interaction.process.event.GrabSelectedHandler;
import fr.upmf.animaths.client.interaction.process.event.ProcessDoneEvent;
import fr.upmf.animaths.client.interaction.process.event.ProcessInterestedEvent;
import fr.upmf.animaths.client.interaction.process.event.ProcessLaunchEvent;
import fr.upmf.animaths.client.interaction.process.event.ProcessLaunchHandler;
import fr.upmf.animaths.client.interaction.process.event.TagDeclarationEvent;
import fr.upmf.animaths.client.mvp.AniMathsPresenter;
import fr.upmf.animaths.client.mvp.MODynamicPresenter;
import fr.upmf.animaths.client.mvp.MOWordingWidget;
import fr.upmf.animaths.client.mvp.MathObject.MOElement;

public abstract class MOAbstractProcess implements GrabSelectedHandler, DragSelectedHandler, DropSelectedHandler, ProcessLaunchHandler {
	
	public static final short PROCESS_NO = 0;
	public static final short PROCESS_CAUTION = 1;
	public static final short PROCESS_OK = 2;
	
	protected final EventBus eventBus = AniMathsPresenter.eventBus;

	protected static final void setEnabled(MOAbstractProcess process, boolean enabled) {
		if(enabled)
			process.setHandler(GrabSelectedEvent.getType());
		else
			process.removeHandler(GrabSelectedEvent.getType());
	}
	

	private Map<Type<?>,HandlerRegistration> hr = new HashMap<Type<?>,HandlerRegistration>();
	
	@SuppressWarnings("unchecked")
	public final <T extends EventHandler> void setHandler(Type<T> type) {
		if(hr.get(type)==null)
			hr.put(type, eventBus.addHandler(type,(T) this));
	}
	
	public final <T extends EventHandler> void removeHandler(Type<T> type) {
		if(hr.get(type)!=null) {
			hr.get(type).removeHandler();
			hr.put(type,null);
		}
	}
	
	protected MODynamicPresenter presenter;
	protected MOElement<?> selectedElement;

	protected MOElement<?> whereElement;
	protected short zoneH;
	protected short zoneV;

	private MOElement<?> choosenWhereElement;
	protected short choosenZoneH;
	protected short choosenZoneV;
	
	private short tag = -1;

	@Override
	public final void onGrabSelected(GrabSelectedEvent event) {
		presenter = event.getPresenter();
		selectedElement = event.getSelectedElement();
		if(isProcessInvolved()) {
			System.out.println("10");
			setHandler(DragSelectedEvent.getType());
			setHandler(DropSelectedEvent.getType());
			eventBus.fireEvent(new ProcessInterestedEvent());
		}
	}
	
	protected abstract boolean isProcessInvolved();

	@Override
	public final void onDragSelected(DragSelectedEvent event) {
		if(event.isFirstLevel())
			tag = -1;
		zoneH = event.getZoneH();
		zoneV = event.getZoneV();
		whereElement = event.getWhereElement();
		if(whereElement==selectedElement)
			return;
		short tag = getTagOfProcess();
		if(tag>this.tag) {
			if(tag>0)
				System.out.println("MOAbstractProcess : tag 1 ou 2");
			this.tag = tag;
			choosenWhereElement = whereElement;
			choosenZoneH = zoneH;
			choosenZoneV = zoneV;
			eventBus.fireEvent(new TagDeclarationEvent(tag,this));
		}
	}
	
	protected abstract short getTagOfProcess();

	@Override
	public final void onDropSelected(DropSelectedEvent event) {
		removeHandler(DragSelectedEvent.getType());
		removeHandler(DropSelectedEvent.getType());
	}
	
	@Override 
	public final void onProcessLaunch(ProcessLaunchEvent event) {
		zoneH = choosenZoneH;
		zoneV = choosenZoneV;
		whereElement = choosenWhereElement;
		askQuestion();
	}
	
	public final void askQuestion() {
		final MessageBox msgLoad = new MessageBox();
		msgLoad.setAsLoading(new MOWordingWidget("Veuillez patienter..."));
		onAskQuestion();
		msgLoad.hide();
	}

	public abstract void onAskQuestion(); 
	
	public final void executeProcess(final int answer) {
		final MessageBox msg = new MessageBox();
		msg.setWithCode(answer,getMessage(answer));
		// pour attendre un peu avant d'agir
		final Timer wait = new Timer() {
		    public void run() {
				msg.hide();
				if(answer>0) {
					eventBus.fireEvent(new ProcessDoneEvent());
					onExecuteProcess(answer);
					presenter.init(presenter.getElement());
				}
		    }
		};
		msg.addButton("Fermer", new ClickHandler() {
			public void onClick(ClickEvent event) {
				wait.cancel();
				msg.hide();
				if(answer>0) {
					onExecuteProcess(answer);
					eventBus.fireEvent(new ProcessDoneEvent());
					presenter.init(presenter.getElement());
				}
			}
		});
		wait.schedule(10000);
	}

	public abstract void onExecuteProcess(int answer);
	
	public abstract MOWordingWidget getMessage(int answer);
	
}
