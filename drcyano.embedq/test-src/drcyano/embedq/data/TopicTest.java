package drcyano.embedq.data;

import org.junit.Test;
import static org.junit.Assert.*;

public class TopicTest {
	@Test
	public void matchTest(){
		Topic abc = new Topic("a/b/c");
		Topic abcd = new Topic("a/b/c/d");
		Topic abcd2 = new Topic("a/b/c/d");
		Topic abed = new Topic("a/b/e/d");
		Topic afd = new Topic("a/f/d");
		Topic ab_d = new Topic("a/b/#/d");
		Topic abzd = new Topic("a/b/+/d");
		Topic a_d = new Topic("a/#/d");
		Topic azd = new Topic("a/+/d");
		assertFalse(Topic.publishToSubscriber(abcd, abed));
		assertTrue(Topic.publishToSubscriber(abcd, abcd2));
		assertFalse(Topic.publishToSubscriber(abcd, afd));
		assertTrue(Topic.publishToSubscriber(abcd, ab_d));
		assertTrue(Topic.publishToSubscriber(ab_d, abcd));
		assertTrue(Topic.publishToSubscriber(abcd, abzd));
		assertTrue(Topic.publishToSubscriber(abzd, abcd));
		assertTrue(Topic.publishToSubscriber(abcd, a_d));
		assertTrue(Topic.publishToSubscriber(a_d, abcd));
		assertFalse(Topic.publishToSubscriber(abcd, azd));
		assertFalse(Topic.publishToSubscriber(azd, abcd));
		assertTrue(Topic.publishToSubscriber(azd, afd));
		assertTrue(Topic.publishToSubscriber(afd, azd));
		assertFalse(Topic.publishToSubscriber(abzd,azd));
		assertTrue(Topic.publishToSubscriber(abzd, a_d));
		assertFalse(Topic.publishToSubscriber(azd, abzd));
		assertTrue(Topic.publishToSubscriber(a_d, abzd));
		assertFalse(Topic.publishToSubscriber(abc, abcd));
		assertTrue(Topic.publishToSubscriber(abc, a_d));
		assertTrue(Topic.publishToSubscriber(abc, azd));
		
	}
}
