package it.sarrocchi.ballandplate;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class EditorView extends View {
    MapEditor mapEditor;
    int ox = 0, oy=0;
    public EditorView(Context context) {
        super(context);
    }

    public EditorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public EditorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public EditorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint p = new Paint();
        //canvas.drawText("EditorView",getWidth()/2,getHeight()/2,p);
        for(int i=0;i<mapEditor.mappa.length;i++)
            for(int j=0;j<mapEditor.mappa[i].length;j++) {
                int y = i*mapEditor.scala - oy;
                int x = j*mapEditor.scala - ox;
                switch (mapEditor.mappa[i][j]) {
                    case ERBA:
                        p.setColor(Color.GREEN);
                        canvas.drawRect(x,y,x+mapEditor.scala,y+mapEditor.scala,p);
                        break;
                    case BUCA:
                        p.setColor(Color.BLACK);
                        canvas.drawRect(x,y,x+mapEditor.scala,y+mapEditor.scala,p);
                        break;
                    case MURO:
                        p.setColor(Color.RED);
                        canvas.drawRect(x,y,x+mapEditor.scala,y+mapEditor.scala,p);
                        break;
                    case GOAL:
                        p.setColor(Color.BLUE);
                        canvas.drawRect(x,y,x+mapEditor.scala,y+mapEditor.scala,p);
                        break;
                }
            }

        p.setColor(Color.BLACK);
        for(int i=0;i<mapEditor.mappa.length+1;i++) {
            int y = i * mapEditor.scala - oy;
            canvas.drawLine(0, y, getWidth(), y, p);
        }
        for(int j=0;j<mapEditor.mappa[0].length+1;j++) {
            int x = j * mapEditor.scala - ox;
            canvas.drawLine(x, 0, x, getHeight(), p);
        }
    }
}
