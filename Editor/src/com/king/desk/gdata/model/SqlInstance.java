package com.king.desk.gdata.model;

import com.king.desk.gdata.Conf;

public class SqlInstance {

	private static SqlInstance instance;

	public static SqlInstance get() {
		if (instance == null) {
			instance = new SqlInstance();
		}
		return instance;
	}

	private SqlDao dao;
	
	private SqlInstance() {
		dao = new SqlDao();
		connect();
	}
	
	public void connect() {
		connect(Conf.WEB_DB);
	}

	public void connect(String db) {
		dao.connect(db);
	}

	public void close() {
		dao.close();
	}
	
	public SqlDao getDao() {
		return dao;
	}

}
