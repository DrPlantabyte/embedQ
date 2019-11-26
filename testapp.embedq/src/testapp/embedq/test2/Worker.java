package testapp.embedq.test2;

import drcyano.embedq.client.Publisher;
import drcyano.embedq.connection.BrokerConnection;
import drcyano.embedq.data.Message;
import drcyano.embedq.data.Topic;

import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

public abstract class Worker implements drcyano.embedq.client.Subscriber {
	
	private final ExecutorService scheduler;
	private final Publisher publisher;
	private final Consumer<Exception> exceptionHandler;
	private final Topic listenTopic;
	
	public Worker(Topic listenTopic, BrokerConnection brokerConnection, ExecutorService threadPool, Consumer<Exception> exceptionHandler) {
		this.scheduler = threadPool;
		this.publisher = new PublisherWrapper(brokerConnection);
		this.exceptionHandler = exceptionHandler;
		this.listenTopic = listenTopic;
		
		//
		brokerConnection.subscribe(this, listenTopic);
	}
	
	protected abstract void doWork(Message input, Publisher publisher) throws Exception;
	
	private void workHandler(Message m){
		try {
			doWork(m, publisher);
		} catch (Exception e) {
			if(exceptionHandler != null) exceptionHandler.accept(e);
		}
	}
	
	@Override
	public void receiveMessage(Message m) {
		scheduler.submit(()->workHandler(m));
	}
	
	private class PublisherWrapper implements Publisher{
		private final Publisher target;
		public PublisherWrapper(Publisher target){
			this.target = target;
		}
		
		
		@Override
		public void publishReliable(Message m) {
			target.publishReliable(m);
		}
		
		@Override
		public void publishFast(Message m) {
			target.publishFast(m);
		}
	}
}
