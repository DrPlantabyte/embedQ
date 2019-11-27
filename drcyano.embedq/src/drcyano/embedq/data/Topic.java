package drcyano.embedq.data;


import drcyano.embedq.exceptions.InvalidTopicStringException;

public class Topic implements Cloneable {
	
	private final String topicString;
	private final String[] levels;
	private final int hashCache;
	public Topic(String topicString) throws InvalidTopicStringException {
		this(topicString, true);
	}
	private Topic(String topicString, boolean validate) throws InvalidTopicStringException {
		this.topicString = topicString;
		this.hashCache = topicString.hashCode();
		if(validate){
			this.levels = validate(topicString);
		} else {
			this.levels = topicString.split("/");
		}
	}
	
	private static final String[] validate(String topicString) throws InvalidTopicStringException {
		if(topicString == null || topicString.length() == 0) {
			throw new InvalidTopicStringException("Empty strings not allowed");
		}
		if(topicString.charAt(0) == '/') {
			throw new InvalidTopicStringException("Topic string cannot start with a leading / (back-slash) character");
		}
		int indexOfMultiWildcard = topicString.indexOf('#');
		if(indexOfMultiWildcard >= 0 && indexOfMultiWildcard < topicString.length() - 1) {
			throw new InvalidTopicStringException("Multi-level wildcard # can only appear at end of topic string");
		}
		String[] levels = topicString.split("/");
		for (String level : levels){
			if((level.contains("+") || level.contains("#")) && level.length() > 1 ){
				throw new InvalidTopicStringException("A level's name cannot contain both wildcard and non-wildcard characters");
			} else if(level.length() == 0){
				throw new InvalidTopicStringException("A level's name cannot be empty");
			}
		}
		return levels;
	}
	
	@Override public int hashCode(){
		return hashCache;
	}
	
	@Override public boolean equals(Object other){
		if(this == other) return true;
		if(this.hashCode() == other.hashCode() && other instanceof Topic){
			Topic that = (Topic)other;
			return this.topicString.equals(that.topicString);
		}
		return false;
	}
	
	@Override public String toString(){
		return topicString;
	}
	
	@Override public Topic clone(){
		return new Topic(this.topicString, false);
	}
	
	public static boolean matches(Topic publisherTopic, Topic subscriberTopic){
		// MQTT spec: # for any (.* regex) and + for any within one level of heirarcy (/.*/ regex)
		final int levelLimit = Math.min(publisherTopic.levels.length, subscriberTopic.levels.length);
		final String multiWild = "#";
		for(int level = 0; level < levelLimit; level++){
			// check each level
			String publvl = publisherTopic.levels[level];
			String sublvl = subscriberTopic.levels[level];
			if(
					multiWild.equals(publvl)
							|| multiWild.equals(sublvl)) {
				return true;
			}
			if(!compare(publvl, sublvl)) return false;
		}
		// if levels are equal, and same number of them, then equal
		return publisherTopic.levels.length == subscriberTopic.levels.length;
		
	}
	
	public final boolean matches(Topic other){
		return Topic.matches(this,other);
	}
	
	private static boolean compare(String a, String b){
		if("+".equals(a) || "+".equals(b)) return true;
		return a.equals(b);
	}
}
