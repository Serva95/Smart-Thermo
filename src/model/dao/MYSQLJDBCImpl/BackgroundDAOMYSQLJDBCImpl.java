package model.dao.MYSQLJDBCImpl;

import backgrounder.Variabili;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import model.dao.BackgroundDAO;
import model.dao.exception.DuplicatedObjectException;
import model.mo.Lettura;

public class BackgroundDAOMYSQLJDBCImpl implements BackgroundDAO {
    
    private final String CODICE = "cod_user";
    Connection conn;
    
    public BackgroundDAOMYSQLJDBCImpl(Connection conn) {this.conn = conn;}
    
    @Override
    public boolean insert(Variabili var) throws DuplicatedObjectException {
        PreparedStatement ps;
        try {
            String sql  = " INSERT INTO temps "
                        + "( temp, hum ) "
                        + "VALUES (? , ?)";
            
            ps = conn.prepareStatement(sql);
            int i = 1;
            ps.setDouble(i++, var.getLettura().getTemp());
            ps.setDouble(i++, var.getLettura().getHum());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    @Override
    public boolean medify(LocalDate day) {
        PreparedStatement ps;
        Lettura media = new Lettura();
        short rowCount = 0;
        int i;
        String sql;
        boolean operationDone = false;
        try {
            sql = "SELECT count(temp) AS temp FROM temps WHERE date LIKE ? ";
            ps = conn.prepareStatement(sql);
            i = 1;
            ps.setString(i++, day.toString() + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    rowCount = rs.getShort("temp");
                }
            }

            if(rowCount >1) {
                sql = "SELECT AVG(temp) AS temp, AVG(hum) AS hum, MIN(date) AS date FROM temps WHERE date LIKE ? ";
                ps = conn.prepareStatement(sql);
                i = 1;
                ps.setString(i++, day.toString() + "%");

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        media = readmediums(rs);
                    }
                }

                sql  = " DELETE FROM temps "
                        + "WHERE date like ?";

                ps = conn.prepareStatement(sql);
                i = 1;
                ps.setString(i++, day.toString() + "%");
                ps.execute();

                sql  = " INSERT INTO temps "
                        + "( temp, hum, date ) "
                        + "VALUES (? , ?, ?)";

                ps = conn.prepareStatement(sql);
                i = 1;
                ps.setDouble(i++, media.getTemp());
                ps.setDouble(i++, media.getHum());
                ps.setString(i++, media.getReadingdatetime().toString());
                ps.executeUpdate();

                operationDone = true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return operationDone;
    }

    Variabili read (ResultSet rs){
        Variabili var = new Variabili();
        
        try {
            var.setActualtemp(rs.getString("temp"));
        } catch (SQLException sqle) {}
        return var;
    }

    Lettura readmediums (ResultSet rs){
        Lettura lett = new Lettura();
        try {
            lett.setTemp((double)Math.round(rs.getDouble("temp")*100)/100);
        } catch (SQLException sqle) {}

        try {
            lett.setHum((double)Math.round(rs.getDouble("hum")*100)/100);
        } catch (SQLException sqle) {}

        LocalDateTime tmp = null;
        try {
            tmp = LocalDateTime.parse(rs.getString("date").substring(0, 19), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        } catch (SQLException sqle) {}
        lett.setReadingdatetime(tmp);

        return lett;
    }
}
