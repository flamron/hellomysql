package by.pvt.service;

import by.pvt.dto.SystemUsers;
import org.dbunit.DBTestCase;
import org.dbunit.PropertiesBasedJdbcDatabaseTester;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.junit.Test;

import java.sql.*;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import static org.junit.Assert.*;

public class SystemUsersServiceTest extends DBTestCase {
    SystemUsersService systemUsersService;

    @Override
    protected IDataSet getDataSet() throws Exception {
        return new FlatXmlDataSetBuilder()
                .build(SystemUsersService.class.getResourceAsStream("SystemUsersServiceTestData.xml"));
    }

    public SystemUsersServiceTest() {
        super();
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_DRIVER_CLASS, "com.mysql.jdbc.Driver");
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_CONNECTION_URL, "jdbc:mysql://localhost:3306/hello_mysql_junit");
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_USERNAME, "root");
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_PASSWORD, "root");

        systemUsersService = new SystemUsersService("testing");

    }

    private boolean SystemUsersEquals(SystemUsers user1, SystemUsers user2) {
        if (user1 == null) {
            return user2 == null; // If both null then return true
        }
        if (user2 == null) return false;
        return Objects.equals(user1.getId(), user2.getId()) &&
                Objects.equals(user1.getUsername(), user2.getUsername()) &&
                Objects.equals(user1.getActive(), user2.getActive());/* &&
                Objects.equals(user1.getDateofbirth(), user2.getDateofbirth());*/
    }

    @Test
    public void testGetSystemUsers() {
        List<SystemUsers> systemUsers = systemUsersService.getSystemUsers();
        assertNotNull(systemUsers);
        assertEquals(4, systemUsers.size());
        // renat
        SystemUsers renat = new SystemUsers();
        renat.setId(1);
        renat.setUsername("renat");
        renat.setActive(true);
        renat.setDateofbirth("417657600");
        // kolyan
        SystemUsers kolyan = new SystemUsers();
        kolyan.setId(2);
        kolyan.setUsername("kolyan");
        kolyan.setActive(true);
        kolyan.setDateofbirth("417657612");
        // Vika
        SystemUsers vika = new SystemUsers();
        vika.setId(3);
        vika.setUsername("Vika");
        vika.setActive(false);
        vika.setDateofbirth("1134263106");
        // RoberT
        SystemUsers robert = new SystemUsers();
        robert.setId(4);
        robert.setUsername("RoberT");
        robert.setActive(true);
        robert.setDateofbirth("1134263116");
        // Margo
        SystemUsers margo = new SystemUsers();
        margo.setId(5);
        margo.setUsername("margo");
        margo.setActive(true);
        margo.setDateofbirth("417657609");

        Iterator<SystemUsers> iterator = systemUsers.iterator();
        if (iterator.hasNext()) {
            SystemUsers user = iterator.next();
            switch (user.getId()) {
                case 1:
                    assertTrue(SystemUsersEquals(user, renat));
                    break;
                case 2:
                    assertTrue(SystemUsersEquals(user, kolyan));
                    break;
                case 3:
                    assertTrue(SystemUsersEquals(user, vika));
                    break;
                case 4:
                    assertTrue(SystemUsersEquals(user, robert));
                    break;
            }
            assertFalse(SystemUsersEquals(user, margo));
            user.setId(1000);
            assertFalse(SystemUsersEquals(user, renat));
            assertFalse(SystemUsersEquals(user, kolyan));
            assertFalse(SystemUsersEquals(user, vika));
            assertFalse(SystemUsersEquals(user, robert));
        }
    }

    @Test
    public void testAddSystemUser() {
        SystemUsers petter = new SystemUsers();
        petter.setId(101);
        petter.setUsername("petter");
        petter.setActive(true);
        petter.setDateofbirth("301301");
        systemUsersService.addSystemUser(petter);

        SystemUsers user = new SystemUsers();

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hello_mysql_junit",
                    "root", "root")) {
            PreparedStatement ps = connection.prepareStatement("select * from system_users where `id` = 101");
            ResultSet rs = ps.executeQuery();
            assertNotNull(rs);

            int rowCount = 0;
            while (rs.next()) {
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setActive(rs.getBoolean("active"));
                rowCount++;
            }
            assertEquals(1, rowCount);
            assertTrue(SystemUsersEquals(user, petter));

            rs.close();
            ps.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void deleteSystemUser() {

    }
}