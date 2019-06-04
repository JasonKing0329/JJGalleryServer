package com.king.desk.gdata.viewmodel;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import com.king.desk.gdata.model.ImageProvider;
import com.king.desk.gdata.model.SqlInstance;
import com.king.desk.gdata.model.live.LiveData;
import com.king.desk.gdata.model.live.Observable;
import com.king.desk.gdata.model.live.ObservableEmitter;
import com.king.desk.gdata.model.live.ObservableOnSubscribe;
import com.king.desk.gdata.model.live.Observer;
import com.king.desk.gdata.res.R;
import com.king.service.gdb.bean.Record;
import com.king.service.gdb.bean.Star;

public class StarViewModel {

	public LiveData<String> messageObserver = new LiveData<>();

	public LiveData<List<Star>> starsObserver = new LiveData<>();

	public LiveData<String> videoInfoObserver = new LiveData<>();

	public LiveData<Image> imageObserver = new LiveData<>();

	public LiveData<Image> rec1ImageObserver = new LiveData<>();

	public LiveData<Image> rec2ImageObserver = new LiveData<>();

	public LiveData<List<Record>> recordsObserver = new LiveData<>();

	public LiveData<Boolean> noVideoObserver = new LiveData<>();

	private Map<Character, Integer> indexMap = new HashMap<>();

	public void loadCoverImage() {

		String path = "D:/king/game/other/img/gdb/star/added/Robbie Rojo/234672346.PNG";
		try {
			imageObserver.setValue(ImageIO.read(new File(path)));
		} catch (IOException e) {
			e.printStackTrace();
			try {
				imageObserver.setValue(ImageIO.read(new File(R.drawable.ic_def_person)));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void loadStars() {
		queryStars()
			.subscribe(new Observer<List<Star>>() {
				
				@Override
				public void onNext(List<Star> value) {
					starsObserver.setValue(value);
				}
				
				@Override
				public void onError(Throwable e) {
					e.printStackTrace();
					messageObserver.setValue(e.getMessage());
				}
			});
	}

	private Observable<List<Star>> queryStars() {
		return Observable.create(new ObservableOnSubscribe<List<Star>>() {

			@Override
			public void subscribe(ObservableEmitter<List<Star>> e)
					throws Exception {
				List<Star> list = SqlInstance.get().getDao().queryAllStars("LOWER(name)");
				char lastChar = ' ';
				for (int i = 0; i < list.size(); i++) {
					char index = list.get(i).getName().toUpperCase().charAt(0);
					if (index != lastChar) {
						indexMap.put(index, i);
						lastChar = index;
					}
				}
				e.onNext(list);
			}
		});
	}
	
	public int getCharPosition(char charIndex) {
		Integer position = indexMap.get(charIndex);
		if (position == null) {
			return -1;
		}
		else {
			return position;
		}
	}

	public String getStarName(int index) {
		try {
			return starsObserver.getValue().get(index).getName();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public Star getStar(int index) {
		try {
			return starsObserver.getValue().get(index);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private String getVideoInfo(Star star) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(star.getRecords()).append(" Videos");
		if (star.getBetop() > 0) {
			buffer.append(", ").append(star.getBetop()).append(" as top");
		}
		if (star.getBebottom() > 0) {
			buffer.append(", ").append(star.getBebottom()).append(" as bottom");
		}
		return buffer.toString();
	}

	public String loadVideoInfo(int index) {
		try {
			Star star = starsObserver.getValue().get(index);
			videoInfoObserver.setValue(getVideoInfo(star));
			if (star.getRecords() == 0) {
				noVideoObserver.setValue(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public void loadStarImage(int index) {
		try {
			Star star = starsObserver.getValue().get(index);
			loadStarImage(star.getName())
				.subscribe(new Observer<Image>() {

					@Override
					public void onNext(Image value) {
						imageObserver.setValue(value);
					}

					@Override
					public void onError(Throwable e) {
						e.printStackTrace();
						messageObserver.setValue(e.getMessage());
					}
				});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Observable<Image> loadStarImage(final String name) {
		return Observable.create(new ObservableOnSubscribe<Image>() {

			@Override
			public void subscribe(ObservableEmitter<Image> e)
					throws Exception {
				String path = ImageProvider.getStarRandomImage(name);
				if (path == null) {
					e.onNext(ImageIO.read(new File(R.drawable.ic_def_person)));
				}
				else {
					try {
						e.onNext(ImageIO.read(new File(path)));
					} catch (Exception e2) {
						e.onNext(ImageIO.read(new File(R.drawable.ic_def_person)));
					}
				}
			}
		});
	}

	public void recount(int index) {
		try {
			Star star = starsObserver.getValue().get(index);
			recountStar(star)
				.subscribe(new Observer<Star>() {

					@Override
					public void onNext(Star value) {
						videoInfoObserver.setValue(getVideoInfo(value));
					}

					@Override
					public void onError(Throwable e) {
						e.printStackTrace();
						messageObserver.setValue(e.getMessage());
					}
				});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Observable<Star> recountStar(final Star star) {
		return Observable.create(new ObservableOnSubscribe<Star>() {

			@Override
			public void subscribe(ObservableEmitter<Star> e)
					throws Exception {
				SqlInstance.get().getDao().updateCountStar(star, false);
				e.onNext(star);
			}
		});
	}

	public void loadStarRecords(int index) {
		try {
			rec1ImageObserver.setValue(ImageIO.read(new File(R.drawable.ic_def_record)));
			rec2ImageObserver.setValue(ImageIO.read(new File(R.drawable.ic_def_record)));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			Star star = starsObserver.getValue().get(index);
			queryStarRecords(star.getId())
				.subscribe(new Observer<List<Record>>() {

					@Override
					public void onNext(List<Record> value) {
						recordsObserver.setValue(value);
					}

					@Override
					public void onError(Throwable e) {
						e.printStackTrace();
						messageObserver.setValue(e.getMessage());
					}
				});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Observable<List<Record>> queryStarRecords(final long starId) {
		return Observable.create(new ObservableOnSubscribe<List<Record>>() {

			@Override
			public void subscribe(ObservableEmitter<List<Record>> e)
					throws Exception {
				List<Record> list = SqlInstance.get().getDao().queryStarRecords(starId);
				e.onNext(list);
			}
		});
	}

	public String getRecordDirectory(int index) {
		try {
			Record record = recordsObserver.getValue().get(index);
			return record.getDirectory();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void loadRecordImages(int index) {
		try {
			Record record = recordsObserver.getValue().get(index);
			loadRecordImage(record.getName())
				.subscribe(new Observer<List<Image>>() {

					@Override
					public void onNext(List<Image> value) {
						rec1ImageObserver.setValue(value.get(0));
						rec2ImageObserver.setValue(value.get(1));
					}

					@Override
					public void onError(Throwable e) {
						e.printStackTrace();
						messageObserver.setValue(e.getMessage());
					}
				});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Observable<List<Image>> loadRecordImage(final String name) {
		return Observable.create(new ObservableOnSubscribe<List<Image>>() {

			@Override
			public void subscribe(ObservableEmitter<List<Image>> e)
					throws Exception {
				List<Image> list = new ArrayList<>();
				List<String> paths = ImageProvider.getRecordRandomImages(name, 2);
				for (int i = 0; i < 2; i++) {
					if (i < paths.size()) {
						try {
							list.add(ImageIO.read(new File(paths.get(i))));
						} catch (Exception e2) {
							list.add(ImageIO.read(new File(R.drawable.ic_def_record)));
						}
					}
					else {
						list.add(ImageIO.read(new File(R.drawable.ic_def_record)));
					}
				}
				e.onNext(list);
			}
		});
	}

	public void deleteStar(int index) {
		try {
			Star star = starsObserver.getValue().get(index);
			SqlInstance.get().getDao().deleteStar(star.getId());
			starsObserver.getValue().remove(index);
			starsObserver.setValue(starsObserver.getValue());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
