package com.play2.crud.template;

import com.play2.crud.beanprocessor.ScaffoldableBean;

public abstract class CrudView implements CrudTemplate {
	protected ScaffoldableBean sb;
	
	public CrudView(ScaffoldableBean sb) {
		this.sb = sb;
	}

	public abstract String htmlCreate();
	
	public abstract String htmlEdit();
	
	public abstract String htmlList();

	public abstract String htmlMakerQueue();

	public abstract String htmlCheckerQueue();

	public abstract String htmlDetails();
	
	public abstract String routeCreate();
	
	public abstract String routeEdit();
	
	public abstract String routeList();
	
	public abstract String routeDetails();
}
