package model.dao.MYSQLJDBCImpl;

import backgrounder.Variabili;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
            String sql  = " INSERT INTO temps "
                        + "( temp, hum ) "
                        + "VALUES (? , ?)";
            
            ps = conn.prepareStatement(sql);
            int i = 1;
            ps.setDouble(i++, var.getActualtempdbl());
            ps.setDouble(i++, var.getActualhumdbl());
            ps.executeUpdate();
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
