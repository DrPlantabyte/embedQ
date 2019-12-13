package drcyano.embedq.data;
/*
This file is part of EmbedQ.

EmbedQ is free software: you can redistribute it and/or modify
it under the terms of the GNU Lesser General Public License as
published by the Free Software Foundation, either version 3 of
the License, or (at your option) any later version.

EmbedQ is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with EmbedQ.  If not, see <https://www.gnu.org/licenses/>.
 */

import drcyano.embedq.exceptions.InvalidTopicStringException;

/**
 * A topic is an identifier used for publishing and subscribing to messages. A topic is basically a
 * '/' slash delimited topic levels, much like a URL. A level can be replaced with a + or #
 * wildcard. A + indicates anything at this level (single-level wildcard) and a # indicates anything
 * from this level and later (multi-level wildcard). For example, a subscriber to topic
 * 'sensor/+/data' would receive a message published to 'sensor/temperature/data' but not
 * 'sensor/temperature/backup/data', but a subscriber to 'sensor/#' would receive both messages.
 * You cannot mix wildcard and non-wildcard characters within a level and the # wildcard must be the
 * last level.
 */
public class Topic implements Cloneable {
	
	private final String topicString;
	private final String[] levels;
	private final int hashCache;
	
	/**
	 * Standard constructor for <code>Topic</code>.
	 * @param topicString The topic String
	 * @throws InvalidTopicStringException Thrown if the rules of the topic levels are violated.
	 */
	public Topic(String topicString) throws InvalidTopicStringException {
		this(topicString, true);
	}
	
	/**
	 * Private constructor that may bypasses validation for copy operations
	 * @param topicString Topic String
	 * @param validate if <code>false</code>, the string will not be checked for wildcard violations
	 * @throws InvalidTopicStringException Thrown if the rules of the topic levels are violated and
	 * <code>validate</code> is <code>true</code>.
	 */
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
	
	/**
	 * hash code override. Topics with identical topic strings are considered equivalent
	 * @return a hash code
	 */
	@Override public int hashCode(){
		return hashCache;
	}
	
	/**
	 * Topics with identical topic strings are considered equivalent
	 * @param other Another object to compare to
	 * @return <code>true</code> if <code>other</code> is another <code>Topic</code> with an equal
	 * topic string (case sensitive), <code>false</code> otherwise
	 */
	@Override public boolean equals(Object other){
		if(this == other) return true;
		if(this.hashCode() != other.hashCode()) return false;
		//
		if(other instanceof Topic){
			Topic that = (Topic)other;
			return this.topicString.equals(that.topicString);
		}
		return false;
	}
	
	/**
	 * Returns the topic string
	 * @return the topic String
	 */
	@Override public String toString(){
		return topicString;
	}
	
	/**
	 * Deep-copy clone
	 * @return a new Topic instance
	 */
	@Override public Topic clone(){
		return new Topic(this.topicString, false);
	}
	
	/**
	 * Compares two <code>Topic</code> instances and returns <code>true</code> if publishing to one
	 * would send the message to a subscriber to the other (includes wildcard rules)
	 * @param publisherTopic publish topic
	 * @param subscriberTopic subscriber topic
	 * @return <code>true</code> if a message published to <code>publisherTopic</code> should be
	 * routed to a subscriber to <code>subscriberTopic</code>
	 */
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
	
	/**
	 * Same as <code>Topic.matches(this, other)</code>. See {@link #matches(Topic, Topic)}
	 * @param other subscriber topic
	 * @return <code>true</code> if a message published to <code>publisherTopic</code> should be
	 * routed to a subscriber to <code>subscriberTopic</code>
	 */
	public final boolean matches(Topic other){
		return Topic.matches(this,other);
	}
	
	private static boolean compare(String a, String b){
		if("+".equals(a) || "+".equals(b)) return true;
		return a.equals(b);
	}
}
