package org.horoyoii.serverSelect;

import java.util.ArrayList;
import org.horoyoii.model.Info;


/*
    Select a server based on weight.
    
    curPosition : 마지막에 사용된 서버 위치
    curCnt      : 그 서버의 사용 횟수
*/
public class WeightedRoundRobin implements ServerSelector{
    private int curPosition = 0;
    private int curCnt   = 0;
    
    @Override
    public Info getServer(ArrayList<Info> serverList){ 

        if( curCnt < serverList.get(curPosition).getWeight() ){
            curCnt++;
        }else{
            curPosition = (curPosition + 1) % serverList.size();
            curCnt = 1;
        }

        return serverList.get(curPosition);
    }

}
