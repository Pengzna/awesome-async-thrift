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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CNode {

  private static final Logger LOGGER = LoggerFactory.getLogger(CNode.class);

  private static final CNodeConfig CONF = CNodeDescriptor.getInstance().getConf();

  public static void main(String[] args) {
    LOGGER.info("Hello, I'm CNode!");
    CNode.getInstance().doMain();
  }

  private void doMain() {
    logConfigurations();
  }

  private void logConfigurations() {
    LOGGER.info("This test will run in the following configurations:");
    LOGGER.info(String.format("\t %s: %s", NodeConstant.REQUEST_TYPE, CONF.getRequestType()));
    LOGGER.info(String.format("\t %s: %s", NodeConstant.CNODE_SERVER_TYPE, CONF.getCnodeServerType()));
    LOGGER.info(String.format("\t %s: %s", NodeConstant.CNODE_SELECTOR_NUM, CONF.getCnodeSelectorNum()));
    LOGGER.info(String.format("\t %s: %s", NodeConstant.CNODE_MAX_THREAD_POOL_SIZE, CONF.getCnodeMaxThreadPoolSize()));
    LOGGER.info(String.format("\t %s: %s", NodeConstant.DNODE_REQUEST_NUM, CONF.getDnodeRequestNum()));
    LOGGER.info(String.format("\t %s: %s", NodeConstant.DNODE_CONCURRENT_CLIENT_NUM, CONF.getDnodeConcurrentClientNum()));
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