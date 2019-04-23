package edu.pucmm.dhamarmj;

import edu.pucmm.dhamarmj.Handler.mainHandler;
import edu.pucmm.dhamarmj.Services.StartDatabase;

import java.util.logging.Handler;

public class Main {
    public static void main(String[] args) {
        StartDatabase.getInstancia().startDb();
        new mainHandler().startup();

    }
}
