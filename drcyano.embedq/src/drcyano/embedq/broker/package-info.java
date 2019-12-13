/**
 * This package contains the Broker abstract class and implementations thereof.
Example usage of EmbedQ:

<pre>{@code

import drcyano.embedq.broker.Broker;
import drcyano.embedq.broker.IntraprocessBroker;
import drcyano.embedq.connection.BrokerConnection;
import drcyano.embedq.client.Subscriber;
import drcyano.embedq.data.Message;
import drcyano.embedq.data.Topic;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class SimpleTest {
	
	public static void main(String[] o){
		// make a new Broker
		Broker b = new IntraprocessBroker();
		BrokerConnection bc = b.getConnection();
		
		// make some subscribers
		Subscriber s1 = (Message msg)->{
				ByteBuffer data = msg.getBytes();
				String s = Charset.forName("UTF-8").decode(data).toString();
				System.out.println("Temperature reading: "+s);
		};
		
		Subscriber s2 = (Message msg)->{
				ByteBuffer data = msg.getBytes();
				String s = Charset.forName("UTF-8").decode(data).toString();
				System.out.println("Log: "+s);
		};
		
		// make some topics
		Topic temperatureData = new Topic("data/temperature");
		Topic pressureData = new Topic("data/pressure");
		Topic all = new Topic("#");
		bc.subscribe(s1, temperatureData);
		bc.subscribe(s2, all);
		
		// publish some messages
		Message m = Message.fromString("20.2C", temperatureData);
		bc.publish(m);
		bc.publish(Message.fromString("996.2hPa", pressureData));
		
		// remove the temperature listener
		bc.unsubscribeAll(s1);
		bc.publish(Message.fromString("20.4C", temperatureData));
		bc.publish(Message.fromString("996.8hPa", pressureData));
		// done
	}
}
}</pre>

 */

package drcyano.embedq.broker;
