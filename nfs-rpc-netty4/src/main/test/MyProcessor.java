package main.test;

import code.google.nfs.rpc.server.ServerProcessor;

/**
 * Created by junwei on 15-5-11.
 */
public class MyProcessor implements ServerProcessor {
    public Object handle(Object request) throws Exception {
        return (String)request + " return by server";
    }
}
