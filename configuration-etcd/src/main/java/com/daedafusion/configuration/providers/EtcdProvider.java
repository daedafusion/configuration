package com.daedafusion.configuration.providers;

import com.daedafusion.configuration.ConfigurationProvider;
import com.daedafusion.jetcd.EtcdClient;
import com.daedafusion.jetcd.EtcdClientFactory;
import com.daedafusion.jetcd.EtcdResult;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.URI;

/**
 * Created by mphilpot on 6/23/14.
 */
public class EtcdProvider implements ConfigurationProvider
{
    private static final Logger log = Logger.getLogger(EtcdProvider.class);

    private final String etcdHost;
    private final int    etcdPort;
    private final String globalPrefix;
    private final String servicePrefix;

    private final String serviceName;

    private static class EtcdClientObjectFactory extends BasePooledObjectFactory<EtcdClient>
    {
        private URI uri;

        public EtcdClientObjectFactory(URI uri)
        {
            this.uri = uri;
        }

        @Override
        public EtcdClient create() throws Exception
        {
            return EtcdClientFactory.newInstance(uri.toString());
        }

        @Override
        public PooledObject<EtcdClient> wrap(EtcdClient etcdClient)
        {
            return new DefaultPooledObject<>(etcdClient);
        }
    }

    private GenericObjectPool<EtcdClient> pool;

    public EtcdProvider()
    {
        etcdHost = System.getProperty("etcdHost", "localhost");
        etcdPort = Integer.getInteger("etcdPort", 4001);
        globalPrefix = System.getProperty("etcdGlobalPrefix", "/etc/global/");
        servicePrefix = System.getProperty("etcdServicePrefix", "/etc/services/");
        serviceName = System.getProperty("serviceName");
    }

    @Override
    public String getName()
    {
        return "etcd caching Configuration Provider";
    }

    @Override
    public void init() throws IOException
    {
        pool = new GenericObjectPool<EtcdClient>(new EtcdClientObjectFactory(URI.create(String.format("http://%s:%d", etcdHost, etcdPort))));
        pool.setLifo(false);
    }

    @Override
    public String getValue(String key) throws IOException
    {
        EtcdClient client = null;
        try
        {
            client = pool.borrowObject();
            EtcdResult result;

            // Look in service scope first
            if (serviceName != null)
            {
                String k = String.format("%s%s/%s", servicePrefix, serviceName, key);

                result = client.get(k);

                if (result != null && result.getNode() != null)
                {
                    return result.getNode().getValue();
                }
            }

            // Look in global space
            String k = String.format("%s%s", globalPrefix, key);
            result = client.get(k);

            if (result != null && result.getNode() != null)
            {
                return result.getNode().getValue();
            }
        }
        catch (Exception e)
        {
            throw new IOException(e);
        }
        finally
        {
            if (client != null)
            {
                try
                {
                    pool.returnObject(client);
                }
                catch (Exception e)
                {
                    log.warn("", e);
                }
            }
        }

        return null;
    }
}
