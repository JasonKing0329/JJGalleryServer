package com.king.desk.gdata.model.live;

public interface Observer<T> {

	public void onNext(T value);
	void onError(Throwable e);
}
