package it.sarrocchi.ballandplate;

public class GamePalla {
     int w,h;
     float x, vx;
     float y, vy;
     int raggio;
    GamePalla(int w,int h,int raggio)
    {
        this.w=w;
        this.h=h;
        this.x=w/2;
        this.y=h/2;
        this.raggio=raggio;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setVx(float vx) {
        this.vx = vx;
    }
    public void setVy(float vy) {
        this.vy = vy;
    }


}
