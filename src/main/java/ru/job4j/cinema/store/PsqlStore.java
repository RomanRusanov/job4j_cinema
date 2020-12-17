package ru.job4j.cinema.store;

import org.apache.commons.dbcp2.BasicDataSource;
import ru.job4j.cinema.model.Place;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author Roman Rusanov
 * @version 0.1
 * @since 05.11.2020
 * email roman9628@gmail.com
 * The class describe work with db.
 */
public class PsqlStore {
    /**
     * The instance provide connection to db what not
     * create new instance each time, reuse connection.
     * Pool. Each time call connection.close() connection
     * return to pool.
     */
    private final BasicDataSource pool = new BasicDataSource();

    /**
     * The default constructor.
     */
    private PsqlStore() {
        Properties cfg = new Properties();
        try (BufferedReader io = new BufferedReader(
                new FileReader("cinema_db.properties")
        )) {
            cfg.load(io);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        try {
            Class.forName(cfg.getProperty("jdbc.driver"));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        pool.setDriverClassName(cfg.getProperty("jdbc.driver"));
        pool.setUrl(cfg.getProperty("jdbc.url"));
        pool.setUsername(cfg.getProperty("jdbc.username"));
        pool.setPassword(cfg.getProperty("jdbc.password"));
        pool.setMinIdle(5);
        pool.setMaxIdle(10);
        pool.setMaxOpenPreparedStatements(100);
    }

    /**
     * The inner class guarantees that only one instance is initialized.
     */
    private static final class Lazy {
        private static final PsqlStore INST = new PsqlStore();
    }

    /**
     * The method create and get instance PsqlStore.
     * @return PsqlStore.
     */
    public static PsqlStore instOf() {
        return Lazy.INST;
    }

    /**
     * The method get list with all occupied places.
     * @return List place instance.
     */
    public List<Place> getAllOccupiedPlaces() {
        ArrayList<Place> result = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement(
                     "SELECT row, place FROM hall where free = false;")
        ) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    result.add(new Place(
                            it.getInt("row"),
                            it.getInt("place"))
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * The method add user to DB.
     * @param fio String first name, last name
     * @param phone String phone number.
     * @return long id key from table account.
     */
    public long addUserToAccount(String fio, String phone) {
        long result = -1;
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement(
                     "insert into accounts (fio, phone) VALUES (?, ?);",
                     Statement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, fio);
            ps.setString(2, phone);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys(); ) {
                if (rs.next()) {
                    result = rs.getLong(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * The method update table hall column free to false and id to userId how buy ticket.
     * @param userId long userId
     * @param hallId int hallId
     * @param row int row
     * @param place int number place in row
     * @return if update return true, otherwise false.
     */
    public boolean placeIsTaken(long userId, int hallId, int row, int place) {
        boolean result = false;
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement(
                     "update hall set free = false, account_id = ?, hall_id = ? where row = ? and place = ?;")
        ) {
            ps.setLong(1, userId);
            ps.setInt(2, hallId);
            ps.setInt(3, row);
            ps.setInt(4, place);
            if (ps.executeUpdate() > 0) {
                result = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * The method update table hall. All records with free equals false change for true.
     * @param hallId int hallId
     */
    public void clearAllTakenPlace(int hallId) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement(
                     "update hall set free = true  where hall_id = ? and free = false;")
        ) {
            ps.setInt(1, hallId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}