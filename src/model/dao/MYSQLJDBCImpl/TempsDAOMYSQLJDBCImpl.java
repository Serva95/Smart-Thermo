package model.dao.MYSQLJDBCImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import model.dao.TempsDAO;
import model.mo.Lettura;

public class TempsDAOMYSQLJDBCImpl implements TempsDAO {

    Connection conn;

    public TempsDAOMYSQLJDBCImpl(Connection conn) {this.conn = conn;}

    /**
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
                    + " (SELECT temp, hum, date FROM temps WHERE date LIKE ? OR date LIKE ? ORDER BY date DESC LIMIT 100)"
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
    public Lettura Readlast(){
        PreparedStatement ps;
        Lettura letti;
        try {
            //SELECT temp, hum, date FROM temps WHERE measureId = (SELECT MAX(measureId) FROM temps)
            String sql = "SELECT temp, hum, date FROM temps ORDER BY measureId DESC LIMIT 1";
            ps = conn.prepareStatement(sql);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                letti = readtemps(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return letti;
    }

    @Override
    public Lettura[] Readmeds(int number_of_reads) {
        PreparedStatement ps;
        ArrayList<Lettura> letti = new ArrayList();
        Lettura[] lettiarray;
        try {
            //WHERE date LIKE '2018-11-16%' OR date LIKE '2018-11-15%' order by date desc limit 150
            /*String sql = " SELECT * FROM"
                    + " (SELECT mediumtemp, mediumhum, mediumDay FROM `mediums` ORDER BY mediumDay DESC LIMIT ? )"
                    + " AS tmp ORDER BY mediumDay ";*/
            //for(int j = number_of_reads-1; j>=0; j--){
                //String sql = "SELECT AVG(temp) AS temp, AVG(hum) AS hum, MIN(date) AS date FROM temps WHERE date LIKE ? ";
                String sql = "SELECT " +
                        "CAST(date AS DATE) AS giorno, " +
                        "AVG(temp) AS temp, " +
                        "AVG(hum) AS hum " +
                        "FROM temps " +
                        "GROUP BY giorno " +
                        "ORDER BY giorno DESC " +
                        "LIMIT ? ";
                ps = conn.prepareStatement(sql);
                /*int i = 1;
                ps.setString(i, LocalDate.now().minusDays(j).toString() + "%");
                */
                ps.setInt(1, number_of_reads);
                try (ResultSet rs = ps.executeQuery()) {
                    rs.afterLast();
                    while (rs.previous()) {
                        Lettura tmp = readmediums(rs);
                        letti.add(tmp);
                    }
                }
            //}
            lettiarray = new Lettura[letti.size()];
            lettiarray = letti.toArray(lettiarray);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return lettiarray;
    }


    private Lettura readtemps (ResultSet rs){
        Lettura lett = new Lettura();
        try {
            lett.setTemp(rs.getDouble("temp"));
        } catch (SQLException sqle) {}

        try {
            lett.setHum(rs.getDouble("hum"));
        } catch (SQLException sqle) {}

        lett.setReadingdatetime(readdatetimesql(rs));

        return lett;
    }

    private LocalDateTime readdatetimesql(ResultSet rs){
        LocalDateTime toret = null;
        try {
            if(rs.getString("date") != null){
                toret = LocalDateTime.parse(rs.getString("date").substring(0, 19).replaceFirst(" ", "T"), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            }else{
                toret = LocalDateTime.now();
            }
        } catch (SQLException sqle) {}
        return toret;
    }

    private LocalDate readDateSql(ResultSet rs){
        LocalDate toret = null;
        try {
            //giorno-date
            if(rs.getString("giorno") != null){
                //toret = LocalDateTime.parse(rs.getString("date").substring(0, 19).replaceFirst(" ", "T"), DateTimeFormatter.ISO_LOCAL_DATE_TIME).toLocalDate();
                toret = LocalDate.parse(rs.getString("giorno"), DateTimeFormatter.ISO_LOCAL_DATE);
            }else{
                toret = LocalDate.parse("2010-01-01", DateTimeFormatter.ISO_LOCAL_DATE);
            }
        } catch (SQLException sqle) {}
        System.out.println(toret);
        System.out.println("");
        return toret;
    }

    private Lettura readmediums (ResultSet rs){
        Lettura lett = new Lettura();
        try {
            lett.setTemp((double)Math.round(rs.getDouble("temp")*100)/100);
        } catch (SQLException sqle) {}

        try {
            lett.setHum((double)Math.round(rs.getDouble("hum")*100)/100);
        } catch (SQLException sqle) {}

        System.out.println(lett.getTemp());
        System.out.println(lett.getHum());

        //lett.setReadingdatetime(readdatetimesql(rs));
        lett.setReadingDate(readDateSql(rs));

        return lett;
    }

}
