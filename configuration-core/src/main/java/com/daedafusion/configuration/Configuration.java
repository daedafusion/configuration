package com.daedafusion.configuration;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mphilpot on 6/23/14.
 */
public class Configuration
{
    private static final Logger        log         = Logger.getLogger(Configuration.class);

    private static       Configuration ourInstance = new Configuration();

    public static Configuration getInstance()
    {
        return ourInstance;
    }

    private List<ConfigurationProvider> providers;

    protected Configuration()
    {
        providers = new ArrayList<ConfigurationProvider>();

        loadProviders(System.getProperty("configurationProviders",
                "com.daedafusion.configuration.providers.SystemPropertiesProvider," +
                        "com.daedafusion.configuration.providers.ClasspathPropertiesProvider"));
    }

    private void loadProviders(String providersString)
    {
        String[] providerArray = providersString.split(",");

        for(String p : providerArray)
        {
            try
            {
                log.info(String.format("Loading Configuration Provider :: %s", p));

                ConfigurationProvider cp = (ConfigurationProvider) Class.forName(p).newInstance();
                cp.init();

                providers.add(cp);
            }
            catch (ClassNotFoundException | InstantiationException | IllegalAccessException e)
            {
                log.warn("Could not load provider class", e);
            }
            catch (IOException e)
            {
                log.warn("Could not initialize provider");
            }
        }
    }

    private String lookupKey(String key)
    {
        for(ConfigurationProvider cp : providers)
        {
            String value = null;
            try
            {
                value = cp.getValue(key);
            }
            catch (IOException e)
            {
                log.warn(String.format("Error retrieving value for \"%s\" from provider (%s)", key, cp.getName()), e);
            }

            if(value != null)
            {
                return value;
            }
        }

        return null;
    }

    public Boolean getBoolean(String key)
    {
        String value = lookupKey(key);

        return value != null ? Boolean.parseBoolean(value) : null;
    }

    public boolean getBoolean(String key, boolean defaultValue)
    {
        String value = lookupKey(key);

        return value != null ? Boolean.parseBoolean(value) : defaultValue;
    }

    public Long getLong(String key)
    {
        String value = lookupKey(key);

        return value != null ? Long.valueOf(value) : null;
    }

    public Long getLong(String key, Long defaultValue)
    {
        String value = lookupKey(key);

        return value != null ? Long.valueOf(value) : defaultValue;
    }

    public Integer getInteger(String key)
    {
        String value = lookupKey(key);

        return value != null ? Integer.valueOf(value) : null;
    }

    public Integer getInteger(String key, Integer defaultValue)
    {
        String value = lookupKey(key);

        return value != null ? Integer.valueOf(value) : defaultValue;
    }

    public String getString(String key)
    {
        return lookupKey(key);
    }

    public String getString(String key, String defaultValue)
    {
        String value = lookupKey(key);

        return value != null ? value : defaultValue;
    }

    public InputStream getResource(String key)
    {
        InputStream is = null;

        String value = lookupKey(key);

        if(value == null)
        {
            // Nothing to load
        }
        else if(value.startsWith("classpath:"))
        {
            is = Configuration.class.getClassLoader().getResourceAsStream(value.substring("classpath:".length()));

        }
        else if(value.startsWith("file://"))
        {
            try
            {
                is = new FileInputStream(new File(URI.create(value)));
            }
            catch (FileNotFoundException e)
            {
                log.info(String.format("File %s not found from key :: %s", value, key), e);
            }
        }
        else if(value.startsWith("http://") || value.startsWith("https://"))
        {
            throw new UnsupportedOperationException("http resource loading not yet implemented");
        }
        else
        {
            is = new ByteArrayInputStream(Base64.decodeBase64(value));
        }

        return is;
    }

}
