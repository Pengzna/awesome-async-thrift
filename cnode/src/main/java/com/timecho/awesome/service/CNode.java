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

package com.timecho.awesome.service;

import com.timecho.awesome.conf.CNodeConfig;
import com.timecho.awesome.conf.NodeConstant;
import com.timecho.awesome.conf.CNodeDescriptor;
import com.timecho.awesome.exception.StartupException;
import com.timecho.awesome.service.thrift.CNodeRPCService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CNode {

  private static final Logger LOGGER = LoggerFactory.getLogger(CNode.class);
  private final String mbeanName =
    String.format(
      "%s:%s=%s",
      this.getClass().getPackage(), NodeConstant.JMX_TYPE, NodeConstant.CNODE);

  private final RegisterManager registerManager = new RegisterManager();

  private static final CNodeConfig CONF = CNodeDescriptor.getInstance().getConf();

  public static void main(String[] args) {
    LOGGER.info("Activating {}...", NodeConstant.CNODE);
    CNode.getInstance().activate();
  }

  private void activate() {
    try {
      setUpJMXService();
      setUpRPCService();
    } catch (StartupException e) {
      LOGGER.error("Meet error when startup.", e);
      deactivate();
    }

    logConfigurations();
  }

  private void setUpJMXService() throws StartupException {
    registerManager.register(new JMXService());
    JMXService.registerMBean(this, mbeanName);
    LOGGER.info("Successfully setup {}.", JMXService.ServiceType.JMX_SERVICE.getName());
  }

  private void setUpRPCService() throws StartupException {
    CNodeRPCService cNodeRPCService = new CNodeRPCService(CONF.getCnServerType());
    registerManager.register(cNodeRPCService);
    LOGGER.info("Successfully setup {}.", JMXService.ServiceType.DNODE_SERVICE.getName());
  }

  private void logConfigurations() {
    LOGGER.info("This test will run in the following configurations:");
    LOGGER.info(String.format("\t %s: %s", NodeConstant.REQUEST_TYPE, CONF.getRequestType()));
    LOGGER.info(String.format("\t %s: %s", NodeConstant.CN_SERVER_TYPE, CONF.getCnServerType()));
    LOGGER.info(String.format("\t %s: %s", NodeConstant.CN_SELECTOR_NUM, CONF.getCnSelectorNum()));
    LOGGER.info(String.format("\t %s: %s", NodeConstant.CN_MAX_THREAD_POOL_SIZE, CONF.getCnMaxThreadPoolSize()));
    LOGGER.info(String.format("\t %s: %s", NodeConstant.DN_REQUEST_NUM, CONF.getDnRequestNum()));
    LOGGER.info(String.format("\t %s: %s", NodeConstant.DN_MAX_CONCURRENT_CLIENT_NUM, CONF.getDnConcurrentClientNum()));
  }

  private void deactivate() {
    LOGGER.warn("Deactivating {}...", NodeConstant.CNODE);
    registerManager.deregisterAll();
    JMXService.deregisterMBean(mbeanName);
    LOGGER.info("{} is deactivated.", NodeConstant.CNODE);
    System.exit(-1);
  }

  private CNode() {
    // Empty constructor
  }

  private static class CNodeHolder {

    private static final CNode INSTANCE = new CNode();

    private CNodeHolder() {
      // Empty constructor
    }
  }

  public static CNode getInstance() {
    return CNodeHolder.INSTANCE;
  }
}