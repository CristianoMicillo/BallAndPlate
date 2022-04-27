package it.sarrocchi.ballandplate;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class MapEditor extends AppCompatActivity
        implements View.OnClickListener, View.OnTouchListener {

    private int strumentoSelezionato;
    float tx, ty, d;

    private float distanza(MotionEvent me) {
        if(me.getPointerCount()<2) return 0;
        double dx2 = Math.pow(me.getX(0)-me.getX(1),2);
        double dy2 = Math.pow(me.getY(0)-me.getY(1),2);
        return (float)Math.sqrt( dx2 + dy2 );
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        int x = (int)(motionEvent.getX()/scala);
        int y = (int)(motionEvent.getY()/scala);
        switch(strumentoSelezionato) {
            case R.id.selectButton:

                if(motionEvent.getPointerCount()>1) {
                    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        d = scala+distanza(motionEvent);
                        Log.i("tocchi",motionEvent.getPointerCount()+"");
                    } else {
                        scala = (int)(distanza(motionEvent)-d);
                        Log.i("tocchi",scala+"");
                    }
                } else {
                    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        tx = editorView.ox + motionEvent.getX();
                        ty = editorView.oy + motionEvent.getY();
                    }
                    if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                        editorView.ox = (int) (tx - motionEvent.getX());
                        editorView.oy = (int) (ty - motionEvent.getY());
                    }
                }

                break;
            case R.id.erbaButton:
                mappa[y][x] = COMPONENTE.ERBA;
                break;
            case R.id.bucaButton:
                mappa[y][x] = COMPONENTE.BUCA;
                break;
            case R.id.muroButton:
                mappa[y][x] = COMPONENTE.MURO;
                break;
            case R.id.goalButton:
                mappa[y][x] = COMPONENTE.GOAL;
                break;
        }
        editorView.invalidate();
        return true;
    }

    public enum COMPONENTE {
        ERBA,
        BUCA,
        MURO,
        GOAL
    }



    @Override
    public void onClick(View view) {
        strumentoSelezionato = view.getId();


        for(int i=0;i<bottoni.length;i++)
            if(bottoni[i].getId()==strumentoSelezionato)
                bottoni[i].setBackgroundColor(Color.RED);
            else
                bottoni[i].setBackgroundColor(Color.BLUE);
    }

    int mw=26, mh=40, scala=30;
    COMPONENTE[][] mappa;
    private Button[] bottoni;

    EditorView editorView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_editor);
        editorView = findViewById(R.id.editorView);
        editorView.mapEditor = this;
        editorView.setOnTouchListener(this);
        bottoni = new Button[5];
        int i=0;
        bottoni[i++] = findViewById(R.id.selectButton);
        bottoni[i++] = findViewById(R.id.erbaButton);
        bottoni[i++] = findViewById(R.id.bucaButton);
        bottoni[i++] = findViewById(R.id.muroButton);
        bottoni[i++] = findViewById(R.id.goalButton);
        for(i=0;i<bottoni.length;i++)
            bottoni[i].setOnClickListener(this);

        caricaDaFile();
        if(mappa==null) {
            mappa = new COMPONENTE[mh][mw];
            for (i = 0; i < mh; i++)
                for (int j = 0; j < mw; j++)
                    mappa[i][j] = COMPONENTE.ERBA;
        }
    }

    final String fileName = "mappa.txt";
    private void caricaDaFile() {
        File file = new File(getExternalFilesDir(null),fileName);
        StringBuilder text = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            br.close();
        }
        catch (IOException e) {
            Log.i("Boh",e.toString());
            return;
        }
        String s = text.toString();
        String[] p = s.split("\n");
        String[] p1 = p[0].split(",");
        mh = Integer.parseInt(p1[0]);
        mw = Integer.parseInt(p1[1]);
        mappa = new COMPONENTE[mh][mw];
        for(int i=0;i<mh;i++) {
            String[] p2 = p[i+1].split(",");
            for(int j=0;j<mw;j++)
                mappa[i][j] = COMPONENTE.valueOf(p2[j]);
        }
    }

    private void salvaNelFile() {
        File file = new File(getExternalFilesDir(null),fileName);
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            bw.write(mh+","+mw+"\n");
            for(int i=0;i<mh;i++) {
                for (int j = 0; j < mw; j++)
                    bw.write(mappa[i][j].toString()+",");
                bw.write("\n");
            }
            bw.close();
        }catch(IOException e) {
            Log.i("Boh",e.toString());
            return;
        }
    }

    @Override
    protected void onDestroy() {
        salvaNelFile();
        super.onDestroy();
    }
}