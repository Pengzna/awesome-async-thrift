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

import com.timecho.awesome.client.property.ClientPoolProperty;

public class ThriftConfig {

  private int selectorNumOfClientManager;

  private int coreClientNumForEachNode = ClientPoolProperty.DefaultProperty.CORE_CLIENT_NUM_FOR_EACH_NODE;
  private int maxClientNumForEachNode = ClientPoolProperty.DefaultProperty.MAX_CLIENT_NUM_FOR_EACH_NODE;

  public int getSelectorNumOfClientManager() {
    return selectorNumOfClientManager;
  }

  public void setSelectorNumOfClientManager(int selectorNumOfClientManager) {
    this.selectorNumOfClientManager = selectorNumOfClientManager;
  }

  public int getCoreClientNumForEachNode() {
    return coreClientNumForEachNode;
  }

  public void setCoreClientNumForEachNode(int coreClientNumForEachNode) {
    this.coreClientNumForEachNode = coreClientNumForEachNode;
  }

  public int getMaxClientNumForEachNode() {
    return maxClientNumForEachNode;
  }

  public void setMaxClientNumForEachNode(int maxClientNumForEachNode) {
    this.maxClientNumForEachNode = maxClientNumForEachNode;
  }
}
