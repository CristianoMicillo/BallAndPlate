package it.sarrocchi.ballandplate;

import android.util.Log;

public class GameEngine extends Thread{
    GamePalla gp;
    int w,h;
    private final int FPS = 100;
    public float gx=0, gy=0;
    int collissione=1;
    GameEngine(int w,int h) {
        this.w=w;
        this.h=h;
        Log.i("ciassdsdsdsdasdo"+w,""+h);
        gp= new GamePalla(w,h,20);
        start();
    }

    void collisioni(GameActivity.COMPONENTE mappa[][], float vx[], float vy[],float mw,float mh)
    {
        int ii=0;
        if(w!=0 && h!=0)
        {
            for(int i=0;i<mh;i++)
            {
                for(int j=0;j<mw;j++)
                {
                    if(mappa[i][j]!= GameActivity.COMPONENTE.ERBA)
                    {
                        float dsx=vx[ii]-gp.x;
                        float dsy=vy[ii]-gp.y;
                        float fx=vx[ii]-gp.x;
                        float fy=vy[ii]+gp.y;
                        float distanzai=(float)Math.sqrt(dsx*dsx+dsy*dsy);
                        float distanzaf=(float)Math.sqrt(fx*fx+fy*fy);
                        if(Math.abs(distanzai)<=Math.abs(gp.raggio+mw/w) && Math.abs(distanzaf)>=Math.abs(gp.raggio+mw/w))
                        {
                            Log.i("ciao","ciao");
                            collissione=0;
                        }else
                        {
                            //collissione=1;
                        }
                    }
                    ii++;
                }
            }
        }
    }
    public double getX(){return gp.x; }
    public double getY(){return gp.y; }
    public void controlloMura()
    {

        if(gp.x-gp.raggio<0)
        {
            gp.x=gp.raggio;
        }else if(gp.x+gp.raggio>w)
        {
            gp.x=w-gp.raggio;
        }
        if(gp.y-gp.raggio<0)
        {
            gp.y=gp.raggio;
        }else if(gp.y+gp.raggio>h)
        {
            gp.y=h-gp.raggio;
        }

    }
    public void spostamentoPalla()
    {
        gp.vx =  -gx*collissione;
        gp.x = gp.x + gp.vx;
        gp.vy =  gy*collissione;
        gp.y = gp.y + gp.vy;
        //Log.i(""+gx,""+gy);
    }
    private void update() {
        spostamentoPalla();
        controlloMura();
    }

    @Override
    public void run() {
        super.run();
        while(true) {
            if(w!=0 && h!=0)
            update();
            try {
                sleep(1000 / FPS);
            }catch(Exception e){}
        }
    }

}
