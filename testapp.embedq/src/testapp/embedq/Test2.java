package testapp.embedq;

import drcyano.embedq.broker.Broker;
import drcyano.embedq.broker.SimpleBroker;
import drcyano.embedq.client.Publisher;
import drcyano.embedq.client.Subscriber;
import drcyano.embedq.connection.BrokerConnection;
import drcyano.embedq.data.Message;
import drcyano.embedq.data.Topic;
import testapp.embedq.test2.Worker;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Test2 {
	
	public static void main(String[] o){
		Broker b = new SimpleBroker();
		BrokerConnection bc = b.getConnection();
		ExecutorService threadPool = Executors.newFixedThreadPool(2);
		Worker baker = new Worker(new Topic("kitchen/dough"), bc, threadPool, System.err::println) {
			
			@Override
			protected void doWork(Message input, Publisher publisher) throws Exception {
				int doughBalls = input.getBytes().getInt();
				for(int i = 0; i < doughBalls; i++){
					Thread.sleep(1000);
					publisher.publishReliable(Message.fromString("Loaf of Bread"), new Topic("store/shelf"));
				}
			}
		};
		Subscriber logger = new Subscriber() {
			@Override
			public void receiveMessage(Message m) {
				System.out.println("Log: "+Message.toString(m));
			}
		};
		bc.subscribe(logger, new Topic("#"));
		
		for(int i = 1; i <= 3; i++) {
			bc.publishReliable(Message.fromInteger(i), new Topic("kitchen/dough"));
		}
		//
		try {
			Thread.sleep(10*1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.exit(0);
	}
	
	
}
