package net.pangu.test.basic;

public class HelloServiceImpl implements HelloService {

    public String say(String what) {
	return "Server Echo:" + what;
    }

}
