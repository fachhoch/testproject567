package com.gae.yotube;

import org.htmlparser.tags.CompositeTag;

public class EmbedTag extends CompositeTag {
	
	
    /**
     * The set of names handled by this tag.
     */
    private static final String[] mIds = new String[] {"embed"};

    /**
     * The set of end tag names that indicate the end of this tag.
     */
    private static final String[] mEndTagEnders = new String[] {"BODY", "HTML"};

    /**
     * Create a new div tag.
     */
    public EmbedTag ()
    {
    }

    /**
     * Return the set of names handled by this tag.
     * @return The names to be matched that create tags of this type.
     */
    public String[] getIds ()
    {
        return (mIds);
    }

    /**
     * Return the set of end tag names that cause this tag to finish.
     * @return The names of following end tags that stop further scanning.
     */
    public String[] getEndTagEnders ()
    {
        return (mEndTagEnders);
    }
}
