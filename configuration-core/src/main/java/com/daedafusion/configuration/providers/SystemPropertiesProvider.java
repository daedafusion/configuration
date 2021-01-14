package com.daedafusion.configuration.providers;

import com.daedafusion.configuration.ConfigurationProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * Created by mphilpot on 6/30/14.
 */
public class SystemPropertiesProvider implements ConfigurationProvider
{
    private static final Logger log = LogManager.getLogger(SystemPropertiesProvider.class);

    @Override
    public String getName()
    {
        return SystemPropertiesProvider.class.getName();
    }

    @Override
    public void init() throws IOException
    {

    }

    @Override
    public String getValue(String key) throws IOException
    {
        return System.getProperty(key);
    }
}
