[![Build Status](https://travis-ci.org/daedafusion/configuration.svg?branch=master)](https://travis-ci.org/daedafusion/configuration)

# configuration

Plugable configuration framework. Allows a heirarchy of providers (e.g. use system props, then etcd, then config file).

Default providers are:

    System Properties -> Classpath
    
Pass ```-DconfigurationProviders``` as a comma separated list of classes to set configuration

## Maven

```xml
<dependency>
    <groupId>com.daedafusion</groupId>
    <artifactId>configuration</artifactId>
    <version>1.0</version>
</dependency
```

## Examples

```java
Configuration.getInstance().getString("epiphyte")
InputStream in = Configuration.getInstance().getResource("log4j");
```