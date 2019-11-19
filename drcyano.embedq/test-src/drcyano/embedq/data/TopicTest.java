package drcyano.embedq.data;

import org.junit.Test;


import static org.junit.Assert.*;

public class TopicTest {
	
	
	@Test
	public void equalityTest(){
		Topic abc = new Topic("a/b/c");
		Topic abc_ = new Topic("a/b/c/#");
		Topic abcz = new Topic("a/b/c/+");
		Topic abcd = new Topic("a/b/c/d");
		Topic abcd2 = new Topic("a/b/c/d");
		Topic abed = new Topic("a/b/e/d");
		Topic abce = new Topic("a/b/c/e");
		assertEquals(abcd, abcd2);
		assertEquals(abcd.hashCode(), abcd2.hashCode());
		assertNotEquals(abcd, abc);
		assertNotEquals(abcd, abc_);
		assertNotEquals(abcd, abcz);
		assertNotEquals(abcd, abed);
		assertNotEquals(abcd, abce);
		assertNotEquals(abcd.hashCode(), abce.hashCode());
	}
	@Test
	public void matchTest(){
		Topic abc = new Topic("a/b/c");
		Topic abc_ = new Topic("a/b/c/#");
		Topic abcz = new Topic("a/b/c/+");
		Topic ab_ = new Topic("a/b/#");
		Topic abz = new Topic("a/b/+");
		Topic abcd = new Topic("a/b/c/d");
		Topic abcd2 = new Topic("a/b/c/d");
		Topic abed = new Topic("a/b/e/d");
		Topic afd = new Topic("a/f/d");
		assertThrows(InvalidTopicStringException.class,()-> new Topic("a/b/#/d"));
		assertThrows(InvalidTopicStringException.class,()-> new Topic("a/b/c#/d"));
		assertThrows(InvalidTopicStringException.class,()-> new Topic("a/b/c+/d"));
		assertThrows(InvalidTopicStringException.class,()-> new Topic("a/b/#c/d"));
		assertThrows(InvalidTopicStringException.class,()-> new Topic("a/b/+c/d"));
		assertThrows(InvalidTopicStringException.class,()-> new Topic(""));
		Topic abzd = new Topic("a/b/+/d");
		assertThrows(InvalidTopicStringException.class,()-> new Topic("a/#/d"));
		Topic azd = new Topic("a/+/d");
		Topic azc = new Topic("a/+/c");
		assertFalse(Topic.matchPublisherToSubscriber(abcd, abed));
		assertTrue(Topic.matchPublisherToSubscriber(abcd, abcd2));
		assertFalse(Topic.matchPublisherToSubscriber(abcd, afd));
		assertTrue(Topic.matchPublisherToSubscriber(abcd, abzd));
		assertTrue(Topic.matchPublisherToSubscriber(abzd, abcd));
		assertFalse(Topic.matchPublisherToSubscriber(abcd, azd));
		assertFalse(Topic.matchPublisherToSubscriber(azd, abcd));
		assertTrue(Topic.matchPublisherToSubscriber(azd, afd));
		assertTrue(Topic.matchPublisherToSubscriber(afd, azd));
		assertFalse(Topic.matchPublisherToSubscriber(abzd,azd));
		assertFalse(Topic.matchPublisherToSubscriber(azd, abzd));
		assertFalse(Topic.matchPublisherToSubscriber(abc, abcd));
		assertFalse(Topic.matchPublisherToSubscriber(abcd, abc));
		assertTrue(Topic.matchPublisherToSubscriber(abc, azc));
		assertFalse(Topic.matchPublisherToSubscriber(abc, abc_));
		assertTrue(Topic.matchPublisherToSubscriber(abc, ab_));
		assertTrue(Topic.matchPublisherToSubscriber(abcd, ab_));
		assertFalse(Topic.matchPublisherToSubscriber(abc, abcz));
		assertTrue(Topic.matchPublisherToSubscriber(abc, abz));
		assertFalse(Topic.matchPublisherToSubscriber(abcd, abz));
		
	}
	
	static void assertThrows(Class exceptionClass, UnsafeRunnable function){
		boolean good = false;
		try{
			function.run();
		} catch (Exception ex){
			if(ex.getClass().equals(exceptionClass)){
				// success!
				good = true;
			} else {
				throw new RuntimeException("Unexpected exception: "+ex.getLocalizedMessage(),ex);
			}
		}
		assertTrue("Expected "+exceptionClass.getCanonicalName()+" exception, but none was thrown",good);
	}
	
	interface UnsafeRunnable{
		public void run() throws Exception;
	}
}
