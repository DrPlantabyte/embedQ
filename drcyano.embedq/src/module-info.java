/**
 * EmbedQ is a simple message-queue library optimized for <b>intra-</b>process communication.
 * <p>Why?</p>
 * <p>Because microservice design principles are useful even in self-contained computer programs,
 * and there are very few options right now for embeddable message queues to support such
 * microservice designs.</p>
 * <p>Example EmbedQ usage:</p>
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
 */
module drcyano.embedq {
	exports drcyano.embedq.broker;
	exports drcyano.embedq.client;
	exports drcyano.embedq.connection;
	exports drcyano.embedq.data;
	exports drcyano.embedq.exceptions;
	exports drcyano.embedq.util;
}
