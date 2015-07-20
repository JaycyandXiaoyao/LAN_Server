package com.test;

public class TestCase {

	public static void main(String[] args){
		
		new Responser(65535,65534);
		
		MsgReceiver receiver = new MsgReceiver(60000);
		
		while(true)
		{
			MyOut.println(receiver.receive());
		}
	}
}
