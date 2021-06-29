package com.tkdrnjs0621.demoapp_bcm;

public interface ClickManagerCallback {
    public class ViewDataArgs
    {
        int x;
        int y;
        int w;
        int h;
        ViewDataArgs(int x, int y, int w, int h)
        {
            this.x=x;
            this.y=y;
            this.w=w;
            this.h=h;
        }
    };

    public ViewDataArgs getViewData(int id);
    public void performAction(int action, int id);
    public void performAction(int action, int x, int y);
}
