package org.horoyoii;

import org.horoyoii.greeter.Greeter;

public class App {

    public String getGreeting() {
        return "Hello world.";
    }

    public static void main(String[] args) {
        System.out.println(new App().getGreeting());
                
        Greeter greeter = new Greeter();
        greeter.init();
        greeter.run();

    }
}
