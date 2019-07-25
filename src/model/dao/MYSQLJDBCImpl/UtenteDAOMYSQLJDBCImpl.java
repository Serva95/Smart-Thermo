package model.dao.MYSQLJDBCImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.mo.Utente;
import model.dao.UtenteDAO;
import model.dao.exception.DuplicatedObjectException;

public class UtenteDAOMYSQLJDBCImpl implements UtenteDAO {
    
    private final String CODICE = "cod_user";
    Connection conn;
    
    public UtenteDAOMYSQLJDBCImpl(Connection conn) {this.conn = conn;}
    
    @Override
    public Utente insert(Utente toinsert) throws DuplicatedObjectException {
        PreparedStatement ps;
        Utente user = new Utente();
        user.setEmail(toinsert.getEmail());
        user.setUsername(toinsert.getUsername());
        user.setPassword(toinsert.getPassword());
        try {
            String sql = " SELECT codice "
                    + " FROM utente "
                    + " WHERE "
                    + " deleted ='0' AND "
                    + " email = ? AND "
                    + " username = ?";
            
            ps = conn.prepareStatement(sql);
            int i = 1;
            ps.setString(i++, user.getEmail());
            ps.setString(i++, user.getUsername());
            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    throw new DuplicatedObjectException("UtenteDAOJDBCImpl.create: Tentativo di inserimento di un utente già esistente.");
                }
            }
            
            sql     = "UPDATE counter "
                    + " SET counterValue=counterValue+1"
                    + " WHERE counterId='" + CODICE + "'";
            
            ps = conn.prepareStatement(sql);
            ps.executeUpdate();
            
            sql     = "SELECT counterValue "
                    + " FROM counter "
                    + " WHERE counterId='" + CODICE + "'";
            
            ps = conn.prepareStatement(sql);
            try (ResultSet resultSet = ps.executeQuery()) {
                resultSet.next();
                user.setCodice(resultSet.getLong("counterValue"));
            }
            
            sql     = " INSERT INTO utente "
                    + "( codice, "
                    + "email,"
                    + "username,"
                    + "password,"
                    + "deleted )"
                    + " VALUES (?,?,?,?,0)";
            
            ps = conn.prepareStatement(sql);
            i = 1;
            ps.setLong(i++, user.getCodice());
            ps.setString(i++, user.getEmail());
            ps.setString(i++, user.getUsername());
            ps.setString(i++, user.getPassword());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return user;
    }
    
    @Override
    public void update(Utente user) throws DuplicatedObjectException {
        PreparedStatement ps;
        try {
            String sql;
            int i;
            
            sql     = " SELECT codice "
                    + " FROM utente "
                    + " WHERE "
                    + " deleted ='0' AND "
                    + " email = ? "
                    + " AND codice != ?";
            
            ps = conn.prepareStatement(sql);
            i = 1;
            ps.setString(i++, user.getEmail());
            ps.setLong(i++, user.getCodice());
            System.out.println(ps);
            boolean exist;
            try (ResultSet resultSet = ps.executeQuery()) {
                exist = resultSet.next();
            }
            if (exist) {
                throw new DuplicatedObjectException("UtenteDAOJDBCImpl.update: Tentativo di aggiornamento in un utente già esistente.");
            }
            sql     = " UPDATE utente "
                    + " SET "
                    + "     codice = ?, "
                    + "     email = ?, "
                    + "     username = ?,"
                    + "     password = ?,"
                    + "     deleted = ?"
                    + " WHERE codice = ? ";
            
            ps = conn.prepareStatement(sql);
            i = 1;
            ps.setLong(i++, user.getCodice());
            ps.setString(i++, user.getEmail());
            ps.setString(i++, user.getUsername());
            ps.setString(i++, user.getPassword());
            ps.setInt(i++, 0);
            ps.setLong(i++, user.getCodice());
            ps.executeUpdate();
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public void delete(Utente user) {
        PreparedStatement ps;
        
        try {
            String sql = " UPDATE utente "
                    + " SET deleted='1' "
                    + " WHERE "
                    + " codice=?";
            
            ps = conn.prepareStatement(sql);
            ps.setLong(1, user.getCodice());
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public Utente findByUserMail (String email){
        PreparedStatement ps;
        Utente user = new Utente();
        /*try {
        
        String sql
        = "SELECT * "
        + "FROM utente "
        + "WHERE "
        + "email = ? AND "
        + "deleted = '0' ";
        
        ps = conn.prepareStatement(sql);
        ps.setString(1, email);
        try (ResultSet resultSet = ps.executeQuery()) {
        if (resultSet.next()) {
        user = read(resultSet);
        }
        }
        ps.close();
        } catch (SQLException e) {
        throw new RuntimeException(e);
        }*/
        user.setCodice(1);
        user.setDeleted(false);
        user.setUsername("alberto12");
        user.setEmail("aa@bb.com");
        user.setPassword("284DA789DBD5955393DEE8D7090ED25550E7D7E3A9295F76926B194421622CC4F4E7911B18957F47D0E32CDFE3C6BD5C0B0F8C76F650D6F6F4985BD0B81A7623");
        return user;
    }
    
    @Override
    public Utente findByUserCodice (long codice){
        PreparedStatement ps;
        Utente user = null;
        try {
            String sql = "SELECT * "
                    + "FROM utente "
                    + "WHERE "
                    + "codice = ? ";
            
            ps = conn.prepareStatement(sql);
            ps.setLong(1, codice);
            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    user = read(resultSet);
                }
            }
            ps.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return user;
    }

    @Override
    public Utente findByUsername (String username){
        PreparedStatement ps;
        Utente user = null;
        try {
            String sql = "SELECT * "
                    + "FROM utente "
                    + "WHERE "
                    + "username = ? ";

            ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    user = read(resultSet);
                }
            }
            ps.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return user;
    }
    
    Utente read (ResultSet rs){
        
        Utente user = new Utente();
        try {
            user.setCodice(rs.getLong("codice"));
        }catch (SQLException sqle){}
        
        try {
            user.setEmail(rs.getString("email"));
        } catch (SQLException sqle) {
        }
        try {
            user.setUsername(rs.getString("username"));
        } catch (SQLException sqle) {
        }
        try {
            user.setPassword(rs.getString("password"));
        } catch (SQLException sqle) {
        }
        try {
            user.setDeleted(rs.getString("deleted").equals("Y"));
        } catch (SQLException sqle) {
        }
        return user;
    }
    
}
