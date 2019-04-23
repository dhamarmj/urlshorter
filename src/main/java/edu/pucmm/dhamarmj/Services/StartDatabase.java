package edu.pucmm.dhamarmj.Services;

import org.h2.tools.Server;

import java.sql.SQLException;

public class StartDatabase {
    private static StartDatabase instancia;

    private StartDatabase(){
    }

    public static StartDatabase getInstancia(){
        if(instancia == null){
            instancia=new StartDatabase();
        }
        return instancia;
    }

    public void startDb() {
        try {
            Server.createTcpServer("-tcpPort",
                    "9092",
                    "-tcpAllowOthers",
                    "-tcpDaemon").start();
        }catch (SQLException ex){
            System.out.println("Problema con la base de datos: "+ex.getMessage());
        }
    }
}
