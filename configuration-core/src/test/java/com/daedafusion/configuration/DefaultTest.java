package com.daedafusion.configuration;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by mphilpot on 6/30/14.
 */
public class DefaultTest
{
    private static final Logger log = LogManager.getLogger(DefaultTest.class);

    @Test
    public void testBasic()
    {
        log.info("Test logging");
        assertThat(Configuration.getInstance().getString("epiphyte"), is("avi"));
        assertThat(Configuration.getInstance().getString("doesn'texist"), is(nullValue()));

        System.setProperty("foo", "bar");

        assertThat(Configuration.getInstance().getString("foo"), is("bar"));
    }

    @Test
    public void testDefault()
    {
        assertThat(Configuration.getInstance().getString("epiphyte", "randy"), is("avi"));
        assertThat(Configuration.getInstance().getString("doesn'texist", "yesitdoes"), is("yesitdoes"));
    }

    @Test
    public void testResource() throws IOException
    {
        InputStream in = Configuration.getInstance().getResource("log4j");

        assertThat(in, is(notNullValue()));

        String contents = IOUtils.toString(in);

        assertThat(contents.contains("PatternLayout"), is(true));

        in = Configuration.getInstance().getResource("unknown");

        assertThat(in, is(nullValue()));
    }
}
