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

public class CNodeConfig {

  private RequestType requestType = RequestType.IO;

  private CNodeServerType cnodeCNodeServerType = CNodeServerType.ASYNC;
  private int cnodeSelectorNum = 4;
  private int cnodeMaxThreadPoolSize = 65535;

  private int dnodeRequestNum = 300;
  private int dnodeConcurrentClientNum = 20;

  public RequestType getRequestType() {
    return requestType;
  }

  public void setRequestType(RequestType requestType) {
    this.requestType = requestType;
  }

  public CNodeServerType getCnodeServerType() {
    return cnodeCNodeServerType;
  }

  public void setCnodeServerType(CNodeServerType cnodeCNodeServerType) {
    this.cnodeCNodeServerType = cnodeCNodeServerType;
  }

  public int getCnodeSelectorNum() {
    return cnodeSelectorNum;
  }

  public void setCnodeSelectorNum(int cnodeSelectorNum) {
    this.cnodeSelectorNum = cnodeSelectorNum;
  }

  public int getCnodeMaxThreadPoolSize() {
    return cnodeMaxThreadPoolSize;
  }

  public void setCnodeMaxThreadPoolSize(int cnodeMaxThreadPoolSize) {
    this.cnodeMaxThreadPoolSize = cnodeMaxThreadPoolSize;
  }

  public int getDnodeRequestNum() {
    return dnodeRequestNum;
  }

  public void setDnodeRequestNum(int dnodeRequestNum) {
    this.dnodeRequestNum = dnodeRequestNum;
  }

  public int getDnodeConcurrentClientNum() {
    return dnodeConcurrentClientNum;
  }

  public void setDnodeConcurrentClientNum(int dnodeConcurrentClientNum) {
    this.dnodeConcurrentClientNum = dnodeConcurrentClientNum;
  }

}
