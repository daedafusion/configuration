package com.daedafusion.configuration.providers.util;

import com.daedafusion.jetcd.EtcdClient;
import com.daedafusion.jetcd.EtcdClientException;
import com.daedafusion.jetcd.EtcdClientFactory;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by mphilpot on 7/24/14.
 */
public class EtcdLoaderUtil
{
    private static final Logger log = Logger.getLogger(EtcdLoaderUtil.class);

    private EtcdClient client;

    public EtcdLoaderUtil()
    {
        client = EtcdClientFactory.newInstance();
    }

    public void loadProperties(String prefix, Properties properties, boolean deleteBeforeAdd) throws IOException
    {
        //client.createDirectory(prefix);

        if(deleteBeforeAdd)
        {
            try
            {
                client.deleteDirectoryRecursive(prefix);
            }
            catch(EtcdClientException e)
            {
                // OK, probably didn't already exist
            }
        }

        for(String key : properties.stringPropertyNames())
        {
            String value = properties.getProperty(key);

            if(value.startsWith("$$"))
            {
                InputStream in = EtcdLoaderUtil.class.getClassLoader().getResourceAsStream(value.substring("$$".length()));

                byte[] resource = IOUtils.toByteArray(in);

                in.close();

                client.set(String.format("%s%s", prefix, key), Base64.encodeBase64String(resource));
            }
            else
            {
                client.set(String.format("%s%s", prefix, key), value);
            }
        }
    }

}
