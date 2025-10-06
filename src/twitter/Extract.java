package twitter;

import java.time.Instant;
import java.util.*;
import java.util.regex.*;

/**
 * Methods to extract info from Tweets (Problem 1).
 */
public class Extract {

    /**
     * Return a Timespan whose start is the earliest timestamp in tweets
     * and whose end is the latest timestamp in tweets.
     *
     * Precondition: tweets is non-empty; if violated we throw IllegalArgumentException.
     */
    public static Timespan getTimespan(List<Tweet> tweets) {
        if (tweets == null || tweets.isEmpty()) {
            throw new IllegalArgumentException("tweets must be non-empty");
        }
        Instant start = tweets.get(0).getTimestamp();
        Instant end = start;

        for (Tweet t : tweets) {
            Instant ts = t.getTimestamp();
            if (ts.isBefore(start)) start = ts;
            if (ts.isAfter(end)) end = ts;
        }
        return new Timespan(start, end);
    }

    /**
     * Return the set of usernames mentioned in the tweets.
     *
     * This implementation returns usernames in lowercase (design choice).
     * A mention is '@' followed by one or more letters, digits, or underscores.
     */
    public static Set<String> getMentionedUsers(List<Tweet> tweets) {
        Set<String> mentioned = new HashSet<>();
        if (tweets == null) return mentioned;

        Pattern p = Pattern.compile("@([A-Za-z0-9_]+)");
        for (Tweet t : tweets) {
            String text = t.getText();
            if (text == null) continue;
            Matcher m = p.matcher(text);
            while (m.find()) {
                mentioned.add(m.group(1).toLowerCase());
            }
        }
        return mentioned;
    }
}
