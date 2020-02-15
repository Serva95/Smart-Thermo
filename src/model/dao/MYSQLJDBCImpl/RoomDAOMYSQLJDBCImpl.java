package model.dao.MYSQLJDBCImpl;

import model.dao.RoomDAO;
import model.dao.exception.DuplicatedObjectException;
import model.mo.Stanza;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

public class RoomDAOMYSQLJDBCImpl implements RoomDAO {

    public RoomDAOMYSQLJDBCImpl(Connection conn) {this.conn = conn;}
    Connection conn;
    private final String CODICE = "cod_room";

    @Override
    public Stanza insert(Stanza stanza) throws DuplicatedObjectException {
        PreparedStatement ps;
        String sql;
        try {
            sql = " SELECT id "
                    + " FROM rooms "
                    + " WHERE "
                    + " nome = ?";

            ps = conn.prepareStatement(sql);
            int i = 1;
            ps.setString(i++, stanza.getNome());
            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    throw new DuplicatedObjectException("RoomDAOJDBCImpl.insert: Tentativo di inserimento di una stanza già esistente.");
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
                stanza.setId(resultSet.getInt("counterValue"));
            }

            sql     = " INSERT INTO rooms "
                    + "( id, "
                    + "nome,"
                    + "orarioInizio,"
                    + "orarioFine,"
                    + "maxTemp,"
                    + "minTemp,"
                    + "absoluteMin )"
                    + " VALUES (?,?,?,?,?,?,?)";

            ps = conn.prepareStatement(sql);
            i = 1;
            ps.setInt(i++, stanza.getId());
            ps.setString(i++, stanza.getNome());
            ps.setString(i++, Arrays.toString(stanza.getTurnOnTimes()));
            ps.setString(i++, Arrays.toString(stanza.getTurnOffTimes()));
            ps.setDouble(i++, stanza.getMaxTemp());
            ps.setDouble(i++, stanza.getMinTemp());
            ps.setDouble(i++, stanza.getAbsoluteMin());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return stanza;
    }

    @Override
    public void update(Stanza stanza) {
        PreparedStatement ps;

    }

    @Override
    public void delete(Stanza stanza) {
        PreparedStatement ps;

    }

    @Override
    public Stanza[] findAllRooms() {
        PreparedStatement ps;
        return new Stanza[0];
    }
}
