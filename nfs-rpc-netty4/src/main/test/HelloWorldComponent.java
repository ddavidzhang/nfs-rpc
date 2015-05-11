package main.test;

/**
 * Created by junwei on 15-5-11.
 */
public class HelloWorldComponent implements HelloWorldService{
    public String sayHello(String word){
        return word + " return by server rpc";
    }
}
