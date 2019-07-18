package by.pvt.service;

import by.pvt.dao.SystemUsersMapper;
import by.pvt.dto.SystemUsers;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SystemUsersService {

    private static Logger log = Logger.getLogger(SystemUsersService.class.getName());
    private SqlSessionFactory sqlSessionFactory;

    public SystemUsersService(String environment) {
        try {
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(
                    Resources.getResourceAsStream("by/pvt/service/mybatis-config.xml"), environment);
        } catch (IOException e) {
            log.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public List<SystemUsers> getSystemUsers() {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            SystemUsersMapper mapper = session.getMapper(SystemUsersMapper.class);
            return mapper.selectByExample(null);
        }
        /*return sqlSessionFactory
                .openSession()
                .getMapper(SystemUsersMapper.class)
                .selectByExample(null);*/
    }


    public void addSystemUser(SystemUsers record) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            SystemUsersMapper mapper = session.getMapper(SystemUsersMapper.class);
            int result = mapper.insert(record);
            session.commit();
            log.info("Added new systemUser with result = " + result);
        }
        /*int result = sqlSessionFactory
                .openSession()
                .getMapper(SystemUsersMapper.class)
                .insert(record);*/
    }

    public int deleteSystemUser(int id) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            SystemUsersMapper mapper = session.getMapper(SystemUsersMapper.class);
            int result = mapper.deleteByPrimaryKey(id);
            session.commit();
            log.info("Deleted systemUser with id = " + id + " with result = " + result);
            return result;
        }
        /*int result = sqlSessionFactory
                .openSession()
                .getMapper(SystemUsersMapper.class)
                .insert(record);*/
    }

    public static void main(String[] args) {
        SystemUsersService systemUsersService = new SystemUsersService("testing");
        /*SystemUsers petter = new SystemUsers();
        petter.setId(101);
        petter.setUsername("petter");
        petter.setActive(true);
        petter.setDateofbirth("301301");
        systemUsersService.addSystemUser(petter);*/
        systemUsersService.deleteSystemUser(3);
    }

}
