package com.tkdrnjs0621.demoapp_bcm;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ClickManager {
    private List<Integer> list_view_id;
    ClickManagerCallback _callback;
    public ClickManager(ClickManagerCallback callback)
    {
        list_view_id = new ArrayList<Integer>();
        _callback = callback;
    }

    public void addView(int target)
    {
        list_view_id.add(target);
    }

    public void addView(Collection<Integer> targets)
    {
        list_view_id.addAll(targets);
    }

    public int performAction(int action, int x, int y)
    {
        Log.i("ClickManager","performAction called");
        int id_r=-1;
        for (int id : list_view_id)
        {
            Log.i("ClickManager","id : "+id);
            ClickManagerCallback.ViewDataArgs vda = _callback.getViewData(id);

            Log.i("ClickManager","vda : "+vda.x+" "+vda.y + " "+vda.h + " "+vda.w);
            if( x>=vda.x && x<=vda.x+vda.w && y >=vda.y && y<=vda.y+vda.h )
            {
                _callback.performAction(action,id);
            }
            id_r=id;
        }

        return id_r; //returns id of the last clicked view
    }

}
