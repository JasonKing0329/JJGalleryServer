package com.king.desk.gdata.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.king.desk.gdata.Conf;
import com.king.desk.gdata.Constants;
import com.king.desk.gdata.Constants.CommonColumn;
import com.king.desk.gdata.model.bean.ColumnBean;
import com.king.desk.gdata.model.bean.SaveData;
import com.king.desk.gdata.model.bean.TableItem;
import com.king.service.gdb.bean.GDBProperites;
import com.king.service.gdb.bean.Record;
import com.king.service.gdb.bean.RecordStar;
import com.king.service.gdb.bean.RecordType1v1;
import com.king.service.gdb.bean.RecordType3w;
import com.king.service.gdb.bean.Star;

public class TableModel {

    /**
     * 按照实际数据结构，id和name都可以标志唯一性
     */
    private Map<Long, Star> starIdMap;
    private Map<String, Star> starNameMap;

    /**
     * 待更新统计的（如果修改了star，那么也要修改star的统计字段）
     */
    protected Map<String, Star> mToUpdateStarMap;

    protected String[] videoTypes = new String[]{
            ".mp4", ".wmv", ".mkv", ".avi", ".mov", ".flv", ".rmvb", ".mpg"
    };

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private SaveData mSaveData;

    private Properties versionProperties;

    public TableModel() {
        starIdMap = new HashMap<>();
        starNameMap = new HashMap<>();
        mToUpdateStarMap = new HashMap<>();
    }

    public List<ColumnBean> createColumnList(String type) {
        List<ColumnBean> list = getBasics();
//		if (Constants.source[1].equals(type)
//				|| Constants.source[2].equals(type)) {
//			list.addAll(getSingle());
//		}
//		else if (Constants.source[3].equals(type)) {
//			list.addAll(get3());
//		}
        return list;
    }

    public List<ColumnBean> getBasics() {
        List<ColumnBean> list = new ArrayList<>();
        list.add(new ColumnBean("序号", 40));
        list.add(new ColumnBean("场景", 60));
        list.add(new ColumnBean("目录", 200));
        list.add(new ColumnBean("文件名", 200));
        list.add(new ColumnBean("Top", 80));
        list.add(new ColumnBean("Bottom", 80));
        list.add(new ColumnBean("Mix", 80));
        list.add(new ColumnBean("HD", 30));
        list.add(new ColumnBean("总分", 40));
        list.add(new ColumnBean("整体", 40));
        list.add(new ColumnBean("主角", 40));
        list.add(new ColumnBean("Passion", 40));
        list.add(new ColumnBean("Body", 40));
        list.add(new ColumnBean("Cock", 40));
        list.add(new ColumnBean("Ass", 40));
        list.add(new ColumnBean("Cum", 40));
        list.add(new ColumnBean("Special", 40));
        list.add(new ColumnBean("Bareback", 40));
        list.add(new ColumnBean("Deprecated", 40));
        list.add(new ColumnBean("Special Description", 100));
        list.add(new ColumnBean("Score Top", 40));
        list.add(new ColumnBean("Score Bottom", 40));
        list.add(new ColumnBean("Score Mix", 40));
        list.add(new ColumnBean("ScoreC Top", 40));
        list.add(new ColumnBean("ScoreC Bottom", 40));
        list.add(new ColumnBean("ScoreC Mix", 40));
        list.add(new ColumnBean("Date", 80));
        list.add(new ColumnBean("Type", 40));
        list.add(new ColumnBean("Fk", 200));
        list.add(new ColumnBean("Other", 200));
        return list;
    }

    public void updateTableItem(TableItem item, int col, String value) {
        item.setBasicChanged(true);
        if (col < CommonColumn.values().length) {
            switch (Constants.convert(col)) {
                case SEQ:// 序号由列表所在row决定，不允许编辑
                case DATE:// 日期读取文件的lastModifyTime，不允许编辑
                    break;
                case SCENE:
                    item.getBean().setScene(value);
                    break;
                case DIR:
                    // 替换\为/
                    value = value.replaceAll("\\\\", "/");
                    item.getBean().setDirectory(value);
                    break;
                case NAME:
                    item.getBean().setName(value);
                    break;
                case HD:
                    try {
                        item.getBean().setHdLevel(Integer.parseInt(value));
                    } catch (Exception e) {
                    }
                    break;
                case SCORE:
                    try {
                        item.getBean().setScore(Integer.parseInt(value));
                    } catch (Exception e) {
                    }
                    break;
                case SCORE_FEEL:
                    try {
                        item.getBean().setScoreFeel(Integer.parseInt(value));
                    } catch (Exception e) {
                    }
                    countScoreTotal(item);
                    break;
                case STAR:
                    try {
                        item.getBean().setScoreStar(Integer.parseInt(value));
                    } catch (Exception e) {
                    }
                    countScoreTotal(item);
                    break;
                case PASSION:
                    try {
                        item.getBean().setScorePassion(Integer.parseInt(value));
                    } catch (Exception e) {
                    }
                    countScoreTotal(item);
                    break;
                case BODY:
                    try {
                        item.getBean().setScoreBody(Integer.parseInt(value));
                    } catch (Exception e) {
                    }
                    countScoreTotal(item);
                    break;
                case COCK:
                    try {
                        item.getBean().setScoreCock(Integer.parseInt(value));
                    } catch (Exception e) {
                    }
                    countScoreTotal(item);
                    break;
                case ASS:
                    try {
                        item.getBean().setScoreAss(Integer.parseInt(value));
                    } catch (Exception e) {
                    }
                    countScoreTotal(item);
                    break;
                case CUM:
                    try {
                        item.getBean().setScoreCum(Integer.parseInt(value));
                    } catch (Exception e) {
                    }
                    countScoreTotal(item);
                    break;
                case SPECIAL:
                    try {
                        item.getBean().setScoreSpecial(Integer.parseInt(value));
                    } catch (Exception e) {
                    }
                    countScoreTotal(item);
                    break;
                case BAREBACK:
                    try {
                        item.getBean().setScoreBareback(Integer.parseInt(value));
                    } catch (Exception e) {
                    }
                    countScoreTotal(item);
                    break;
                case DEPRECATED:
                    try {
                        item.getBean().setDeprecated(Integer.parseInt(value));
                    } catch (Exception e) {
                    }
                    break;
                case SPECIAL_DESC:
                    item.getBean().setSpecialDesc(value);
                    break;

                // 下面这些元素有一一对应的关系，直接改单个元素非常麻烦，这里不做处理，在最后更新到数据库的时候重新对整体进行处理
                // 这里只保存临时的显示字符串
                case TOP:
                    item.setTop(value);
                    break;
                case BOTTOM:
                    item.setBottom(value);
                    break;
                case MIX:
                    item.setMix(value);
                    break;
                case SCORE_TOP:
                    item.setScoreTop(value);
                    break;
                case SCORE_BOTTOM:
                    item.setScoreBottom(value);
                    break;
                case SCORE_MIX:
                    item.setScoreMix(value);
                    break;
                case SCORE_C_TOP:
                    item.setScoreCTop(value);
                    break;
                case SCORE_C_BOTTOM:
                    item.setScoreCBottom(value);
                    break;
                case SCORE_C_MIX:
                    item.setScoreCMix(value);
                    break;
                default:
                    break;
            }
        }
    }

    private void countScoreTotal(TableItem item) {
        Record bean = item.getBean();
        int count = bean.getScoreFeel() + bean.getScoreStar() + bean.getScorePassion()
                + bean.getScoreCum() + bean.getScoreSpecial() + bean.getScoreBareback()
                + bean.getScoreBody() + bean.getScoreCock() + bean.getScoreAss();
        bean.setScore(count);
    }

    public void formatDate(TableItem item) {
        if (item.getBean().getLastModifyTime() > 0) {
            item.setDate(dateFormat.format(new Date(item.getBean().getLastModifyTime())));
        } else {
            item.setDate("");
        }
    }

    public void formatStarString(TableItem item) {
        List<RecordStar> stars = item.getStars();
        if (stars != null) {
            String top = "";
            String bottom = "";
            String mix = "";
            String topScore = "";
            String bottomScore = "";
            String mixScore = "";
            String topScoreC = "";
            String bottomScoreC = "";
            String mixScoreC = "";
            for (RecordStar rs : stars) {
                if (rs.getType() == GDBProperites.VALUE_RELATION_TOP) {
                    if (rs.getStar() != null) {
                        top = setOrConcat(top, rs.getStar().getName());
                    }
                    if (rs.getScore() > 0) {
                        topScore = setOrConcat(topScore, String.valueOf(rs.getScore()));
                    }
                    if (rs.getScoreC() > 0) {
                        topScoreC = setOrConcat(topScoreC, String.valueOf(rs.getScoreC()));
                    }
                } else if (rs.getType() == GDBProperites.VALUE_RELATION_BOTTOM) {
                    if (rs.getStar() != null) {
                        bottom = setOrConcat(bottom, rs.getStar().getName());
                    }
                    if (rs.getScore() > 0) {
                        bottomScore = setOrConcat(bottomScore, String.valueOf(rs.getScore()));
                    }
                    if (rs.getScoreC() > 0) {
                        bottomScoreC = setOrConcat(bottomScoreC, String.valueOf(rs.getScoreC()));
                    }
                } else if (rs.getType() == GDBProperites.VALUE_RELATION_MIX) {
                    if (rs.getStar() != null) {
                        mix = setOrConcat(mix, rs.getStar().getName());
                    }
                    if (rs.getScore() > 0) {
                        mixScore = setOrConcat(mixScore, String.valueOf(rs.getScore()));
                    }
                    if (rs.getScoreC() > 0) {
                        mixScoreC = setOrConcat(mixScoreC, String.valueOf(rs.getScoreC()));
                    }
                }
            }
            item.setTop(top);
            item.setBottom(bottom);
            item.setMix(mix);
            item.setScoreTop(topScore);
            item.setScoreBottom(bottomScore);
            item.setScoreMix(mixScore);
            item.setScoreCTop(topScoreC);
            item.setScoreCBottom(bottomScoreC);
            item.setScoreCMix(mixScoreC);
        }
    }

    private String setOrConcat(String src, String value) {
        if (src == null || src.length() == 0) {
            return value;
        } else {
            return src + "," + value;
        }
    }

    public List<Record> queryAllRecords() {
        return SqlInstance.get().getDao().queryRecords();
    }

    public List<RecordStar> getRecordStars(Long recordId) {
        List<RecordStar> stars = SqlInstance.get().getDao().getRecordStars(recordId);
        for (RecordStar rs : stars) {
            rs.setStar(starIdMap.get(rs.getStarId()));
        }
        return stars;
    }

    public void loadStarMap() {
        starIdMap = SqlInstance.get().getDao().queryStarMaps();
        Collection<Star> collection = starIdMap.values();
        for (Star star : collection) {
            starNameMap.put(star.getName(), star);
        }
    }

    public void saveTable(SaveData saveData) throws Exception {
        mSaveData = saveData;
        // 保存更新逻辑，由于涉及到多张关联表并且各种没有id的情况，太过复杂，按照修改的部分，依次作为事务提交
        List<TableItem> basicUpdateList = new ArrayList<>();
        List<TableItem> starUpdateList = new ArrayList<>();
        List<TableItem> detailUpdateList = new ArrayList<>();
        for (int i = 0; i < saveData.getList().size(); i++) {
            TableItem item = saveData.getList().get(i);
            // 检查是否有detail为null的情况
            // 有些老数据detail为null但是type已设置过，略过
            if (item.getDetail() == null && item.getBean().getType() == 0) {
                String message = "序号[" + (i + 1) + "]缺少Detail数据";
                throw new Exception(message);
            }

            String logMsg = "序号" + (item.getIndexInTotalList() + 1) + ", 名称" + item.getBean().getName();
            if (item.isBasicChanged()) {
                DebugLog.e(logMsg + " <--> isBasicChanged");
                basicUpdateList.add(item);
            }
            if (item.isStarChanged()) {
                DebugLog.e(logMsg + " <--> isStarChanged");
                starUpdateList.add(item);
            }
            if (item.isDetailChanged()) {
                DebugLog.e(logMsg + " <--> isDetailChanged");
                detailUpdateList.add(item);
            }
        }
        saveBasicUpdate(basicUpdateList);
        saveStarUpdate(starUpdateList);
        saveDetailUpdate(detailUpdateList);
        updateStarCount();
        saveVersion();
    }

    private void saveVersion() {
        if (mSaveData.getVersion() != null && mSaveData.getVersion().length() > 0) {
            versionProperties = new Properties();
            try {
                versionProperties.load(new FileInputStream(Conf.WEB_PROPERTIES));
            } catch (IOException e) {
                e.printStackTrace();
            }
            versionProperties.setProperty("gdb_db_version", mSaveData.getVersion());
            try {
                versionProperties.store(new FileOutputStream(Conf.WEB_PROPERTIES), "Update");
            } catch (IOException e) {
                e.printStackTrace();
            }
            SqlInstance.get().getDao().updateVersion(mSaveData.getVersion());
        }
    }

    private void saveBasicUpdate(List<TableItem> list) throws Exception {
        SqlInstance.get().getDao().beginTransaction();
        long recordId = SqlInstance.get().getDao().queryLastRecordSequence();
        for (TableItem item : list) {
            if (item.getBean().getId() == null) {
                // 处于事务中，需要手动设置ID
                SqlInstance.get().getDao().insertRecord(item.getBean());
                item.getBean().setId(++recordId);
            } else {
                // 修改过的数据
                if (item.isDeleted()) {
                    // 标记关联star需要重新统计
                    if (item.getStars() != null) {
                        for (RecordStar rs : item.getStars()) {
                            mToUpdateStarMap.put(rs.getStar().getName(), rs.getStar());
                        }
                    }
                    SqlInstance.get().getDao().deleteRecord(item.getBean().getId());
                } else {
                    SqlInstance.get().getDao().updateRecord(item.getBean());
                }
            }
        }
        SqlInstance.get().getDao().commit();
    }

    private void saveStarUpdate(List<TableItem> list) throws Exception {
        SqlInstance.get().getDao().beginTransaction();
        long starId = SqlInstance.get().getDao().queryLastStarSequence();
        for (TableItem item : list) {
            if (item.getStars() == null) {
                continue;
            }
            List<RecordStar> orginStars = SqlInstance.get().getDao().getRecordStars(item.getBean().getId());
            // 之前关联的通知要重新统计
            for (RecordStar rs : orginStars) {
                Star star = starIdMap.get(rs.getStarId());
                mToUpdateStarMap.put(star.getName(), star);
            }
            // 内存中最后存在的，由于界面只支持新增和删除，如果关系中有新增的star要关联上starId
            for (RecordStar rs : item.getStars()) {
                Star star;
                if (rs.getId() == null) {
                    star = getStar(rs.getStar().getName());
                    rs.setStar(star);
                    rs.setRecordId(item.getBean().getId());
                    // 当前内存中没有找到star，属于新增，插入star到数据库
                    if (star.getId() == null) {
                        SqlInstance.get().getDao().insertStar(star);
                        star.setId(++starId);
                    }
                    rs.setStarId(star.getId());
                }
                else {
                    star = rs.getStar();
                }
                mToUpdateStarMap.put(star.getName(), star);
            }
            SqlInstance.get().getDao().updateRecordStar(item.getStars(), item.getBean().getId());
        }
        SqlInstance.get().getDao().commit();
    }

    private void saveDetailUpdate(List<TableItem> list) throws Exception {
        SqlInstance.get().getDao().beginTransaction();
        long record1v1Id = SqlInstance.get().getDao().queryLastRecord1v1Sequence();
        long record3wId = SqlInstance.get().getDao().queryLastRecord3wSequence();
        for (TableItem item : list) {
            if (item.getDetail() instanceof RecordType1v1) {
                if (item.getDetail().getId() == null) {
                    // insert 1v1
                    SqlInstance.get().getDao().insertRecord1v1((RecordType1v1) item.getDetail());
                    item.getDetail().setId(++record1v1Id);

                    // update detailId for record
                    item.getBean().setRecordDetailId(item.getDetail().getId());
                    SqlInstance.get().getDao().updateRecord(item.getBean());
                } else {
                    // update 1v1
                    SqlInstance.get().getDao().updateRecord1v1((RecordType1v1) item.getDetail());
                }
            } else if (item.getDetail() instanceof RecordType3w) {
                if (item.getDetail().getId() == null) {
                    // insert 1v1
                    SqlInstance.get().getDao().insertRecord3W((RecordType3w) item.getDetail());
                    item.getDetail().setId(++record3wId);

                    // update detailId for record
                    item.getBean().setRecordDetailId(item.getDetail().getId());
                    SqlInstance.get().getDao().updateRecord(item.getBean());
                } else {
                    // update 3w
                    SqlInstance.get().getDao().updateRecord3W((RecordType3w) item.getDetail());
                }
            }
        }
        SqlInstance.get().getDao().commit();
    }

    private void updateStarCount() throws Exception {
        SqlInstance.get().getDao().beginTransaction();
        for (Star star : mToUpdateStarMap.values()) {
            SqlInstance.get().getDao().updateCountStar(star, mSaveData.isDeleteStarWithoutRecords());
        }
        SqlInstance.get().getDao().commit();
    }

    private Star getStar(String name) {
        Star star = starNameMap.get(name);
        if (star == null) {
            star = new Star();
            star.setName(name);
            starNameMap.put(name, star);
        }
        return star;
    }

    public RecordType1v1 loadRecordType1v1(Long id) {
        return SqlInstance.get().getDao().queryRecordType1v1(id);
    }

    public RecordType3w loadRecordType3w(Long id) {
        return SqlInstance.get().getDao().queryRecordType3w(id);
    }

    /**
     * 获取文件最近修改时间
     *
     * @param item
     */
    public long readRecordModifyTime(TableItem item) {
        long time = 0;
        Record record = item.getBean();
        String path = record.getDirectory() + "/" + record.getName();
        for (String extras : videoTypes) {
            String realPath = path + extras;
            File file = new File(realPath);
            if (file.exists()) {
                time = file.lastModified();
                break;
            }
        }
        return time;
    }

    public String getVersion() {
        return SqlInstance.get().getDao().queryVersion();
    }

}
