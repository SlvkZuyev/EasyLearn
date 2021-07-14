package com.slvk.words20.activities.items;

import android.content.Context;
import android.widget.TextView;

public class PulseCountDownTimer {
    long durationMillis;
    Context context;

    PulseCountDownTimer(long durationMillis, Context context){
        this.context = context;
        this.durationMillis = durationMillis;

    }

    public void startTimer(){

    }

    public void setDurationMillis(long durationMillis) {
        this.durationMillis = durationMillis;
    }

    public void setContext(Context context) {
        this.context = context;
    }


}
