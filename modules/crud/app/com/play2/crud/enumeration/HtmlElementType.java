package com.play2.crud.enumeration;

public enum HtmlElementType {
	NONE(0)
    , TEXT_BOX(1)
    , TEXT_AREA(2)
    , DROP_DOWN(3)
    , RADIO_BUTTON(4)
    , CHECK_BOX(5)
    , PASSWORD(6)
    , DATE_PICKER_CALENDAR(7)
    , DATE_PICKER_DROP_DOWN(8)
    , NUMBER(9)
    ;
	
	private int value;

	private HtmlElementType(int value) {
			this.value = value;
	}
}
