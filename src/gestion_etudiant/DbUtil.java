package gestion_etudiant;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * @author B.M Nadir
 */
public class DbUtil {
    private static Connection conn;
    private static PreparedStatement stat;
    private static ResultSet rs;
    public static void dbConnection ()
    {
        try {
            Class.forName("org.h2.Driver").newInstance();
            conn = DriverManager.getConnection("jdbc:h2:./db/mydb", "root", "motdepasse");
            
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException ex) {
            Logger.getLogger(DbUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void dbDisconnect () 
    {
        try {
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(DbUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
     public static boolean execAction(String qu,String lName,String fName, String bDay, String addr) {
        try {
            stat = conn.prepareStatement(qu);
            stat.setString(1, lName);
            stat.setString(2, fName);
            stat.setString(3, bDay);
            stat.setString(4, addr);
            stat.executeUpdate();
            return true;
        }
        catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return false;
        }
    }
     public static ResultSet execQuery(String qu, String uname, String pword)
     {
        try {
            stat = conn.prepareStatement(qu);
            stat.setString(1, uname);
            stat.setString(2, pword);
            return stat.executeQuery();
        } catch (SQLException ex) {
            Logger.getLogger(DbUtil.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
     }
     
     public static ResultSet loadStudents (String col, String key)
     {
        String qu = "SELECT * FROM Etudiant WHERE "+col+" like '"+key+"%'";
        try {
            stat = conn.prepareStatement(qu);
            //stat.setString(1, col);
            //stat.setString(2, key);
            return stat.executeQuery();
        } catch (SQLException ex) {
            Logger.getLogger(DbUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
     }
}

 /*
    String sql = "CREATE TABLE IF NOT EXISTS Etudiant "
            + "(id_etudiant INTEGER(11) AUTO_INCREMENT NOT NULL, "
            + " nom_etudiant VARCHAR(20), "
            + " prenom_etudiant VARCHAR(20), "
            + " date_naiss VARCHAR(10), "
            + " adresse_etudiant VARCHAR(100), "
            + " PRIMARY KEY (id_etudiant))";
    stat = conn.createStatement();
    stat.executeUpdate(sql);
    sql  = "CREATE TABLE IF NOT EXISTS Admin "
            + " (username VARCHAR(20) PRIMARY KEY, password VARCHAR(20))"; //uname = Admin, password = Admin
    stat.executeUpdate(sql);

    sql2 = "INSERT INTO Admin VALUES ('Admin', 'Admin')";
    stat.executeUpdate(sql2);
    String sql = "DELETE FROM Admin";
    stat.executeUpdate(sql);
    sql = "INSERT INTO Admin VALUES ('Admin', 'Admin')";
    stat.executeUpdate(sql);

    String sql = "SELECT * FROM Admin WHERE username='aze'";
    rs = stat.executeQuery(sql);
    if(rs.next())
    {
        System.out.println("success");
    }
    else
    {
        System.out.println("Fail");
    }

    stat = conn.prepareStatement("INSERT INTO Etudiant(nom_etudiant, prenom_etudiant, date_naiss, adresse_etudiant) "
            + "VALUES('BAGHDADI', 'Rabah', '03/02/1993', 'Lacadat')");
    stat.execute();
*/