package edu.pucmm.dhamarmj;

import edu.pucmm.dhamarmj.Handler.*;
import edu.pucmm.dhamarmj.Services.StartDatabase;

public class Main {
    public static void main(String[] args) throws Exception {
        StartDatabase.getInstancia().startDb();
        new mainHandler().startup();
        new restHandler().startup();
        new soapHandler().init();

    }
}
