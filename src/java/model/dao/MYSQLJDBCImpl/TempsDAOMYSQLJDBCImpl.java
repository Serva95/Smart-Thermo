package model.dao.MYSQLJDBCImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import model.dao.TempsDAO;
import model.mo.Lettura;

public class TempsDAOMYSQLJDBCImpl implements TempsDAO {
    
    Connection conn;
    
    public TempsDAOMYSQLJDBCImpl(Connection conn) {this.conn = conn;}
    
    /**
     *
     * @param today localdate esprime il giorno in cui cercare le letture
     * @return un array Lettura[] con tutte le letture di quel giorno
     */
    @Override
    public Lettura[] Readtoday(LocalDate today){
        PreparedStatement ps;
        ArrayList<Lettura> letti = new ArrayList();
        Lettura[] lettiarray;
        try {
            //WHERE date LIKE '2018-11-16%' OR date LIKE '2018-11-15%' order by date desc limit 150
            String sql = " SELECT * FROM"
                    + " (SELECT temp, hum, date FROM `temps` WHERE date LIKE ? OR date LIKE ? ORDER BY date DESC LIMIT 100)"
                    + " AS tmps ORDER BY date";
            
            ps = conn.prepareStatement(sql);
            int i = 1;
            ps.setString(i++, today.format(DateTimeFormatter.ISO_LOCAL_DATE)+"%");
            ps.setString(i++, today.minusDays(1).format(DateTimeFormatter.ISO_LOCAL_DATE)+"%");
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Lettura tmp = readtemps(rs);
                    letti.add(tmp);
                }
            }
            
            lettiarray = new Lettura[letti.size()];
            lettiarray = letti.toArray(lettiarray);
            
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return lettiarray;
    }

    @Override
    public Lettura[] Readmeds(int number_of_reads) {
        PreparedStatement ps;
        ArrayList<Lettura> letti = new ArrayList();
        Lettura[] lettiarray;
        try {
            //WHERE date LIKE '2018-11-16%' OR date LIKE '2018-11-15%' order by date desc limit 150
            String sql = " SELECT * FROM"
                    + " (SELECT mediumtemp, mediumhum, mediumDay FROM `mediums` ORDER BY mediumDay DESC LIMIT ? )"
                    + " AS tmp ORDER BY mediumDay ";
            
            ps = conn.prepareStatement(sql);
            int i = 1;
            ps.setInt(i++, number_of_reads);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Lettura tmp = readmediums(rs);
                    letti.add(tmp);
                }
            }
            lettiarray = new Lettura[letti.size()];
            lettiarray = letti.toArray(lettiarray);
            
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return lettiarray;
    }
    
    Lettura readtemps (ResultSet rs){
        Lettura lett = new Lettura();
        try {
            lett.setTemp(rs.getDouble("temp"));
        } catch (SQLException sqle) {}
        
        try {
            lett.setHum(rs.getDouble("hum"));
        } catch (SQLException sqle) {}
        
        try {
            lett.setReadingdatetime(LocalDateTime.parse(rs.getString("date").substring(0, 19).replaceFirst(" ", "T"), DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            //ofPattern("yyyy-MM-dd HH:mm:ss")
        } catch (SQLException sqle) {}
        return lett;
    }
    
    Lettura readmediums (ResultSet rs){
        Lettura lett = new Lettura();
        try {
            lett.setTemp(rs.getDouble("mediumtemp"));
        } catch (SQLException sqle) {}
        
        try {
            lett.setHum(rs.getDouble("mediumhum"));
        } catch (SQLException sqle) {}
        
        try {
            lett.setReadingdatetime(LocalDate.parse(rs.getString("mediumDay"), DateTimeFormatter.ISO_LOCAL_DATE).atStartOfDay());
            //ofPattern("yyyy-MM-dd HH:mm:ss")
        } catch (SQLException sqle) {}
        return lett;
    }
    
}
