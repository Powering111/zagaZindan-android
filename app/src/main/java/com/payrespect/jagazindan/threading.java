package com.payrespect.jagazindan;

import android.os.Handler;

public class threading extends Thread {
    private boolean loaded;
    private Handler handler;
    private MainActivity.mode m;
    public threading(Handler h){
        m= MainActivity.mode.normal;
        handler=h;
    }
    public void setMode(MainActivity.mode mode1){
        m=mode1;
    }
    public void setLoaded(boolean b){
        loaded=b;
    }
    @Override
    public void run(){
        try {
            if (m== MainActivity.mode.direct) {
                handler.sendEmptyMessage(1);
            } else if(m== MainActivity.mode.school) {
                loaded=false;
                handler.sendEmptyMessage(8);

                while(!loaded){
                    sleep(10);
                }
                loaded=false;
                handler.sendEmptyMessage(7);
                sleep(100);
                /*while(!loaded){
                    sleep(10);
                }*/
                loaded=false;
                handler.sendEmptyMessage(9);
                sleep(100);
                /*while(!loaded){
                    sleep(10);
                }*/
                handler.sendEmptyMessage(10);
            }else {
                    loaded=false;
                    handler.sendEmptyMessage(2);

                    while(!loaded){
                        sleep(10);
                    }
                    handler.sendEmptyMessage(3);
            }
            loaded=false;
            while(!loaded){
                sleep(10);
            }
            loaded=false;
            handler.sendEmptyMessage(4);
            while(!loaded){
                sleep(10);
            }
            handler.sendEmptyMessage(5);
        }catch(Exception e){
            handler.sendEmptyMessage(6);
        }
    }
}
