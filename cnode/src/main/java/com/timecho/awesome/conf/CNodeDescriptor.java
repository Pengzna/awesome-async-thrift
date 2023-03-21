/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.timecho.awesome.conf;

import com.timecho.awesome.exception.BadNodeUrlException;
import com.timecho.awesome.exception.ConfigurationException;
import com.timecho.awesome.utils.NodeUrlUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

public class CNodeDescriptor {

  private static final Logger LOGGER = LoggerFactory.getLogger(CNodeDescriptor.class);

  private final CNodeConfig conf = new CNodeConfig();

  private void loadProperties() {
    URL propertiesUrl;
    String urlString = System.getenv(NodeConstant.CNODE_CONF) +
      File.separatorChar + NodeConstant.CNODE_CONFIG_FILE_NAME;
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

      conf.setCnRpcAddress(
        properties
          .getProperty(NodeConstant.CN_RPC_ADDRESS, conf.getCnRpcAddress())
          .trim());

      conf.setCnRpcPort(
        Integer.parseInt(
          properties
            .getProperty(
              NodeConstant.CN_RPC_PORT, String.valueOf(conf.getCnRpcPort()))
            .trim()));

      String dnUrls = properties.getProperty(NodeConstant.WORKER_DN_LIST);
      if (dnUrls != null) {
        try {
          conf.setWorkerDnList(NodeUrlUtils.parseTEndPointUrls(dnUrls));
        } catch (BadNodeUrlException e) {
          throw new ConfigurationException(e.getMessage());
        }
      }

      conf.setRequestType(
        RequestType.parse(
          properties
            .getProperty(NodeConstant.REQUEST_TYPE, conf.getRequestType().getRequestType())
            .trim()));

      conf.setCnServerType(
        CNodeServerType.parse(
          properties
            .getProperty(NodeConstant.CN_SERVER_TYPE, conf.getCnServerType().getServerType())
            .trim()));

      conf.setCnSelectorNum(
        Integer.parseInt(
          properties
            .getProperty(NodeConstant.CN_SELECTOR_NUM, String.valueOf(conf.getCnSelectorNum()))
            .trim()));

      conf.setDnRequestNum(
        Integer.parseInt(
          properties
            .getProperty(NodeConstant.DN_REQUEST_NUM, String.valueOf(conf.getDnRequestNum()))
            .trim()));


      conf.setDnConcurrentClientNum(Integer.parseInt(properties.getProperty(NodeConstant.DN_MAX_CONCURRENT_CLIENT_NUM)));


      conf.setCnMaxThreadPoolSize(Integer.parseInt(properties.getProperty(NodeConstant.CN_MAX_THREAD_POOL_SIZE)));

    } catch (IOException | ConfigurationException e) {
      LOGGER.warn("Error occurs when loading config file, use default config.", e);
    }
  }

  public CNodeConfig getConf() {
    return conf;
  }

  private CNodeDescriptor() {
    loadProperties();
  }

  public static CNodeDescriptor getInstance() {
    return CNodeDescriptorHolder.INSTANCE;
  }

  private static class CNodeDescriptorHolder {

    private static final CNodeDescriptor INSTANCE = new CNodeDescriptor();

    private CNodeDescriptorHolder() {
      // Empty constructor
    }
  }
}
