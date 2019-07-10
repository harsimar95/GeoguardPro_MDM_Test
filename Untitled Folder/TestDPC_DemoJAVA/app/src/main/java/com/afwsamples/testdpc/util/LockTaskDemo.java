package com.afwsamples.testdpc.util;


class LockTaskDemo {

    private static LockTaskDemo ltd = null;

    public static LockTaskDemo getInstance(){
        if(ltd == null){
             ltd = new LockTaskDemo();
             return ltd;
        }
        return ltd;
    }

    public void lockTheTask(){


    }
}
