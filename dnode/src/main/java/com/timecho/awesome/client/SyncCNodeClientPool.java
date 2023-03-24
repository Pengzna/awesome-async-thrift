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

package com.timecho.awesome.client;

import com.timecho.aweseme.thrift.TEndPoint;
import com.timecho.awesome.client.sync.SyncCNodeServiceClient;
import com.timecho.awesome.conf.DNodeDescriptor;
import com.timecho.awesome.exception.ClientManagerException;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

public class SyncCNodeClientPool {

  private static final Logger LOGGER = LoggerFactory.getLogger(SyncCNodeClientPool.class);

  private final static TEndPoint cNode = DNodeDescriptor.getInstance().getConf().getCNode();

  private final IClientManager<TEndPoint, SyncCNodeServiceClient> clientManager;

  private SyncCNodeClientPool() {
    clientManager =
      new IClientManager.Factory<TEndPoint, SyncCNodeServiceClient>()
        .createClientManager(
          new ClientPoolFactory.SyncCNodeServiceClientPoolFactory());
  }

  public void ioRequest() {
    try (SyncCNodeServiceClient client = clientManager.borrowClient(cNode)) {
      client.ioRequest();
    } catch (TException | ClientManagerException e) {
      LOGGER.error("Error when executing ioRequest", e);
    }
  }

  public void cpuRequest() {
    try (SyncCNodeServiceClient client = clientManager.borrowClient(cNode)) {
      client.cpuRequest(new Random().nextInt());
    } catch (TException | ClientManagerException e) {
      LOGGER.error("Error when executing ioRequest", e);
    }
  }

  private static class SyncCNodeClientPoolHolder {

    private static final SyncCNodeClientPool INSTANCE = new SyncCNodeClientPool();

    private SyncCNodeClientPoolHolder() {
      // Empty constructor
    }
  }

  public static SyncCNodeClientPool getInstance() {
    return SyncCNodeClientPoolHolder.INSTANCE;
  }
}
