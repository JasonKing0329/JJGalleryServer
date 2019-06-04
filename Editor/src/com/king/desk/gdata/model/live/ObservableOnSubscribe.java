package com.king.desk.gdata.model.live;

public interface ObservableOnSubscribe<T> {
	void subscribe(ObservableEmitter<T> e) throws Exception;
}
