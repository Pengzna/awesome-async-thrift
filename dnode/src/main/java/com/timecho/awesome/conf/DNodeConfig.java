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

import com.timecho.aweseme.thrift.TEndPoint;

public class DNodeConfig {

  private TEndPoint cNode = new TEndPoint("127.0.0.1", 10710);

  private RequestType requestType = RequestType.IO;
  private int dnRequestNum = 1000;

  private String dnRpcAddress = "127.0.0.1";
  private int dnRpcPort = 6667;

  private int dnMaxConcurrentClientNum = 65535;

  public TEndPoint getCNode() {
    return cNode;
  }

  public void setCNode(TEndPoint cNode) {
    this.cNode = cNode;
  }

  public RequestType getRequestType() {
    return requestType;
  }

  public void setRequestType(RequestType requestType) {
    this.requestType = requestType;
  }

  public int getDnRequestNum() {
    return dnRequestNum;
  }

  public void setDnRequestNum(int dnRequestNum) {
    this.dnRequestNum = dnRequestNum;
  }

  public String getDnRpcAddress() {
    return dnRpcAddress;
  }

  public void setDnRpcAddress(String dnRpcAddress) {
    this.dnRpcAddress = dnRpcAddress;
  }

  public int getDnRpcPort() {
    return dnRpcPort;
  }

  public void setDnRpcPort(int dnRpcPort) {
    this.dnRpcPort = dnRpcPort;
  }

  public int getDnMaxConcurrentClientNum() {
    return dnMaxConcurrentClientNum;
  }

  public void setDnMaxConcurrentClientNum(int dnMaxConcurrentClientNum) {
    this.dnMaxConcurrentClientNum = dnMaxConcurrentClientNum;
  }
}
