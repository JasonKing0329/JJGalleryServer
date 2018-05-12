package com.jing.app.jjgallery.db;

import com.jing.app.jjgallery.http.bean.data.StarRating;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {

    private final String DB_PATH = "E:\\king\\Devolopment\\intelliJ_project\\JJGalleryServer\\web\\extra\\extra.db";

    public DatabaseManager() {

        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private Connection connect(String db) {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + db);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public List<StarRating> queryStarRatings(long starId) {
        List<StarRating> list = new ArrayList<>();
        Connection connection = connect(DB_PATH);
        String query = "SELECT * FROM star_rating";
        if (starId != 0) {
            query = query + " WHERE star_id=" + starId;
        }
        try {
            ResultSet set = connection.createStatement().executeQuery(query);
            while (set.next()) {
                StarRating rating = new StarRating(set.getLong(1), set.getLong(2)
                    , set.getFloat(3), set.getFloat(4), set.getFloat(5)
                    , set.getFloat(6), set.getFloat(7), set.getFloat(8)
                    , set.getFloat(9));
                list.add(rating);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void updateStarRatings(List<StarRating> ratingList) {
        if (ratingList == null || ratingList.size() == 0) {
            return;
        }
        System.out.println("updateStarRatings " + ratingList.size());
        Connection connection = connect(DB_PATH);
        try {
            // 一定要用事务，否则非常非常非常慢（比如378条数据开启事务后只用了一点几秒，不开事务则要将近一分钟）
            connection.setAutoCommit(false);

            String replace = "REPLACE INTO star_rating (_id,star_id,face,body,sexuality,dk,passion,video,complex) VALUES (?,?,?,?,?,?,?,?,?)";
            PreparedStatement replaceStmt = connection.prepareStatement(replace);
            for (StarRating bean:ratingList) {
                System.out.println("replace " + bean.getId());
                replaceStmt.setLong(1, bean.getId());
                replaceStmt.setLong(2, bean.getStarId());
                replaceStmt.setFloat(3, bean.getFace());
                replaceStmt.setFloat(4, bean.getBody());
                replaceStmt.setFloat(5, bean.getSexuality());
                replaceStmt.setFloat(6, bean.getDk());
                replaceStmt.setFloat(7, bean.getPassion());
                replaceStmt.setFloat(8, bean.getVideo());
                replaceStmt.setFloat(9, bean.getComplex());
                replaceStmt.executeUpdate();
            }

            connection.commit();
            replaceStmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
