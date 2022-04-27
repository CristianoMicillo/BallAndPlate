package it.sarrocchi.ballandplate;

import android.Manifest;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class GameActivity  extends AppCompatActivity implements SensorEventListener,Runnable {
    public static final String HIGH_SAMPLING_RATE_SENSORS = Manifest.permission.HIGH_SAMPLING_RATE_SENSORS;
    final String fileName = "mappa.txt";
    private GameView gv;
    GameEngine ge;
    private SensorManager sensorManager;
    private Sensor sensor;
    int w,h;
    int ox, oy, scala;
    int SCREEN_FPS=30;
    float vx[];
    float vy[];
    int mw=26, mh=40;
    public enum COMPONENTE {
        ERBA,
        BUCA,
        MURO,
        GOAL
    }
    COMPONENTE[][] mappa;
    private void caricaDaFile() {
        File file = new File(getExternalFilesDir(null), fileName);
        StringBuilder text = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            br.close();
        } catch (IOException e) {
            Log.i("Boh", e.toString());
            return;
        }
        String s = text.toString();
        String[] p = s.split("\n");
        String[] p1 = p[0].split(",");
        mh = Integer.parseInt(p1[0]);
        mw = Integer.parseInt(p1[1]);
        mappa = new COMPONENTE[mh][mw];
        for (int i = 0; i < mh; i++) {
            String[] p2 = p[i + 1].split(",");
            for (int j = 0; j < mw; j++)
                mappa[i][j] = COMPONENTE.valueOf(p2[j]);
        }
    }
    public void mappaDefault()
    {
        for(int i=0;i<mw;i++)
        {
            for(int j=0;j<mh;j++)
            {
                mappa[j][i]= COMPONENTE.ERBA;
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        gv=findViewById(R.id.gameView);
        gv.init(this);
        ge= new GameEngine(gv.getWidth(),gv.getHeight());
        vx= new float[mw*mh];
        vy= new float[mw*mh];
        mappa= new COMPONENTE[mh][mw];
        mappaDefault();
        caricaDaFile();
        gv.mw=mw;
        gv.mh=mh;

        //Log.i("ciaoooo"+gv.getWidth(),""+ gv.getHeight());
        Thread t= new Thread(this);
        t.start();
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> deviceSensor= sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
        for(Sensor i:deviceSensor)
        {
            Log.i("Michele",""+i);
        }
        sensor= deviceSensor.get(0);
    }
    public void dividi()
    {
        int vxx=0;
        int vyy=0;
        Log.i("W: "+w,"H: "+h);
        for(int i=0;i<mh;i++)
        {
            for(int j=0;j<mw;j++)
            {
                double x=(double) w/(double) mw*j;//cordinata x di partenza, da aggiungere w/mw per sapere l'arrivo

                double y=(double)h/(double)mh*i;//cordinata y di partenza, da aggiungere h/mh per sapere l'arrivo
                Log.i("X:"+x,"Y:"+y);
                vx[vxx++]=(float)x;
                vy[vyy++]=(float)y;

                // Log.i("VXX"+vxx,"VYY"+vyy);
            }
        }
    }
    private Runnable reDraw=new Runnable() {
        @Override
        public void run() {
            gv.invalidate();
        }
    };
    public void aspettaWH()
    {

        ge.w=gv.getWidth();
        ge.h=gv.getHeight();
        gv.pronto=0;
        w=gv.getWidth();
        h=gv.getHeight();
        dividi();
        //stampaVettori();
        Log.i("ciao","CRISTIANO");
        Log.i("Larghezza "+gv.getWidth(),"Altezza"+gv.getHeight());
        gv.pronto=0;
    }
    @Override
    public void run() {
        while(true) {

            if(gv.pronto==1)
            {
                aspettaWH();
            }
            ge.collisioni(mappa,vx,vy,mw,mh);
            runOnUiThread(reDraw);
            try {
                Thread.sleep(1000/SCREEN_FPS);
            }catch(Exception e) {}
        }
    }
    @Override
    protected void onResume() {

        super.onResume();

        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST);
    }
    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        /*String s = sensorEvent.values[0]+"";
        s += ","+sensorEvent.values[1];
        s += ","+sensorEvent.values[2];*/
        ge.gx = sensorEvent.values[0];
        ge.gy = sensorEvent.values[1];
        //Log.i("sensori",s);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public int getX() {
        return (int)ge.gp.x;
    }
    public int getY() {
        return (int)ge.gp.y;
    }
}
