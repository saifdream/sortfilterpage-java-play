package com.play2.crud.helper;

import java.nio.file.FileSystems;
import java.nio.file.Path;

import play.Play;

public class Constants {
	public static final String NEW_LINE = System.lineSeparator();
	public static final String DOUBLE_NEW_LINE = NEW_LINE + NEW_LINE;
	public static final String TRIPPLE_NEW_LINE = DOUBLE_NEW_LINE + NEW_LINE;

	public static final String CONF_KEY_CRUD_PKG = "play2crudpackage";
	public static final String DEFAULT_CRUD_PKG = "models.*";
	public static final String CRUD_PKG_NAME_DELIM = ",";

	public static final String ROUTES = "routes";
	public static final String CONF = "conf";
	public static final String CONTROLLERS = "controllers";
	public static final String ACTION_CREATE_FORM = "createForm()";
	public static final String ACTION_CREATE = "create()";
	public static final String ACTION_EDIT_FORM = "editForm";  // receives id, so no braces
	public static final String ACTION_EDIT = "edit()";
	public static final String ACTION_LIST = "list()";
	public static final String ACTION_DETAILS = "details";  // receives id, so no braces
	public static final String ACTION_CHECKER_QUEUE_FORM = "checkerQueueForm()";  // receives id, so no braces
	public static final String ACTION_CHECKER_QUEUE = "checkerQueue()";
	public static final String ACTION_MAKER_QUEUE_FORM = "makerQueueForm()";  // receives id, so no braces

	//public static final String ROUTES_USER_HOME_PAGE = ROUTES + ".Application.userHomePage()";
	//public static final String ACTION_USER_HOME_PAGE = CONTROLLERS + ".Application.userHomePage()";
	public static final String ROUTES_USER_HOME_PAGE = ROUTES + ".Application.dashboard()";
	public static final String ACTION_USER_HOME_PAGE = CONTROLLERS + ".Application.dashboard()";

	public static final Path BASE_PATH = Play.application().path().toPath();
	public static final Path CONTROLLER_PATH = FileSystems.getDefault().getPath(BASE_PATH.toString(), "app", CONTROLLERS);
	public static final Path ROUTES_PATH = FileSystems.getDefault().getPath(BASE_PATH.toString(), CONF, ROUTES);

	public static final String ID_TYPE_INTEGER = "Integer";
	public static final String ID_TYPE_LONG = "Long";

    public static final String DUMMY_MODEL_PACKAGE_NAME = "models.dummy.formmodel";
    public static final String DUMMY_MODEL_CLASS_PREFIX = "Dummy";

}
