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

public class NodeConstant {

  public static final String REQUEST_TYPE = "request_type";

  public static final String CNODE_RPC_ADDRESS = "cnode_rpc_address";
  public static final String CNODE_RPC_PORT = "cnode_rpc_port";
  public static final String CNODE_SERVER_TYPE = "cnode_server_type";
  public static final String CNODE_SELECTOR_NUM = "cnode_selector_num";
  public static final String CNODE_MAX_THREAD_POOL_SIZE = "cnode_max_thread_pool_size";

  public static final String DNODE_RPC_ADDRESS = "dnode_rpc_address";
  public static final String DNODE_RPC_PORT = "dnode_rpc_port";
  public static final String DNODE_REQUEST_NUM = "dnode_request_num";
  public static final String DNODE_CONCURRENT_CLIENT_NUM = "dnode_concurrent_client_num";

  public static final String CNODE = "CNODE";
  public static final String DNODE = "DNODE";

  public static final String CNODE_HOME = "CNODE_HOME";
  public static final String CNODE_CONF = "CNODE_CONF";
  public static final String CNODE_CONFIG_FILE_NAME = "cnode.properties";

  public static final String DNODE_HOME = "DNODE_HOME";
  public static final String DNODE_CONF = "DNODE_CONF";
  public static final String DNODE_CONFIG_FILE_NAME = "dnode.properties";

  public static final int THRIFT_DEFAULT_BUF_CAPACITY = 1024;
  public static final int THRIFT_FRAME_MAX_SIZE = 536870912;
  public static final int MAX_BUFFER_OVERSIZE_TIME = 5;
  public static final long MIN_SHRINK_INTERVAL = 60_000L;

  public static final String JMX_TYPE = "type";
  public static final String JMX_PORT = "com.sun.management.jmxremote.port";
}
