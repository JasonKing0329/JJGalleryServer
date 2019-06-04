package com.king.desk.gdata.model.live;

import java.util.List;

import javax.swing.SwingWorker;

public class Observable<T> extends SwingWorker<T, Object> {

	public static <T> Observable<T> create(ObservableOnSubscribe<T> source) {
		Observable<T> observable = new Observable<>();
		observable.setSource(source);
		return observable;
	}
	
	private ObservableOnSubscribe<T> source;
	
	private ObservableEmitter<T> emitter;
	
	private Observer<T> observer;
	
	private Observable() {
		emitter = new ObservableEmitter<T>() {

			@Override
			public void onNext(T value) {
				publish(value);
			}

			@Override
			public void onError(Throwable e) {
				publish(e);
			}
		};
	}
	
	public void subscribe(Observer<T> observer) {
		this.observer = observer;
		execute();
	}
	
	@Override
	protected T doInBackground() throws Exception {
		try {
			source.subscribe(emitter);
		} catch (Exception e) {
			publish(e);
		}
		return null;
	}

	@Override
	protected void done() {
		// 改用publish来实现
//		try {
//			observer.onNext(get());
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		} catch (ExecutionException e) {
//			e.printStackTrace();
//		}
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void process(List<Object> arg) {
		Object object = arg.get(0);
		if (object instanceof Throwable) {
			observer.onError((Throwable) object);
		}
		else {
			observer.onNext((T) object);
		}
	}

	public void setSource(ObservableOnSubscribe<T> source) {
		this.source = source;
	}

}
