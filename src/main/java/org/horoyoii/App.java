/*
 * 
 */

package org.horoyoii;

import org.horoyoii.loadbalance.LoadBalancer;

public class App {

    public String getGreeting() {
        return "Hello world.";
    }

    public static void main(String[] args) {
        System.out.println(new App().getGreeting());
                
        LoadBalancer loadBalancer = new LoadBalancer();
        loadBalancer.init();
        loadBalancer.run();

    }
}
