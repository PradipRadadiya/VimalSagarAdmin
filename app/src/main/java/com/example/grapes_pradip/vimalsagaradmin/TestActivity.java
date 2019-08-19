package com.example.grapes_pradip.vimalsagaradmin;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TestActivity extends AppCompatActivity {

    private static final String DEFAULT_DRIVER = "oracle.jdbc.driver.OracleDriver";
    private static final String DEFAULT_URL = "jdbc:oracle:thin:@192.168.0.1:1521:xe";
    private static final String DEFAULT_USERNAME = "system";
    private static final String DEFAULT_PASSWORD = "oracle";

    private Connection connection;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            this.connection = createConnection();
            Log.e("Connected","success");
            Statement stmt=connection.createStatement();

            ResultSet rs=stmt.executeQuery("select * from cat");
            while(rs.next()) {
                System.out.println("hello : " + rs.getString(1));
            }
            connection.close();
        }
        catch (Exception e) {
            Log.e("exception",""+e);
//            e.Log(""+e);
            e.printStackTrace();
        }


    }

    public static Connection createConnection(String driver, String url, String username, String password) throws ClassNotFoundException, SQLException {
        Class.forName(driver);
        return DriverManager.getConnection(url, username, password);
    }

    public static Connection createConnection() throws ClassNotFoundException, SQLException {
        return createConnection(DEFAULT_DRIVER, DEFAULT_URL, DEFAULT_USERNAME, DEFAULT_PASSWORD);
    }


}
