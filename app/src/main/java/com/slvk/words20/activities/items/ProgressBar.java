package com.slvk.words20.activities.items;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.slvk.words20.DisplayHelper;
import com.slvk.words20.R;

import java.util.ArrayList;
import java.util.List;

public class ProgressBar {
private Context context;
private int numOfRectangles;
private List<ImageView> items;
private List<ShapeDrawable> rectangles;
private int currentItem = 0;

public final int GREY = 0;
public final int WHITE = 1;
public final int RED = 2;
public final int GREEN = 3;
private final int MARGIN = 2;

    public ProgressBar(Context context, int numOfRectangles){
        this.context = context;
        this.numOfRectangles = numOfRectangles;
        items = new ArrayList<>();
        rectangles = new ArrayList<>();

        int width = getWidthOfRectangle(numOfRectangles);

        for(int i =0; i < 4; i++) {
            rectangles.add(new ShapeDrawable(new RectShape()));
            rectangles.get(i).setIntrinsicHeight(8);
            rectangles.get(i).setIntrinsicWidth(width);
        }

        int red = context.getResources().getColor(R.color.notCorrectRed);
        int green = context.getResources().getColor(R.color.correctGreen);
        int white = context.getResources().getColor(R.color.white);
        int grey = context.getResources().getColor(R.color.grey);

        rectangles.get(RED).getPaint().setColor(red);
        rectangles.get(GREEN).getPaint().setColor(green);
        rectangles.get(WHITE).getPaint().setColor(white);
        rectangles.get(GREY).getPaint().setColor(grey);
    }




    private int getWidthOfRectangle(int num_of_rectangles){
        int screen_with = context.getResources().getDisplayMetrics().widthPixels;

        int rectangle_with = screen_with / num_of_rectangles;
        rectangle_with -= DisplayHelper.dpToPixel(MARGIN - 1, context);
        return rectangle_with;
    }

    public void drawRectangles(LinearLayout linearLayout){

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        params.setMargins(MARGIN, 0, MARGIN, 0);


        for(int i = 0; i < numOfRectangles; i++) {
            ImageView iView = new ImageView(context);
            iView.setLayoutParams(params);
            iView.setImageDrawable(rectangles.get(GREY));
            linearLayout.addView(iView);

            items.add(iView);
        }

        int i = context.getResources().getColor(R.color.white);
        items.get(currentItem).setImageDrawable(rectangles.get(WHITE));
    }

    public void setCurrentColor(int color_id){
        items.get(currentItem).setImageDrawable(rectangles.get(color_id));
    }

    public void moveNext(){
        currentItem++;
        setCurrentColor(WHITE);
    }



}
