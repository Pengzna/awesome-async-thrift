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

package com.timelab.awesome.client;

import com.timecho.aweseme.thrift.IService;
import com.timecho.aweseme.thrift.Service;
import com.timecho.aweseme.thrift.TEndPoint;
import java.io.IOException;
import org.apache.thrift.async.TAsyncClientManager;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.transport.TNonblockingSocket;
import org.apache.thrift.transport.TTransportException;

public class AsyncServiceClient extends Service.AsyncClient {

  private static final int CONNECTION_TIMEOUT = 20_000;

  private final TEndPoint endpoint;
  private final ClientManager clientManager;

  public AsyncServiceClient(TProtocolFactory protocolFactory,
      TAsyncClientManager clientManager,
      TEndPoint endpoint,
      ClientManager clientManager1) throws IOException {
    super(protocolFactory, clientManager, wrapper(endpoint.getHost(), endpoint.getPort()));
    this.endpoint = endpoint;
    this.clientManager = clientManager1;
  }


  private static TNonblockingSocket wrapper(String host, int port) throws IOException {
    try {
      return new TNonblockingSocket(host, port, CONNECTION_TIMEOUT);
    } catch (TTransportException e) {
      // never happen
      return null;
    }
  }
}
