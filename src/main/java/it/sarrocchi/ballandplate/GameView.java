package it.sarrocchi.ballandplate;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

public class GameView extends View {
    float w,h;
    int x=26;
    int y=50;
    float mw, mh;
    private GameActivity controller;
    int pronto=0;
    boolean fatto=true;
    public GameView(Context context) {
        super(context);
    }

    public GameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public GameView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public GameView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void init(GameActivity ma)
    {
        controller=ma;


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(controller==null)
            return;
        Paint p = new Paint();
        int ii=0;
        p.setColor(Color.GREEN);
        canvas.drawRect(0,0,getWidth(),getHeight(),p);
        for(int i=0;i<mh;i++)
        {
            for(int j=0;j<mw;j++)
            {
                switch(controller.mappa[i][j])
                {
                    case ERBA:
                        p.setColor(Color.GREEN);
                        canvas.drawRect(controller.vx[ii],controller.vy[ii],controller.vx[ii]+w/mw,controller.vy[ii]+h/mh,p);
                        break;
                    case BUCA:
                        p.setColor(Color.BLACK);
                        canvas.drawCircle(controller.vx[ii]+w/mw/2,controller.vy[ii]+h/mh/2,w/mw/2,p);
                        //canvas.drawRect(controller.vx[ii],controller.vy[ii],controller.vx[ii]+w/mw,controller.vy[ii]+h/mh,p);
                        break;
                    case MURO:
                        p.setColor(Color.GRAY);
                        canvas.drawRect(controller.vx[ii],controller.vy[ii],controller.vx[ii]+w/mw,controller.vy[ii]+h/mh,p);
                        break;
                    case GOAL:
                        p.setColor(Color.BLUE);
                        canvas.drawRect(controller.vx[ii],controller.vy[ii],controller.vx[ii]+w/mw,controller.vy[ii]+h/mh,p);
                        break;
                }
                ii++;
            }
        }
        /*p.setColor(Color.GREEN);
        canvas.drawRect(0,0,getWidth(),getHeight(),p);*/
        p.setColor(Color.RED);
        canvas.drawCircle(controller.getX(),controller.getY(),controller.ge.gp.raggio,p);

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if(fatto)
        {
            //dividi(getWidth(),getHeight());
            pronto=1;
            w=getWidth();
            h=getHeight();
        }
        fatto=false;
    }

    void stampaVettori()
    {
        for(int i=0;i<controller.vx.length;i++)
        {
            Log.i("Vettore x:"+controller.vx[i],"Vettore y: "+controller.vy[i]);
        }
    }
}
