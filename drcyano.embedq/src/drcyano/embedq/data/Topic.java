package drcyano.embedq.data;

public class Topic {
	
	private final String topicString;
	public Topic(String topicString) {
		this.topicString = topicString;
	}
	
	public static boolean publishToSubscriber(Topic publishTopic, Topic subscribeTopic){
		// MQTT spec: # for any (.* regex) and + for any within one level of heirarcy (/.*/ regex)
		throw new UnsupportedOperationException("Not implemented yet!");
	}
}
