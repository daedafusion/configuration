package com.daedafusion.configuration;

import java.io.IOException;

/**
 * Created by mphilpot on 6/23/14.
 */
public interface ConfigurationProvider
{
    /**
     * @return Name of the provider
     */
    String getName();

    /**
     * Initialize backing store
     * @throws IOException
     */
    void init() throws IOException;

    /**
     *
     * @param key
     * @return Value found in configuration store, null if not found
     * @throws IOException if there was a problem accessing backing store
     */
    String getValue(String key) throws IOException;
}
