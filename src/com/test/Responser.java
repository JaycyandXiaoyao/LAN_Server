package com.test;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * 
 * @author congxiaoyao
 * @version 0.0.2
 */
public class Responser extends Thread {

	private int srcPort; // ����ȫ���㲥ʹ�õĶ˿ں�
	private int dstPort; // ��Ӧ������ipʹ�õĶ˿ں�

	private DatagramSocket socket;
	private DatagramPacket packet;

	private List<User> userList;

	/**
	 * 
	 * @param srcPort
	 *            ����ȫ���㲥ʹ�õĶ˿ں�
	 * @param dstPort
	 *            ��Ӧ������ipʹ�õĶ˿ں�
	 */
	public Responser(int srcPort, int dstPort) {

		this.srcPort = srcPort;
		this.dstPort = dstPort;
		userList = new ArrayList<User>();
		start();
	}

	@Override
	public void run() {
		while (true) {
			new MyOut();
			MyOut.println("running");

			try {
				packet = new DatagramPacket(new byte[1024], 1024, null, srcPort);
				socket = new DatagramSocket(srcPort);
				socket.receive(packet);
				socket.close();

				String massage = byteToString(packet.getData());

				if (!isRequest(massage.substring(0, 3)))
					continue;
				
				replytoClient((Inet4Address) packet.getAddress());
				
				recordUser(massage.substring(3, massage.length()), packet.getAddress().getHostAddress());
				

			} catch (IOException e) {
				new MyOut();
				MyOut.println(e.toString());
			}
		}
	}

	public void replytoClient(Inet4Address clientAddress) {

		new MyOut();
		MyOut.println("�ͻ���ip:" + clientAddress.getHostAddress());

		String reply = "002";
		packet = new DatagramPacket(reply.getBytes(), reply.length(),
				clientAddress, dstPort);

		try {
			socket = new DatagramSocket(dstPort);
			socket.send(packet);
			socket.close();
		} catch (IOException e) {
			new MyOut();
			MyOut.println(e.toString());
		}

	}
	
	private void recordUser(String username, String userIP) {
		for (User user : userList) {
			if (user.getUsername().equals(username)) {
				return;
			}
		}
		userList.add(new User(username, userIP, new Date()));

	}

	public static String byteToString(byte[] bs) {
		String result = new String(bs);
		return result.trim();
	}

	public boolean isRequest(String s) {
		return s.equals("001");
	}

	public List<User> getUserList() {
		return userList;
	}

	public void setUserList(List<User> userList) {
		this.userList = userList;
	}
}