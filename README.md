[![Build Status](https://travis-ci.org/daedafusion/configuration.svg?branch=master)](https://travis-ci.org/daedafusion/configuration)

[![Coverage Status](https://coveralls.io/repos/github/daedafusion/configuration/badge.svg?branch=master)](https://coveralls.io/github/daedafusion/configuration?branch=master)

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
    <version>1.1</version>
</dependency
```

```xml
<dependency>
    <groupId>com.daedafusion</groupId>
    <artifactId>configuration</artifactId>
    <version>1.2-SNAPSHOT</version>
</dependency
```

## Examples

```java
Configuration.getInstance().getString("epiphyte")
InputStream in = Configuration.getInstance().getResource("log4j");
```