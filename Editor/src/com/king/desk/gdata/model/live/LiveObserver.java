package com.king.desk.gdata.model.live;

public interface LiveObserver<T> {

	public void onChanged(T value);
}
