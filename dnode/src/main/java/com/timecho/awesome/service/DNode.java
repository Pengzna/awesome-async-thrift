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

import com.timecho.awesome.conf.NodeConstant;
import com.timecho.awesome.service.thrift.DNodeRPCService;
import com.timecho.awesome.thrift.JMXService;
import com.timecho.awesome.thrift.ServiceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DNode implements DNodeMBean {

  private static final Logger LOGGER = LoggerFactory.getLogger(DNode.class);
  private final String mbeanName =
    String.format(
      "%s:%s=%s",
      this.getClass().getPackage(), NodeConstant.JMX_TYPE, NodeConstant.DNODE);

  private final RegisterManager registerManager = new RegisterManager();

  public static void main(String[] args) {
    LOGGER.info("Hello, I'm DNode!");
    DNode.getInstance().doMain();
  }

  private void doMain() {
    setUpJMXService();
    setUpRPCService();
  }

  private void setUpJMXService() {
    // Setup JMXService
    registerManager.register(new JMXService());
    JMXService.registerMBean(this, mbeanName);
    LOGGER.info("Successfully setup {}.", ServiceType.JMX_SERVICE.getName());
  }

  private void setUpRPCService() {
    DNodeRPCService dNodeRPCService = new DNodeRPCService();
    registerManager.register(dNodeRPCService);
    LOGGER.info("Successfully setup {}.", ServiceType.DNODE_SERVICE.getName());
  }

  private DNode() {
    // Empty constructor
  }

  private static class DNodeHolder {

    private static final DNode INSTANCE = new DNode();

    private DNodeHolder() {
      // Empty constructor
    }
  }

  public static DNode getInstance() {
    return DNodeHolder.INSTANCE;
  }
}