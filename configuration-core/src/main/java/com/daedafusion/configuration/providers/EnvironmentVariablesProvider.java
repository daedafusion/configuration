package com.daedafusion.configuration.providers;

import com.daedafusion.configuration.ConfigurationProvider;
import org.apache.log4j.Logger;

import java.io.IOException;

/**
 * Created by mphilpot on 6/30/14.
 */
public class EnvironmentVariablesProvider implements ConfigurationProvider
{
    private static final Logger log = Logger.getLogger(EnvironmentVariablesProvider.class);

    @Override
    public String getName()
    {
        return EnvironmentVariablesProvider.class.getName();
    }

    @Override
    public void init() throws IOException
    {

    }

    @Override
    public String getValue(String key) throws IOException
    {
        return System.getenv().get(key);
    }
}
