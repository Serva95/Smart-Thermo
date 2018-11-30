package model.dao.MYSQLJDBCImpl;

import backgrounder.Variabili;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import model.dao.BackgroundDAO;
import model.dao.exception.DuplicatedObjectException;

public class BackgroundDAOMYSQLJDBCImpl implements BackgroundDAO {
    
    private final String CODICE = "cod_user";
    Connection conn;
    
    public BackgroundDAOMYSQLJDBCImpl(Connection conn) {this.conn = conn;}
    
    @Override
    public boolean insert(Variabili var) throws DuplicatedObjectException {
        PreparedStatement ps;
        try {
            String sql     = " INSERT INTO temps "
                    + "( temp, hum ) VALUES (? , ?)";
            
            ps = conn.prepareStatement(sql);
            int i = 1;
            ps.setDouble(i++, var.getActualtempdbl());
            ps.setDouble(i++, var.getActualhumdbl());
            ps.executeUpdate();
            
            sql = "SELECT * FROM mediums WHERE mediumDay = ?";
            ps = conn.prepareStatement(sql);
            i = 1;
            ps.setString(i++, LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE));
            boolean exist;
            double tottmp = var.getActualtempdbl();
            double tothum = var.getActualhumdbl();
            double mediumtmp;
            double mediumhum;
            int measnumb;
            try (ResultSet rs = ps.executeQuery()) {
                exist = rs.next();
                if (exist) {
                    mediumtmp = rs.getDouble("mediumtemp");
                    mediumhum = rs.getDouble("mediumhum");
                    measnumb = rs.getInt("measurenumber");
                    tottmp += mediumtmp * measnumb;
                    tothum += mediumhum * measnumb;
                    measnumb++;
                    mediumtmp = (Math.round((tottmp/measnumb)*10))/10.0;
                    mediumhum = (Math.round((tothum/measnumb)*10))/10.0;
                    
                    sql     = " UPDATE mediums SET"
                            + " mediumtemp = ?,"
                            + " mediumhum = ?,"
                            + " measurenumber = ?"
                            + " WHERE mediumDay = ? ";
                    
                    ps = conn.prepareStatement(sql);
                    i = 1;
                    ps.setDouble(i++, mediumtmp);
                    ps.setDouble(i++, mediumhum);
                    ps.setInt(i++, measnumb);
                    ps.setString(i++, LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE));
                    ps.executeUpdate();
                }else{
                    sql     = " INSERT INTO mediums "
                            + " (mediumtemp, mediumhum, measurenumber, mediumDay) "
                            + " VALUES (? , ? , ? , ?) ";
                    
                    ps = conn.prepareStatement(sql);
                    i = 1;
                    ps.setDouble(i++, var.getActualtempdbl());
                    ps.setDouble(i++, var.getActualhumdbl());
                    ps.setInt(i++, 1);
                    ps.setString(i++, LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE));
                    ps.executeUpdate();
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return true;
    }
    
    Variabili read (ResultSet rs){
        Variabili var = new Variabili();
        
        try {
            var.setActualtemp(rs.getString("temp"));
        } catch (SQLException sqle) {}
        return var;
    }
    
}
