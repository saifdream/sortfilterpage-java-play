package com.play2.crud.template;

import com.play2.crud.beanprocessor.ScaffoldableBean;

public abstract class CrudController implements CrudTemplate {
	protected ScaffoldableBean sb;
		
	public CrudController(ScaffoldableBean sb) {
		this.sb = sb;
	}

	public abstract String actionCreateForm();
	
	public abstract String actionCreate();
	
	public abstract String actionEditForm();
	
	public abstract String actionEdit();
	
	public abstract String actionList();
	
	public abstract String actionDetails();

	public abstract String actionCheckerQueueForm();

	public abstract String actionCheckerQueue();

	public abstract String actionMakerQueueForm();

	public abstract String getControllerBody(String... actions);
}
