package com.king.desk.gdata.model.live;

public interface ObservableEmitter<T> {
	void onNext(T value);
	void onError(Throwable e);
}
