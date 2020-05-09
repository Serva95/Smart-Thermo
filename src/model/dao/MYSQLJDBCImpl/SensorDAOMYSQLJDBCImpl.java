package model.dao.MYSQLJDBCImpl;

import model.dao.SensorDAO;
import model.mo.Sensore;

import java.sql.Connection;

public class SensorDAOMYSQLJDBCImpl implements SensorDAO {

    public SensorDAOMYSQLJDBCImpl(Connection conn) {this.conn = conn;}
    Connection conn;

    @Override
    public Sensore[] findAllSensors() {
        return new Sensore[0];
    }
}
