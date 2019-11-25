package testapp.embedq;

import drcyano.embedq.broker.Broker;
import drcyano.embedq.broker.SimpleBroker;
import drcyano.embedq.connection.BrokerConnection;
import drcyano.embedq.client.Subscriber;
import drcyano.embedq.data.Message;
import drcyano.embedq.data.Topic;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class Test1 {
	
	public static void main(String[] o){
		// developer tests
		Broker b = new SimpleBroker();
		BrokerConnection bc = b.getConnection();
		Subscriber s1 = new Subscriber(){
			@Override public void receiveMessage(Message msg){
				ByteBuffer data = msg.getBytes();
				String s = Charset.forName("UTF-8").decode(data).toString();
				System.out.println("Temperature reading: "+s);
			}
		};
		
		
		Subscriber s2 = new Subscriber(){
			@Override public void receiveMessage(Message msg){
				ByteBuffer data = msg.getBytes();
				String s = Charset.forName("UTF-8").decode(data).toString();
				System.out.println("Log: "+s);
			}
		};
		
		
		bc.subscribe(s1, new Topic("data/temperature"));
		bc.subscribe(s2, new Topic("#"));
		Message m = Message.fromString("20.2C");
		bc.publish(m, new Topic("data/temperature"));
		bc.unsubscribe(s1);
	}
}
