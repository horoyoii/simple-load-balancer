package org.horoyoii.algo;

import java.util.ArrayList;
import java.net.InetAddress;
import java.util.List;

import org.horoyoii.model.Peer;


/*
    Select a server based on weight.
    
    curPosition : 마지막에 사용된 서버 위치
    curCnt      : 그 서버의 사용 횟수
*/
public class WeightedRoundRobin implements Algo {
    private int curPosition = 0;
    private int curCnt   = 0;
    
    @Override
    public Peer getPeer(List<Peer> serverList, InetAddress clientIp){

        if( curCnt < serverList.get(curPosition).getWeight() ){
            curCnt++;
        }else{
            curPosition = (curPosition + 1) % serverList.size();
            curCnt = 1;
        }

        return serverList.get(curPosition);
    }

}
