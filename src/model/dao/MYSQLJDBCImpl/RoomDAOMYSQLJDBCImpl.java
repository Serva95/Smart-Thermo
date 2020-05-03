package model.dao.MYSQLJDBCImpl;

import model.dao.RoomDAO;
import model.dao.exception.DuplicatedObjectException;
import model.mo.Lettura;
import model.mo.Stanza;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
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
                    throw new DuplicatedObjectException("Esiste già una stanza con questo nome, provane uno diverso");
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
                    + "maxTemp,"
                    + "minTemp,"
                    + "absoluteMin )"
                    + " VALUES (?,?,?,?,?)";

            ps = conn.prepareStatement(sql);
            i = 1;
            ps.setInt(i++, stanza.getId());
            ps.setString(i++, stanza.getNome());
            ps.setDouble(i++, stanza.getMaxTemp());
            ps.setDouble(i++, stanza.getMinTemp());
            ps.setDouble(i++, stanza.getAbsoluteMin());
            ps.executeUpdate();

            int day = 0;
            for(LocalTime[] localTimes : stanza.getTurnOnOffTimes()){
                int fascia = 1;
                for(int cnt=0; cnt<6; cnt++){
                    if(localTimes[cnt]!=null) {
                        sql = " INSERT INTO orarionoff "
                                + "( roomID, "
                                + "giorno,"
                                + "fascia,"
                                + "orarioAccensione,"
                                + "orarioSpegnimento )"
                                + " VALUES (?,?,?,?,?)";

                        ps = conn.prepareStatement(sql);
                        i = 1;
                        ps.setInt(i++, stanza.getId());
                        ps.setInt(i++, day);
                        ps.setInt(i++, fascia);
                        ps.setTime(i++, Time.valueOf(localTimes[cnt]));
                        ps.setTime(i++, Time.valueOf(localTimes[cnt+1]));
                        ps.executeUpdate();
                        cnt++;
                        fascia++;
                    }else cnt++;
                }
                day++;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return stanza;
    }

    @Override
    public boolean update(Stanza stanza) {
        PreparedStatement ps;
        String sql;
        int i = 1;
        try {
            sql     = " UPDATE rooms SET "
                    + "nome = ?,"
                    + "maxTemp = ?,"
                    + "minTemp = ?,"
                    + "absoluteMin = ? "
                    + "WHERE id = ?";

            ps = conn.prepareStatement(sql);
            ps.setString(i++, stanza.getNome());
            ps.setDouble(i++, stanza.getMaxTemp());
            ps.setDouble(i++, stanza.getMinTemp());
            ps.setDouble(i++, stanza.getAbsoluteMin());
            ps.setInt(i, stanza.getId());
            ps.executeUpdate();

            sql = "DELETE FROM orarionoff WHERE roomID = ?";

            ps = conn.prepareStatement(sql);
            ps.setInt(1, stanza.getId());

            int day = 0;
            for(LocalTime[] localTimes : stanza.getTurnOnOffTimes()){
                int fascia = 1;
                for(int cnt=0; cnt<6; cnt++){
                    if(localTimes[cnt]!=null) {
                        sql = " INSERT INTO orarionoff "
                                + "( roomID, "
                                + "giorno,"
                                + "fascia,"
                                + "orarioAccensione,"
                                + "orarioSpegnimento )"
                                + " VALUES (?,?,?,?,?)";

                        ps = conn.prepareStatement(sql);
                        i = 1;
                        ps.setInt(i++, stanza.getId());
                        ps.setInt(i++, day);
                        ps.setInt(i++, fascia);
                        ps.setTime(i++, Time.valueOf(localTimes[cnt]));
                        ps.setTime(i++, Time.valueOf(localTimes[cnt+1]));
                        ps.executeUpdate();
                        cnt++;
                        fascia++;
                    }else cnt++;
                }
                day++;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    @Override
    public boolean delete(Stanza stanza) {
        PreparedStatement ps;
        return false;
    }

    @Override
    public Stanza[] findAllRooms() {
        PreparedStatement ps;
        Stanza[] stanze;
        ArrayList <Stanza> tmp = new ArrayList();
        int i = 0;
        try {
            String sql = "SELECT * FROM rooms";

            ps = conn.prepareStatement(sql);
            /*ps.setString(i++, LocalDate.now());*/
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Stanza stanza = readStanza(rs);
                    tmp.add(stanza);
                }
            }
            stanze = new Stanza[tmp.size()];
            stanze = tmp.toArray(stanze);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return stanze;
    }

    @Override
    public Stanza findRoom(int roomId) {
        PreparedStatement ps;
        Stanza stanza = new Stanza();
        try {
            String sql = "SELECT * FROM rooms where id = ?";

            ps = conn.prepareStatement(sql);
            ps.setInt(1, roomId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    stanza = readStanza(rs);
                }
            }

            sql = "SELECT * FROM orarionoff where RoomID = ?";

            ps = conn.prepareStatement(sql);
            ps.setInt(1, roomId);
            try (ResultSet rs = ps.executeQuery()) {
                stanza.setTurnOnOffTimes(readOrari(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return stanza;
    }

    private Stanza readStanza (ResultSet rs){
        Stanza stanza = new Stanza();
        try {
            stanza.setId(rs.getInt("id"));
        } catch (SQLException sqle) {}
        try {
            stanza.setNome(rs.getString("nome"));
        } catch (SQLException sqle) {}
        try {
            stanza.setMaxTemp(rs.getDouble("maxTemp"));
        } catch (SQLException sqle) {}
        try {
            stanza.setMinTemp(rs.getDouble("minTemp"));
        } catch (SQLException sqle) {}
        try {
            stanza.setAbsoluteMin(rs.getDouble("absoluteMin"));
        } catch (SQLException sqle) {}

        return stanza;
    }

    private LocalTime[][] readOrari (ResultSet rs){
        LocalTime[][] orariOnOff = new LocalTime[7][];
        try {
            for(int i=0; i<7; i++) {
                LocalTime[] onOff = new LocalTime[6];
                int tmp;
                rs.next();
                onOff[0] = rs.getTime("orarioAccensione").toLocalTime();
                onOff[1] = rs.getTime("orarioSpegnimento").toLocalTime();
                if(!rs.isLast()) {
                    rs.next();
                    tmp = rs.getInt("fascia");
                    if (tmp == 2) {
                        onOff[2] = rs.getTime("orarioAccensione").toLocalTime();
                        onOff[3] = rs.getTime("orarioSpegnimento").toLocalTime();
                        if(!rs.isLast()) {
                            rs.next();
                            tmp = rs.getInt("fascia");
                            if (tmp == 3) {
                                onOff[4] = rs.getTime("orarioAccensione").toLocalTime();
                                onOff[5] = rs.getTime("orarioSpegnimento").toLocalTime();
                            } else {
                                rs.previous();
                            }
                        }
                    } else {
                        rs.previous();
                    }
                }
                orariOnOff[i] = onOff;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return orariOnOff;
    }

    private LocalTime[] parse(String input){
        String noEstremi = input.substring( 1, input.length() - 1);
        String[] splitted = noEstremi.split(", ");
        LocalTime[] toRet = new LocalTime[splitted.length];
        int i = 0;
        for (String orario : splitted){
            if(orario.equalsIgnoreCase("null")){
                toRet[i]=null;
            }else{
                toRet[i] = LocalTime.parse(orario);
            }
            i++;
        }
        return toRet;
    }

}