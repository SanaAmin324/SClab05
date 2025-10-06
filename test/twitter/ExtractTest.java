package twitter;

import static org.junit.Assert.*;
import org.junit.Test;
import java.time.Instant;
import java.util.*;

/**
 * Testing strategy
 * ----------------
 * Partitioning for getTimespan():
 *  - single tweet (start == end)
 *  - multiple tweets with earliest and latest times (test with ordered and unordered lists)
 *
 * Partitioning for getMentionedUsers():
 *  - no mentions
 *  - multiple mentions in one or more tweets
 *  - mentions with punctuation and mixed case
 *
 * Tests are small and legal clients of the spec. Because the spec allows
 * underdetermined casing, tests normalize usernames to lowercase where needed.
 */

public class ExtractTest {

    @Test
    public void testGetTimespanSingleTweet() {
        Instant t = Instant.parse("2015-02-17T10:00:00Z");
        Tweet tweet = new Tweet(1, "alice", "Hi", t);

        Timespan span = Extract.getTimespan(Arrays.asList(tweet));
        assertEquals("start must equal timestamp", t, span.getStart());
        assertEquals("end must equal timestamp", t, span.getEnd());
    }

    @Test
    public void testGetTimespanTwoTweetsUnordered() {
        Instant t1 = Instant.parse("2015-02-17T10:00:00Z");
        Instant t2 = Instant.parse("2015-02-17T11:00:00Z");

        // unordered input: later tweet first
        Tweet later = new Tweet(2, "bob", "later", t2);
        Tweet earlier = new Tweet(1, "alice", "earlier", t1);

        Timespan span = Extract.getTimespan(Arrays.asList(later, earlier));
        assertEquals(t1, span.getStart());
        assertEquals(t2, span.getEnd());
    }

    @Test
    public void testGetMentionedUsersVarious() {
        Tweet t1 = new Tweet(1, "x", "hey @Bob and @alice.", Instant.parse("2015-02-17T10:00:00Z"));
        Tweet t2 = new Tweet(2, "y", "no mentions here", Instant.parse("2015-02-17T11:00:00Z"));
        Tweet t3 = new Tweet(3, "z", "@BOB! @charlie", Instant.parse("2015-02-17T12:00:00Z"));

        Set<String> mentions = Extract.getMentionedUsers(Arrays.asList(t1, t2, t3));

        // tests treat returned usernames case-insensitively by normalizing to lowercase
        Set<String> lower = new HashSet<>();
        for (String s : mentions) lower.add(s.toLowerCase());

        Set<String> expected = new HashSet<>(Arrays.asList("bob", "alice", "charlie"));
        assertEquals(expected, lower);
    }
}
