package org.horoyoii.algo;

import org.horoyoii.model.Peer;
import java.util.ArrayList;
import java.net.InetAddress;
import java.util.List;

public interface Algo {

    /**
     * get the upstream server based on the Load Balance algorithm.
     *
     * @param serverList
     * @param clientIp     TODO : this looks ugly.
     * @return
     */
    Peer getPeer(List<Peer> serverList, InetAddress clientIp);
}
