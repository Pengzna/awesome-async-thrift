package com.timecho.awesome.conf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

public class DNodeDescriptor {

  private static final Logger LOGGER = LoggerFactory.getLogger(DNodeDescriptor.class);

  private final DNodeConfig conf = new DNodeConfig();

  private void loadProperties() {
    URL propertiesUrl;
    String urlString = System.getenv(NodeConstant.DNODE_CONF) +
      File.separatorChar + NodeConstant.DNODE_CONFIG_FILE_NAME;
    try {
      propertiesUrl = new URL("file:" + urlString);
    } catch (MalformedURLException e) {
      LOGGER.error("Can't find config file: {}, use default configuration.", urlString);
      return;
    }

    try (InputStream inputStream = propertiesUrl.openStream()) {
      LOGGER.info("Start to read config file: {}", urlString);
      Properties properties = new Properties();
      properties.load(inputStream);

      conf.setDnRpcAddress(properties.getProperty(NodeConstant.DN_RPC_ADDRESS));
      conf.setDnRpcPort(Integer.parseInt(properties.getProperty(NodeConstant.DN_RPC_PORT)));

    } catch (IOException e) {
      LOGGER.warn("Error occurs when loading config file, use default config.", e);
    }
  }

  public DNodeConfig getConf() {
    return conf;
  }

  private DNodeDescriptor() {
    loadProperties();
  }

  public static DNodeDescriptor getInstance() {
    return DNodeDescriptorHolder.INSTANCE;
  }

  private static class DNodeDescriptorHolder {

    private static final DNodeDescriptor INSTANCE = new DNodeDescriptor();

    private DNodeDescriptorHolder() {
      // Empty constructor
    }
  }
}
