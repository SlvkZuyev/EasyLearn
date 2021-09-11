package com.slvk.words20.activities.items;

import android.content.Context;
import android.widget.ImageView;

import com.slvk.words20.R;

public class ProgressCircles {
    Context context;
    private ImageView[] circles;
    private int numberOfCircles;
    private int currentCirclePosition;
    int emptyCircle;
    int correctCircle;
    int notCorrectCircle;

    public ProgressCircles(ImageView...circles){
        this.circles = circles;
        numberOfCircles = circles.length;
    }

    public void drawEmptyCircles(){
        for(ImageView circle : circles){
            circle.setImageResource(R.drawable.empty_circle);
        }
    }

    public void setNextCorrect(){
        if(allCirclesFilled() ){
            currentCirclePosition = 0;
            drawEmptyCircles();
        }
        circles[currentCirclePosition].setImageResource(R.drawable.correct_circle);
        currentCirclePosition++;
    }

    public void setNextNotCorrect(){
        if(allCirclesFilled() ){
            currentCirclePosition = 0;
            drawEmptyCircles();
        }
        circles[currentCirclePosition].setImageResource(R.drawable.not_correct_circle);
        currentCirclePosition++;
    }

    private boolean allCirclesFilled(){
        return currentCirclePosition == numberOfCircles;
    }

}
