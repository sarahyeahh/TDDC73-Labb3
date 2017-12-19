package com.example.sarah.labb3;

/*  Sarah Fosse sarfo265
    Malin Wetterskog malwe794
    TDDC73
    Labb 3
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.constraint.solver.widgets.Rectangle;
import android.util.AttributeSet;
import android.view.View;

public class InteractiveList extends View{

    Context c;
    int x = 0;
    int y = 0;
    private String name = "";

    public InteractiveList(Context context){
        super(context);
        this.c = context;
    }

    public InteractiveList(Context context, String name, int x, int y) {
        super(context);
        this.name = name;
        this.setWillNotDraw(false);
        this.x = x;
        this.y = y;
    }

    public InteractiveList(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public InteractiveList(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public InteractiveList(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //Draw the canvas with the specific parameters
        Paint pBackground = new Paint();
        pBackground.setColor(Color.WHITE);
        Paint pText = new Paint();
        pText.setColor(Color.BLACK);
        pText.setTextSize(40);
        canvas.drawPaint(pBackground);
        canvas.drawText(name, x, y, pText);
    }

    //Return the current name
    public String getName(){
        return name;
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        super.setOnClickListener(l);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //Set dimensions on the PopUpWindow
        setMeasuredDimension(500, 100);
    }
}