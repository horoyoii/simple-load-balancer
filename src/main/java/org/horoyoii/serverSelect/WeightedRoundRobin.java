package org.horoyoii.serverSelect;

import java.util.ArrayList;
import org.horoyoii.model.Info;


/*


*/
public class WeightedRoundRobin implements ServerSelector{
    private int curPosition = 0;
    private int curCnt   = 0;
    
    ArrayList<Info>     serverList      = null;
    ArrayList<Integer>  serverWeight    = null;
        
    @Override
    public Info getServer(ArrayList<Info> serverList){ 

        if( curCnt < serverWeight.get(curPosition) ){
            curCnt++;
        }else{
            curPosition = (curPosition + 1) % serverList.size();
            curCnt = 1;
        }

        return serverList.get(curPosition);
    }

}
