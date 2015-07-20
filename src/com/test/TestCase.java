package com.test;

public class TestCase {

	public static void main(String[] args){
		
		Responser responser = new Responser(65535,65534);
		
		MsgReceiver receiver = new MsgReceiver(60000);
		
		while(true)
		{
			new MyOut().println(receiver.receive());
			new MyOut().println(responser.getUserList().get(0));
		}
	}
}
