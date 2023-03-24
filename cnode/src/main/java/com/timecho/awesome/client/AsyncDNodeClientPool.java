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

import com.timecho.aweseme.thrift.TDNodeConfiguration;
import com.timecho.aweseme.thrift.TEndPoint;
import com.timecho.awesome.client.async.AsyncDNodeServiceClient;
import com.timecho.awesome.conf.CNodeDescriptor;
import com.timecho.awesome.exception.ClientManagerException;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class AsyncDNodeClientPool {

  private static final Logger LOGGER = LoggerFactory.getLogger(AsyncDNodeClientPool.class);

  private static final List<TEndPoint> WORKERS = CNodeDescriptor.getInstance().getConf().getWorkerDnList();

  private final IClientManager<TEndPoint, AsyncDNodeServiceClient> clientManager;

  private AsyncDNodeClientPool() {
    clientManager =
      new IClientManager.Factory<TEndPoint, AsyncDNodeServiceClient>()
        .createClientManager(
          new ClientPoolFactory.AsyncDNodeServiceClientPoolFactory());
  }

  public void activateClusterDNodes() {
    for (TEndPoint worker : WORKERS) {
      try (AsyncDNodeServiceClient client = clientManager.borrowClient(worker)) {
        client.activateDNode(new TDNodeConfiguration(), new EmptyAsyncHandler());
      } catch (ClientManagerException | TException e) {
        LOGGER.error("Error when executing activateDNode", e);
      }
    }
  }

  public void processIORequest() {
    for (TEndPoint worker : WORKERS) {
      try (AsyncDNodeServiceClient client = clientManager.borrowClient(worker)) {
        client.processIO(new EmptyAsyncHandler());
      } catch (ClientManagerException | TException e) {
        LOGGER.error("Error when executing processIORequest", e);
      }
    }
  }

  private static class AsyncDNodeClientPoolHolder {

    private static final AsyncDNodeClientPool INSTANCE = new AsyncDNodeClientPool();

    private AsyncDNodeClientPoolHolder() {
      // Empty constructor
    }
  }

  public static AsyncDNodeClientPool getInstance() {
    return AsyncDNodeClientPoolHolder.INSTANCE;
  }
}
