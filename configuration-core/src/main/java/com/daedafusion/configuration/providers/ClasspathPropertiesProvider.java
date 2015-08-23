package com.daedafusion.configuration.providers;

import com.daedafusion.configuration.ConfigurationProvider;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by mphilpot on 6/23/14.
 */
public class ClasspathPropertiesProvider implements ConfigurationProvider
{
    private static final Logger log = Logger.getLogger(ClasspathPropertiesProvider.class);

    private Properties properties;

    public ClasspathPropertiesProvider()
    {
        properties = new Properties();
    }


    @Override
    public String getName()
    {
        return ClasspathPropertiesProvider.class.getName();
    }

    @Override
    public void init() throws IOException
    {
        String file = System.getProperty("classpathPropertiesFile", "system.properties");

        InputStream is = ClasspathPropertiesProvider.class.getClassLoader().getResourceAsStream(file);

        if(is == null)
        {
            throw new IOException(String.format("Unable to load properties file :: %s", file));
        }

        properties.load(is);
    }

    @Override
    public String getValue(String key) throws IOException
    {
        return properties.getProperty(key);
    }
}
