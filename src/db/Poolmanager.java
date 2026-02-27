package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ArrayBlockingQueue;

public class Poolmanager {
    private ArrayBlockingQueue<Connection> pool;

    public Poolmanager(String url, String user, String pass, int size) throws SQLException {
        pool = new ArrayBlockingQueue<>(size);
        for (int i = 0; i < size; i++) {
            pool.add(DriverManager.getConnection(url, user, pass));
        }
    }

    public Connection getConnection() throws InterruptedException { return pool.take(); }
    public void releaseConnection(Connection conn) { pool.offer(conn); }
}