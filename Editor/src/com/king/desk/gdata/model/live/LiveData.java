package com.king.desk.gdata.model.live;

import java.util.ArrayList;
import java.util.List;

public class LiveData<T> {
	
	private T value;
	
	private List<LiveObserver<T>> observers;
	
	public LiveData() {
		observers = new ArrayList<LiveObserver<T>>();
	}

	public void setValue(T data) {
		this.value = data;
		for (int i = 0; i < observers.size(); i++) {
			observers.get(i).onChanged(data);
		}
	}
	
	public T getValue() {
		return value;
	}
	
	public void observe(LiveObserver<T> observer) {
		observers.add(observer);
	}
}
