package drcyano.embedq.data;

import drcyano.embedq.exceptions.InvalidTopicStringException;
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
		assertFalse(Topic.matches(abcd, abed));
		assertTrue(Topic.matches(abcd, abcd2));
		assertFalse(Topic.matches(abcd, afd));
		assertTrue(Topic.matches(abcd, abzd));
		assertTrue(Topic.matches(abzd, abcd));
		assertFalse(Topic.matches(abcd, azd));
		assertFalse(Topic.matches(azd, abcd));
		assertTrue(Topic.matches(azd, afd));
		assertTrue(Topic.matches(afd, azd));
		assertFalse(Topic.matches(abzd,azd));
		assertFalse(Topic.matches(azd, abzd));
		assertFalse(Topic.matches(abc, abcd));
		assertFalse(Topic.matches(abcd, abc));
		assertTrue(Topic.matches(abc, azc));
		assertFalse(Topic.matches(abc, abc_));
		assertTrue(Topic.matches(abc, ab_));
		assertTrue(Topic.matches(abcd, ab_));
		assertFalse(Topic.matches(abc, abcz));
		assertTrue(Topic.matches(abc, abz));
		assertFalse(Topic.matches(abcd, abz));
		
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
