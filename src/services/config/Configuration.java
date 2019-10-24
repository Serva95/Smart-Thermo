package services.config;

import java.util.logging.Level;

import model.dao.DAOFactory;
import model.session.dao.SessionDAOFactory;

public class Configuration {

    /* Database Configruation */
    public static final String DAO_IMPL=DAOFactory.MYSQLJDBCIMPL;
    public static final String DATABASE_DRIVER="com.mysql.jdbc.Driver";
    //public static final String DATABASE_URL="jdbc:mysql://localhost/thermo?user=root&password=serva-1995-orologio";

    public static final String DATABASE_URL="jdbc:mysql://localhost/thermo?user=root&password=";

    /* Session Configuration */
    public static final String SESSION_IMPL=SessionDAOFactory.COOKIEIMPL;

    /* Logger Configuration */
    public static final String GLOBAL_LOGGER_NAME="smart_thermo";
    public static final String GLOBAL_LOGGER_FILE="C:\\Users\\serva\\Documents\\logs\\logger_smartthermo.%g.txt";
    public static final Level GLOBAL_LOGGER_LEVEL=Level.WARNING;

    /* Password hashing configuration */
    /*public static final String[] BEGIN_SALTS = {"aNK}c86RYzsx^FpjT7@<","x@nR<S>GYC]6pv_)g^jZ","axFe68)<AguB_%fV5Ls3","e7Pb^Df{h>9S&+nqGA6V",
            "RySqAe(#3f5r2Ea}k[@u","Vpu}!R8&j%D6q=aM4B_-","p<S+N!sT(q@FPU8C}?aG","h^fprDc5{(WVqSv2$%H9","s>qm-fZ2VBL]k3^n[NH_","b9e@ZK/UDA}hT2V5QJvP"};
    public static final String[] END_PEPPERS = {"d2bN[?k)ZEqY6eJT]VSU","na!sJhA<}{L4/d2>9gqk","yKWqCgk26%VwbQED&?pF","Cv})xe8c5SVNzA-]Z^J$",
            "M!NQbrvL>a&57d$@2K4f","ft2cyd9GhBe8_?a6PS(q","c4NVX+U3JLp]W/s!x8gK","HEcpC]rS/{3vsbUeWn+P","J<HP6cT&h?5[2Y@]7LtU","d2G)8bzA4Y?H!ZQB6>7W"};
    */
}
