package com.sourcey.materiallogindemo.ui;
/**
 * View model for every adapter cell (regular ot ad)
 */
public class ContentAdapterModel {
	public final int layoutId;

	/**
	 * Only for {@link ContentCellHolder}
	 */
	public final int day;
	public final int hour;
	public final String note;

	/**
	 * Only for {@link ContentCellHolder}
	 */
	//public final String description;

	public ContentAdapterModel(int layoutId, int day, int hour , String note) {
		this.layoutId = layoutId > 0 ? layoutId : 0;
		this.day = day;
		this.hour = hour;
		this.note = note;
	}
}
