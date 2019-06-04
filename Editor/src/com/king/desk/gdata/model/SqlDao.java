package com.king.desk.gdata.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.king.service.gdb.bean.GDBProperites;
import com.king.service.gdb.bean.Record;
import com.king.service.gdb.bean.RecordStar;
import com.king.service.gdb.bean.RecordType1v1;
import com.king.service.gdb.bean.RecordType3w;
import com.king.service.gdb.bean.Star;

public class SqlDao {

	private final String TABLE_SEQUENCE = "sqlite_sequence";
	private final String TABLE_CONF = "properties";
	private final String TABLE_STAR = "stars";
	private final String TABLE_RECORD = "record";
	private final String TABLE_RECORD_1V1 = "record_type1";
	private final String TABLE_RECORD_3W = "record_type3";
	private final String TABLE_RECORD_STAR = "record_star";

	private Connection connection;
	
	public SqlDao() {
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void connect(String dbFile) {
		try {
			connection = DriverManager.getConnection("jdbc:sqlite:" + dbFile);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void close() {
		try {
			connection.close();
			connection = null;
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void beginTransaction() {
		try {
			connection.setAutoCommit(false);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void commit() throws Exception {
		try {
			connection.commit();
			connection.setAutoCommit(true);
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				connection.rollback();
				connection.setAutoCommit(true);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			throw e;
		}
	}

	public List<Star> queryAllStars() {
		return queryAllStars(null);
	}
	
	public List<Star> queryAllStars(String orderBy) {
		List<Star> list = new ArrayList<>();
		String sql = "SELECT * FROM " + TABLE_STAR;
		if (orderBy != null && orderBy.trim().length() > 0) {
			sql = sql + " ORDER BY " + orderBy;
		}
		Statement statement = null;
		Star star = null;
		try {
			statement = connection.createStatement();
			ResultSet set = statement.executeQuery(sql);
			while (set.next()) {
				star = parseStar(set);
				list.add(star);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	public Map<Long, Star> queryStarMaps() {
		Map<Long, Star> map = new HashMap<>();
		String sql = "SELECT * FROM " + TABLE_STAR;
		Statement statement = null;
		Star star = null;
		try {
			statement = connection.createStatement();
			ResultSet set = statement.executeQuery(sql);
			while (set.next()) {
				star = parseStar(set);
				map.put(star.getId(), star);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return map;
	}

	private Star parseStar(ResultSet set) throws SQLException {

		Star star = new Star();
		star.setId(set.getLong(1));
		star.setName(set.getString(2));
		star.setRecords(set.getInt(3));
		star.setBetop(set.getInt(4));
		star.setBebottom(set.getInt(5));
		star.setAverage(set.getFloat(6));
		star.setMax(set.getInt(7));
		star.setMin(set.getInt(8));
		star.setCaverage(set.getFloat(9));
		star.setCmax(set.getInt(10));
		star.setCmin(set.getInt(11));
		return star;
	}

	public List<Record> queryRecords() {
		return queryRecords(-1);
	}
	
	public List<Record> queryRecords(int type) {
		String sql = "SELECT * FROM " + TABLE_RECORD;
		if (type != -1) {
			sql = sql + " WHERE type = " + type;
		}
		sql = sql + " ORDER BY deprecated, directory, name";
		Statement statement = null;
		List<Record> list = new ArrayList<>();
		try {
			statement = connection.createStatement();
			ResultSet set = statement.executeQuery(sql);
			while (set.next()) {
				Record record = parseRecord(set);
				list.add(record);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	public List<Record> queryStarRecords(long starId) {
		String sql = "SELECT r.* FROM record r JOIN record_star rs ON r._id=rs.record_id WHERE rs.star_id=" + starId + " ORDER BY r.score DESC";
		Statement statement = null;
		List<Record> list = new ArrayList<>();
		try {
			statement = connection.createStatement();
			ResultSet set = statement.executeQuery(sql);
			while (set.next()) {
				Record record = parseRecord(set);
				list.add(record);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	private Record parseRecord(ResultSet set) throws SQLException {
		Record record = new Record();
		record.setId(set.getLong(1));
		record.setScene(set.getString(2));
		record.setDirectory(set.getString(3));
		record.setName(set.getString(4));
		record.setHdLevel(set.getInt(5));
		record.setScore(set.getInt(6));
		record.setScoreFeel(set.getInt(7));
		record.setScoreStar(set.getInt(8));
		record.setScorePassion(set.getInt(9));
		record.setScoreCum(set.getInt(10));
		record.setScoreSpecial(set.getInt(11));
		record.setScoreBareback(set.getInt(12));
		record.setDeprecated(set.getInt(13));
		record.setSpecialDesc(set.getString(14));
		record.setLastModifyTime(set.getLong(15));
		record.setType(set.getInt(16));
		record.setRecordDetailId(set.getLong(17));
		return record;
	}

	public RecordType1v1 queryRecordType1v1(long id) {
		String sql = "SELECT * FROM " + TABLE_RECORD_1V1 + " WHERE _id=" + id;
		Statement statement = null;
		RecordType1v1 data = null;
		try {
			statement = connection.createStatement();
			ResultSet set = statement.executeQuery(sql);
			if (set.next()) {
				data = parseRecord1v1(set);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return data;
	}

	private RecordType1v1 parseRecord1v1(ResultSet set) throws SQLException {
		RecordType1v1 record = new RecordType1v1();
		record.setId(set.getLong(1));
		record.setSequence(set.getInt(2));
		record.setScoreFkType1(set.getInt(3));
		record.setScoreFkType2(set.getInt(4));
		record.setScoreFkType3(set.getInt(5));
		record.setScoreFkType4(set.getInt(6));
		record.setScoreFkType5(set.getInt(7));
		record.setScoreFkType6(set.getInt(8));
		record.setScoreStory(set.getInt(9));
		record.setScoreScene(set.getInt(10));
		record.setScoreRim(set.getInt(11));
		record.setScoreBjob(set.getInt(12));
		record.setScoreForePlay(set.getInt(13));
		record.setScoreRhythm(set.getInt(14));
		record.setScoreCshow(set.getInt(15));
		return record;
	}

	public RecordType3w queryRecordType3w(long id) {
		String sql = "SELECT * FROM " + TABLE_RECORD_3W + " WHERE _id=" + id;
		Statement statement = null;
		RecordType3w data = null;
		try {
			statement = connection.createStatement();
			ResultSet set = statement.executeQuery(sql);
			if (set.next()) {
				data = parseRecord3w(set);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return data;
	}

	private RecordType3w parseRecord3w(ResultSet set) throws SQLException {
		RecordType3w record = new RecordType3w();
		record.setId(set.getLong(1));
		record.setSequence(set.getInt(2));
		record.setScoreFkType1(set.getInt(3));
		record.setScoreFkType2(set.getInt(4));
		record.setScoreFkType3(set.getInt(5));
		record.setScoreFkType4(set.getInt(6));
		record.setScoreFkType5(set.getInt(7));
		record.setScoreFkType6(set.getInt(8));
		record.setScoreFkType7(set.getInt(9));
		record.setScoreFkType8(set.getInt(10));
		record.setScoreStory(set.getInt(11));
		record.setScoreScene(set.getInt(12));
		record.setScoreRim(set.getInt(13));
		record.setScoreBjob(set.getInt(14));
		record.setScoreForePlay(set.getInt(15));
		record.setScoreRhythm(set.getInt(16));
		record.setScoreCshow(set.getInt(17));
		return record;
	}

	public List<RecordStar> getRecordStars(Long recordId) {
		String sql = "SELECT * FROM " + TABLE_RECORD_STAR + " WHERE record_id = " + recordId;
		Statement statement = null;
		List<RecordStar> list = new ArrayList<>();
		try {
			statement = connection.createStatement();
			ResultSet set = statement.executeQuery(sql);
			while (set.next()) {
				RecordStar record = parseRecordStar(set, 0);
				list.add(record);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	private RecordStar parseRecordStar(ResultSet set, int offset) throws SQLException {

		RecordStar star = new RecordStar();
		star.setId(set.getLong(offset + 1));
		star.setRecordId(set.getLong(offset + 2));
		star.setStarId(set.getLong(offset + 3));
		star.setType(set.getInt(offset + 4));
		star.setScore(set.getInt(offset + 5));
		star.setScoreC(set.getInt(offset + 6));
		return star;
	}

	public void updateCountStar(Star star, boolean deleteNoRecords) {
		String sql = "select count(_id) as total, count(case type when 1 then 1 when 3 then 1 end) as top"
				+ ", count(case type when 2 then 1 when 3 then 1 end) as bottom"
				+ " from record_star"
				+ " where star_id=" + star.getId();
		Statement statement = null;
		try {
			statement = connection.createStatement();
			ResultSet set = statement.executeQuery(sql);
			if (set.next()) {
				star.setRecords(set.getInt(1));
				star.setBetop(set.getInt(2));
				star.setBebottom(set.getInt(3));
			}
			// 删除没有records的star
			if (deleteNoRecords && star.getRecords() == 0) {
				sql = "delete from stars where _id=" + star.getId();
				statement.executeUpdate(sql);
				System.out.println("delete star _id=" + star.getId() + ", _name=" + star.getName());
				return;
			}
			
			sql = "select avg(score), max(score), min(score) from record_star where star_id=" + star.getId() + " and score > 0";
			set = statement.executeQuery(sql);
			if (set.next()) {
				star.setAverage(set.getFloat(1));
				star.setMax(set.getInt(2));
				star.setMin(set.getInt(3));
			}
			
			sql = "select avg(score_c), max(score_c), min(score_c) from record_star where star_id=" + star.getId() + " and score_c > 0";
			set = statement.executeQuery(sql);
			if (set.next()) {
				star.setCaverage(set.getFloat(1));
				star.setCmax(set.getInt(2));
				star.setCmin(set.getInt(3));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
		updateStar(star);
	}

	public boolean insertStar(Star star) {
		String sql = "INSERT INTO " + TABLE_STAR + "(name,RECORDS,BETOP,BEBOTTOM,AVERAGE,MAX,MIN,CAVERAGE,CMAX,CMIN,FAVOR) VALUES(?,0,0,0,0,0,0,0,0,0,0)";
		PreparedStatement stmt = null;
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, star.getName());
			stmt.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public boolean updateStar(Star star) {
		// 带"'"号的要特殊处理
		String name = star.getName();
		if (name.contains("'")) {
			name = name.replace("'", "''");
		}
		PreparedStatement stmt = null;
		String sql = "UPDATE " + TABLE_STAR + " SET NAME=?,RECORDS=?,BETOP=?"
				+ ",BEBOTTOM=?,AVERAGE=?,MAX=?,MIN=?,CAVERAGE=?,CMAX=?,CMIN=?"
				+ " WHERE _id=?";
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, name);
			stmt.setInt(2, star.getRecords());
			stmt.setInt(3, star.getBetop());
			stmt.setInt(4, star.getBebottom());
			stmt.setFloat(5, star.getAverage());
			stmt.setInt(6, star.getMax());
			stmt.setInt(7, star.getMin());
			stmt.setFloat(8, star.getCaverage());
			stmt.setInt(9, star.getCmax());
			stmt.setInt(10, star.getCmin());
			stmt.setLong(11, star.getId());
			stmt.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public void insertRecord(Record record) {
		StringBuffer buffer = new StringBuffer("INSERT INTO ");
		buffer.append(TABLE_RECORD)
				.append("(scene,directory,name,HD_LEVEL,score,SCORE_FEEL")
				.append(",SCORE_STAR,SCORE_PASSION,SCORE_CUM,SCORE_SPECIAL,SCORE_BAREBACK,deprecated,SPECIAL_DESC")
				.append(",LAST_MODIFY_TIME,TYPE,RECORD_DETAIL_ID)")
				.append(" VALUES(?");
		for (int i = 0; i < 15; i ++) {
			buffer.append(",?");
		}
		buffer.append(")");
		String sql = buffer.toString();
		try {
			PreparedStatement stmt = connection.prepareStatement(sql);
			insertRecord(stmt, record);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void updateRecord(Record record) {
		StringBuffer buffer = new StringBuffer("UPDATE ");
		buffer.append(TABLE_RECORD)
				.append(" SET scene=?,directory=?,name=?,HD_LEVEL=?,score=?,SCORE_FEEL=?")
				.append(",SCORE_STAR=?,SCORE_PASSION=?,SCORE_CUM=?,SCORE_SPECIAL=?,SCORE_BAREBACK=?,deprecated=?")
				.append(",SPECIAL_DESC=?,LAST_MODIFY_TIME=?,TYPE=?,RECORD_DETAIL_ID=?")
				.append(" WHERE _id=?");
		String sql = buffer.toString();
		try {
			PreparedStatement stmt = connection.prepareStatement(sql);
			updateRecord(stmt, record);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void insertRecord1v1(RecordType1v1 record) {
		StringBuffer buffer = new StringBuffer("INSERT INTO ");
		buffer.append(TABLE_RECORD_1V1)
				.append("(SEQUENCE,SCORE_RHYTHM")
				.append(",SCORE_FORE_PLAY,SCORE_BJOB,SCORE_FK_TYPE1,SCORE_FK_TYPE2,SCORE_FK_TYPE3")
				.append(",SCORE_FK_TYPE4,SCORE_FK_TYPE5,SCORE_FK_TYPE6,SCORE_SCENE,SCORE_STORY")
				.append(",SCORE_CSHOW,SCORE_RIM)")
				.append(" VALUES(?");
		for (int i = 0; i < 13; i ++) {
			buffer.append(",?");
		}
		buffer.append(")");
		String sql = buffer.toString();
		try {
			PreparedStatement stmt = connection.prepareStatement(sql);
			insertRecord1v1(stmt, record);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void updateRecord1v1(RecordType1v1 record) {
		StringBuffer buffer = new StringBuffer("UPDATE ");
		buffer.append(TABLE_RECORD_1V1)
				.append(" SET SEQUENCE=?,SCORE_RHYTHM=?,SCORE_FORE_PLAY=?,SCORE_BJOB=?,SCORE_FK_TYPE1=?,SCORE_FK_TYPE2=?")
				.append(",SCORE_FK_TYPE3=?,SCORE_FK_TYPE4=?,SCORE_FK_TYPE5=?,SCORE_FK_TYPE6=?,SCORE_SCENE=?,SCORE_STORY=?")
				.append(",SCORE_CSHOW=?,SCORE_RIM=?")
				.append(" WHERE _id=?");
		String sql = buffer.toString();
		try {
			PreparedStatement stmt = connection.prepareStatement(sql);
			updateRecord1v1(stmt, record);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void insertRecord3W(RecordType3w record) {
		StringBuffer buffer = new StringBuffer("INSERT INTO ");
		buffer.append(TABLE_RECORD_3W)
				.append("(SEQUENCE, SCORE_RHYTHM")
				.append(",SCORE_FORE_PLAY,SCORE_BJOB,SCORE_FK_TYPE1,SCORE_FK_TYPE2,SCORE_FK_TYPE3")
				.append(",SCORE_FK_TYPE4,SCORE_FK_TYPE5,SCORE_FK_TYPE6,SCORE_FK_TYPE7,SCORE_FK_TYPE8,SCORE_SCENE,SCORE_STORY")
				.append(",SCORE_CSHOW,SCORE_RIM)")
				.append(" VALUES(?");
		for (int i = 0; i < 15; i ++) {
			buffer.append(",?");
		}
		buffer.append(")");
		String sql = buffer.toString();
		try {
			PreparedStatement stmt = connection.prepareStatement(sql);
			insertRecord3W(stmt, record);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void updateRecord3W(RecordType3w record) {
		StringBuffer buffer = new StringBuffer("UPDATE ");
		buffer.append(TABLE_RECORD_3W)
				.append(" SET SEQUENCE=?,SCORE_RHYTHM=?,SCORE_FORE_PLAY=?,SCORE_BJOB=?,SCORE_FK_TYPE1=?,SCORE_FK_TYPE2=?")
				.append(",SCORE_FK_TYPE3=?,SCORE_FK_TYPE4=?,SCORE_FK_TYPE5=?,SCORE_FK_TYPE6=?,SCORE_FK_TYPE7=?,SCORE_FK_TYPE8=?")
				.append(",SCORE_SCENE=?,SCORE_STORY=?,SCORE_CSHOW=?,SCORE_RIM=?")
				.append(" WHERE _id=?");
		String sql = buffer.toString();
		try {
			PreparedStatement stmt = connection.prepareStatement(sql);
			updateRecord3W(stmt, record);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public boolean insertRecord(PreparedStatement stmt, Record record) {
		try {
			int index = 1;
			stmt.setString(index++, record.getScene());
			stmt.setString(index++, record.getDirectory());
			stmt.setString(index++, record.getName());
			stmt.setInt(index++, record.getHdLevel());
			stmt.setInt(index++, record.getScore());
			stmt.setInt(index++, record.getScoreFeel());
			stmt.setInt(index++, record.getScoreStar());
			stmt.setInt(index++, record.getScorePassion());
			stmt.setInt(index++, record.getScoreCum());
			stmt.setInt(index++, record.getScoreSpecial());
			stmt.setInt(index++, record.getScoreBareback());
			stmt.setInt(index++, record.getDeprecated());
			stmt.setString(index++, record.getSpecialDesc());
			stmt.setLong(index++, record.getLastModifyTime());
			stmt.setInt(index++, record.getType());
			stmt.setLong(index++, record.getRecordDetailId());
			stmt.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return false;
	}
	public boolean updateRecord(PreparedStatement stmt, Record record) {
		try {
			int index = 1;
			stmt.setString(index++, record.getScene());
			stmt.setString(index++, record.getDirectory());
			stmt.setString(index++, record.getName());
			stmt.setInt(index++, record.getHdLevel());
			stmt.setInt(index++, record.getScore());
			stmt.setInt(index++, record.getScoreFeel());
			stmt.setInt(index++, record.getScoreStar());
			stmt.setInt(index++, record.getScorePassion());
			stmt.setInt(index++, record.getScoreCum());
			stmt.setInt(index++, record.getScoreSpecial());
			stmt.setInt(index++, record.getScoreBareback());
			stmt.setInt(index++, record.getDeprecated());
			stmt.setString(index++, record.getSpecialDesc());
			stmt.setLong(index++, record.getLastModifyTime());
			stmt.setInt(index++, record.getType());
			stmt.setLong(index++, record.getRecordDetailId());
			stmt.setLong(index++, record.getId());
			stmt.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return false;
	}
	public boolean insertRecord1v1(PreparedStatement stmt, RecordType1v1 record) {
		try {
			int index = 1;
			stmt.setInt(index ++, record.getSequence());
			stmt.setInt(index ++, record.getScoreRhythm());
			stmt.setInt(index++, record.getScoreForePlay());
			stmt.setInt(index++, record.getScoreBjob());
			stmt.setInt(index++, record.getScoreFkType1());
			stmt.setInt(index++, record.getScoreFkType2());
			stmt.setInt(index++, record.getScoreFkType3());
			stmt.setInt(index++, record.getScoreFkType4());
			stmt.setInt(index++, record.getScoreFkType5());
			stmt.setInt(index++, record.getScoreFkType6());
			stmt.setInt(index++, record.getScoreScene());
			stmt.setInt(index++, record.getScoreStory());
			stmt.setInt(index++, record.getScoreCshow());
			stmt.setInt(index++, record.getScoreRim());
			stmt.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return false;
	}
	public boolean updateRecord1v1(PreparedStatement stmt, RecordType1v1 record) {
		try {
			int index = 1;
			stmt.setInt(index ++, record.getSequence());
			stmt.setInt(index ++, record.getScoreRhythm());
			stmt.setInt(index++, record.getScoreForePlay());
			stmt.setInt(index++, record.getScoreBjob());
			stmt.setInt(index++, record.getScoreFkType1());
			stmt.setInt(index++, record.getScoreFkType2());
			stmt.setInt(index++, record.getScoreFkType3());
			stmt.setInt(index++, record.getScoreFkType4());
			stmt.setInt(index++, record.getScoreFkType5());
			stmt.setInt(index++, record.getScoreFkType6());
			stmt.setInt(index++, record.getScoreScene());
			stmt.setInt(index++, record.getScoreStory());
			stmt.setInt(index++, record.getScoreCshow());
			stmt.setInt(index++, record.getScoreRim());
			stmt.setLong(index++, record.getId());
			stmt.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return false;
	}
	public boolean insertRecord3W(PreparedStatement stmt, RecordType3w record) {
		try {
			int index = 1;
			stmt.setInt(index ++, record.getSequence());
			stmt.setInt(index ++, record.getScoreRhythm());
			stmt.setInt(index++, record.getScoreForePlay());
			stmt.setInt(index++, record.getScoreBjob());
			stmt.setInt(index++, record.getScoreFkType1());
			stmt.setInt(index++, record.getScoreFkType2());
			stmt.setInt(index++, record.getScoreFkType3());
			stmt.setInt(index++, record.getScoreFkType4());
			stmt.setInt(index++, record.getScoreFkType5());
			stmt.setInt(index++, record.getScoreFkType6());
			stmt.setInt(index++, record.getScoreFkType7());
			stmt.setInt(index++, record.getScoreFkType8());
			stmt.setInt(index++, record.getScoreScene());
			stmt.setInt(index++, record.getScoreStory());
			stmt.setInt(index++, record.getScoreCshow());
			stmt.setInt(index++, record.getScoreRim());
			stmt.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return false;
	}
	public boolean updateRecord3W(PreparedStatement stmt, RecordType3w record) {
		try {
			int index = 1;
			stmt.setInt(index ++, record.getSequence());
			stmt.setInt(index ++, record.getScoreRhythm());
			stmt.setInt(index++, record.getScoreForePlay());
			stmt.setInt(index++, record.getScoreBjob());
			stmt.setInt(index++, record.getScoreFkType1());
			stmt.setInt(index++, record.getScoreFkType2());
			stmt.setInt(index++, record.getScoreFkType3());
			stmt.setInt(index++, record.getScoreFkType4());
			stmt.setInt(index++, record.getScoreFkType5());
			stmt.setInt(index++, record.getScoreFkType6());
			stmt.setInt(index++, record.getScoreFkType7());
			stmt.setInt(index++, record.getScoreFkType8());
			stmt.setInt(index++, record.getScoreScene());
			stmt.setInt(index++, record.getScoreStory());
			stmt.setInt(index++, record.getScoreCshow());
			stmt.setInt(index++, record.getScoreRim());
			stmt.setLong(index++, record.getId());
			stmt.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public void deleteStar(long id) {
		String sql = "delete from stars where _id=" + id;
		Statement statement = null;
		try {
			statement = connection.createStatement();
			statement.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void deleteRecord(long id) {
		// 先删除detail表
		String sql = "select type,record_detail_id from record where _id=" + id;
		Statement statement = null;
		try {
			statement = connection.createStatement();
			ResultSet set = statement.executeQuery(sql);
			if (set.next()) {
				int type = set.getInt(1);
				int detail_id = set.getInt(2);
				if (type == GDBProperites.VALUE_RECORD_TYPE_1V1) {
					sql = "delete from record_type1 where _id=" + detail_id;
				}
				// 目前3w和multi都是3w
				else {
					sql = "delete from record_type3 where _id=" + detail_id;
				}
				statement.executeUpdate(sql);
			}
			
			// 删除record_star表
			sql = "delete from record_star where record_id=" + id;
			statement.executeUpdate(sql);
			// 删除record表
			sql = "delete from record where _id=" + id;
			statement.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public long queryLastStarSequence() {
		String sql = "SELECT * FROM " + TABLE_SEQUENCE + " WHERE name='" + TABLE_STAR + "'";
		Statement statement = null;
		long id = 0;
		try {
			statement = connection.createStatement();
			ResultSet set = statement.executeQuery(sql);
			if (set.next()) {
				id = set.getLong(2);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return id;
	}

	public long queryLastRecordSequence() {
		String sql = "SELECT * FROM " + TABLE_SEQUENCE + " WHERE name='" + TABLE_RECORD + "'";
		Statement statement = null;
		long id = 0;
		try {
			statement = connection.createStatement();
			ResultSet set = statement.executeQuery(sql);
			if (set.next()) {
				id = set.getLong(2);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return id;
	}

	public long queryLastRecord1v1Sequence() {
		String sql = "SELECT * FROM " + TABLE_SEQUENCE + " WHERE name='" + TABLE_RECORD_1V1 + "'";
		Statement statement = null;
		long id = 0;
		try {
			statement = connection.createStatement();
			ResultSet set = statement.executeQuery(sql);
			if (set.next()) {
				id = set.getLong(2);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return id;
	}

	public long queryLastRecord3wSequence() {
		String sql = "SELECT * FROM " + TABLE_SEQUENCE + " WHERE name='" + TABLE_RECORD_3W + "'";
		Statement statement = null;
		long id = 0;
		try {
			statement = connection.createStatement();
			ResultSet set = statement.executeQuery(sql);
			if (set.next()) {
				id = set.getLong(2);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return id;
	}

	public boolean updateRecordStar(List<RecordStar> relationList, Long recordId) {
		String sql = "DELETE FROM " + TABLE_RECORD_STAR + " WHERE record_id=" + recordId;
		// 先删除之前的
		try {
			Statement statement = connection.createStatement();
			statement.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 在插入新的
		return insertRecordStar(relationList, recordId);
	}
	
	public boolean insertRecordStar(List<RecordStar> relationList, Long recordId) {
		PreparedStatement stmt = null;
		try {
			connection.setAutoCommit(false);
			String sql = "INSERT INTO " + TABLE_RECORD_STAR + "(record_id,star_id,type,SCORE,SCORE_C) VALUES(?,?,?,?,?)";
			stmt = connection.prepareStatement(sql);
			for (RecordStar bean:relationList) {
				bean.setRecordId(recordId);
				stmt.setLong(1, bean.getRecordId());
				stmt.setLong(2, bean.getStarId());
				stmt.setInt(3, bean.getType());
				stmt.setInt(4, bean.getScore());
				stmt.setInt(5, bean.getScoreC());
				stmt.executeUpdate();
			}
			connection.commit();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public String queryVersion() {
		String sql = "SELECT value FROM " + TABLE_CONF + " WHERE key='version'";
		Statement statement = null;
		String version = null;
		try {
			statement = connection.createStatement();
			ResultSet set = statement.executeQuery(sql);
			if (set.next()) {
				version = set.getString(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return version;
	}

	public boolean updateVersion(String version) {
		String sql = "UPDATE " + TABLE_CONF + " SET value='" + version
				+ "' WHERE key='version'";
		Statement stmt = null;
		try {
			stmt = connection.createStatement();
			stmt.executeUpdate(sql);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

}
