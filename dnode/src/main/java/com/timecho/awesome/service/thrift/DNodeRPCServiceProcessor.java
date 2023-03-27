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

package com.timecho.awesome.service.thrift;

import com.timecho.aweseme.thrift.IDNodeRPCService;
import com.timecho.aweseme.thrift.TDNodeConfiguration;
import com.timecho.awesome.client.SyncCNodeClientPool;
import com.timecho.awesome.conf.DNodeConfig;
import com.timecho.awesome.conf.DNodeDescriptor;
import com.timecho.awesome.conf.RequestType;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class DNodeRPCServiceProcessor implements IDNodeRPCService.Iface {

  private static final Logger LOGGER = LoggerFactory.getLogger(DNodeRPCServiceProcessor.class);

  private final static DNodeConfig CONF = DNodeDescriptor.getInstance().getConf();

  @Override
  public void activateDNode(TDNodeConfiguration configuration) {
    final int requestNum = CONF.getDnRequestNum();
    final RequestType requestType = CONF.getRequestType();
    for (int request = 0; request < requestNum; request++) {
      switch (requestType) {
        case CPU:
          SyncCNodeClientPool.getInstance().cpuRequest();
        case IO:
        default:
          SyncCNodeClientPool.getInstance().ioRequest();
      }
    }
  }

  @Override
  public void processIO() {
    try {
      // Randomly sleeping for [500, 1000) ms
      TimeUnit.MILLISECONDS.sleep(500 + new Random().nextInt(500));
    } catch (InterruptedException e) {
      LOGGER.warn("Error when executing processIO", e);
    }
  }
}
