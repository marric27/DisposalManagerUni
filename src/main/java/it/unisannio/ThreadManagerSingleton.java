package it.unisannio;

import it.unisannio.filters.Filter;
import it.unisannio.filters.FilterByDistance;
import it.unisannio.filters.FilterByOccurrency;
import it.unisannio.filters.FilterByTag;
import it.unisannio.filters.FilterByTimestamp;
import it.unisannio.filters.FilterDest;

public class ThreadManagerSingleton {
    private static ThreadManagerSingleton instance;
    private Filter startFilter;
    private ThreadManagerSingleton() {
        Filter f5 = new FilterDest("we", null);
        Filter f4 = new FilterByDistance("filtro4", f5);
        Filter f3 = new FilterByTimestamp("filtro3", f4);
        Filter f2 = new FilterByOccurrency("filtro2", f3);
        Filter f1 = new FilterByTag("filtro1", f2);
        startFilter = f1;

        Thread th1 = new Thread(f1);
        Thread th2 = new Thread(f2);
        Thread th3 = new Thread(f3);
        Thread th4 = new Thread(f4);
        Thread th5 = new Thread(f5);
        th1.start();
        th2.start();
        th3.start();
        th4.start();
        th5.start();
    }

    public static ThreadManagerSingleton getInstance(){
        if (instance == null){
            instance = new ThreadManagerSingleton();
        }
        return instance;
    }

    public Filter getStartFilter() {
        return startFilter;
    }
}
