package cmu.practicum;



import java.net.UnknownHostException;

import org.jgroups.Address;
import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;
import org.jgroups.View;

public class JgroupsTest extends ReceiverAdapter {
	
	static JChannel channel;
	
	private void start() throws Exception {

	    channel=new JChannel();
	    channel.setReceiver(this);
	    channel.setDiscardOwnMessages(true);
	    channel.connect("VehicleCluster");
	    
	}
	
	private static void close() throws Exception {
		if(channel!=null){
			channel.close();
		}

	}
	

	private void sendMessage(String message) {
	            Message msg=new Message(null, null, message);

	            try {
					channel.send(msg);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

	       
	}
	
	public void viewAccepted(View new_view) {

	    System.out.println("** view: " + new_view);

	}


	public void receive(Message msg) {

	    System.out.println(msg.getSrc() + ": " + msg.getObject().toString());

	}
	
	
	public static void main (String[] args){
		JgroupsTest jmsRPC= new JgroupsTest();
		try {
			jmsRPC.start();
			while (true){
			jmsRPC.sendMessage("From Host " + java.net.InetAddress.getLocalHost().getHostName());
			}
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}


