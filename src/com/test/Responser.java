package com.test;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;

/**
 * @author congxiaoyao
 * @version 0.0.6
 */
public class Responser extends Thread {

	private int srcPort; // ����ȫ���㲥ʹ�õĶ˿ں�
	private int dstPort; // ��Ӧ������ipʹ�õĶ˿ں�

	private DatagramSocket socket;
	private DatagramPacket packet;
	
	/**
	 * @param srcPort ����ȫ���㲥ʹ�õĶ˿ں�
	 * @param dstPort ��Ӧ������ipʹ�õĶ˿ں�
	 */
	public Responser(int srcPort, int dstPort) {

		this.srcPort = srcPort;
		this.dstPort = dstPort;
		start();
	}

	@Override
	public void run() {
		while (true) {
			
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

			} catch (IOException e) {
				
				MyOut.println(e.toString());
			}
		}
	}

	public void replytoClient(Inet4Address clientAddress) {
		
		MyOut.println("�ͻ���ip:" + clientAddress.getHostAddress());

		String reply = "002";
		packet = new DatagramPacket(reply.getBytes(), reply.length(),
				clientAddress, dstPort);

		try {
			socket = new DatagramSocket(dstPort);
			socket.send(packet);
			socket.close();
		} catch (IOException e) {
			MyOut.println(e.toString());
		}
	}

	public static String byteToString(byte[] bs) {
		String result = new String(bs);
		return result.trim();
	}

	public boolean isRequest(String s) {
		return s.equals("001");
	}

}