package com.daedafusion.configuration.providers;

import com.daedafusion.configuration.ConfigurationProvider;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by mphilpot on 6/23/14.
 */
public class FilesystemPropertiesProvider implements ConfigurationProvider
{
    private static final Logger log = LogManager.getLogger(FilesystemPropertiesProvider.class);

    private Properties properties;

    public FilesystemPropertiesProvider()
    {
        properties = new Properties();
    }


    @Override
    public String getName()
    {
        return FilesystemPropertiesProvider.class.getName();
    }

    @Override
    public void init() throws IOException
    {
        String file = System.getenv().getOrDefault("filesystemPropertiesFile", System.getProperty("filesystemPropertiesFile", "/etc/system.properties"));

        InputStream is = FileUtils.openInputStream(new File(file));

        properties.load(is);

        is.close();
    }

    @Override
    public String getValue(String key) throws IOException
    {
        return properties.getProperty(key);
    }
}
