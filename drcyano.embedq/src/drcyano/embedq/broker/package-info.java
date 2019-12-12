/**
 * This package contains the Broker abstract class and implementations thereof.
Example usage of EmbedQ:
<pre>{@code

import drcyano.embedq.broker.Broker;
import drcyano.embedq.broker.IntraprocessBroker;
import drcyano.embedq.client.Publisher;
import drcyano.embedq.client.Subscriber;
import drcyano.embedq.connection.BrokerConnection;
import drcyano.embedq.data.Message;
import drcyano.embedq.data.Topic;
import drcyano.embedq.util.Worker;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BakeBreadExample {
	
	public static void main(String[] o){
		
		// initialize a new message queue broker (intraprocess)
		Broker b = new IntraprocessBroker();
		BrokerConnection bc = b.getConnection();
		
		// setup a thread pool for the workers that will be listening
		// for data and responding with results
		ExecutorService threadPool = Executors.newFixedThreadPool(2);
		
		// add a worker that Subscribes to "kitchen/dough" and 
		// publishes "Loaf of Bread" based on the number it receives
		Worker baker = new Worker(new Topic("kitchen/dough"), bc, threadPool, System.err::println) {
			
			protected void doWork(Message input, Publisher publisher) throws Exception {
				int doughBalls = input.getBytes().getInt();
				for(int i = 0; i < doughBalls; i++){
					Thread.sleep(1000);
					publisher.publish(Message.fromString("Loaf of Bread", new Topic("store/shelf")));
				}
			}
		};
		
		// subscribe a logger to print all communications
		Subscriber logger = new Subscriber() {
			public void receiveMessage(Message m) {
				System.out.println(m.getTopic()+": "+m.heuristicToString());
			}
		};
		bc.subscribe(logger, new Topic("#"));
		
		// now publish some dough and watch the workers make bread
		for(int i = 1; i <= 3; i++) {
			bc.publish(Message.fromInt(i, new Topic("kitchen/dough")));
		}
		
		// wait a little before exiting
		try {
			Thread.sleep(10*1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.exit(0);
	}
}

}</pre>

Another example:
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
