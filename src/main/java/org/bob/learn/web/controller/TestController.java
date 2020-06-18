package org.bob.learn.web.controller;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.bob.learn.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@RestController
@RequestMapping("/test")
public class TestController {


    private static AtomicLong atomicLong = new AtomicLong(1);

    @Autowired
    private TestService testService;

    @Autowired
    private HikariDataSource hikariDataSource;

    @GetMapping("/get")
    public String test() throws SQLException {
        Connection connection = hikariDataSource.getConnection();
        Statement statement=connection.createStatement();

        //statement做一些 增删改查
        String sqlString = "select * from student";
        ResultSet resultSet=statement.executeQuery(sqlString);
        while(resultSet.next()){
            log.info(resultSet.getLong(1)+"");
        }
        connection.close();
        String mac = mac();
        testService.print(mac);
        return mac;
    }


    private String mac(){
        return "MAC"+ String.format("%09d", atomicLong.getAndIncrement());
    }
}