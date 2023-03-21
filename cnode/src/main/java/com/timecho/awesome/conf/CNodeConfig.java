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

import java.util.Collections;
import java.util.List;

public class CNodeConfig {

  private String cnRpcAddress = "127.0.0.1";
  private int cnRpcPort = 10710;

  private List<TEndPoint> workerDnList =
    Collections.singletonList(new TEndPoint("127.0.0.1", 6667));


  private RequestType requestType = RequestType.IO;

  private CNodeServerType cnServerType = CNodeServerType.ASYNC;
  private int cnSelectorNum = 4;

  private int dnRequestNum = 1000;



  private int cnMinConcurrentClientNum = Runtime.getRuntime().availableProcessors();
  private int cnMaxConcurrentClientNum = 65535;

  private int dnConcurrentClientNum = 65535;


  private int cnMaxThreadPoolSize = 65535;

  public int getCnMinConcurrentClientNum() {
    return cnMinConcurrentClientNum;
  }

  public void setCnMinConcurrentClientNum(int cnMinConcurrentClientNum) {
    this.cnMinConcurrentClientNum = cnMinConcurrentClientNum;
  }

  public int getCnMaxConcurrentClientNum() {
    return cnMaxConcurrentClientNum;
  }

  public void setCnMaxConcurrentClientNum(int cnMaxConcurrentClientNum) {
    this.cnMaxConcurrentClientNum = cnMaxConcurrentClientNum;
  }

  public String getCnRpcAddress() {
    return cnRpcAddress;
  }

  public void setCnRpcAddress(String cnRpcAddress) {
    this.cnRpcAddress = cnRpcAddress;
  }

  public int getCnRpcPort() {
    return cnRpcPort;
  }

  public void setCnRpcPort(int cnRpcPort) {
    this.cnRpcPort = cnRpcPort;
  }

  public RequestType getRequestType() {
    return requestType;
  }

  public void setRequestType(RequestType requestType) {
    this.requestType = requestType;
  }

  public CNodeServerType getCnServerType() {
    return cnServerType;
  }

  public void setCnServerType(CNodeServerType cnodeCNodeServerType) {
    this.cnServerType = cnodeCNodeServerType;
  }

  public int getCnSelectorNum() {
    return cnSelectorNum;
  }

  public void setCnSelectorNum(int cnSelectorNum) {
    this.cnSelectorNum = cnSelectorNum;
  }

  public int getCnMaxThreadPoolSize() {
    return cnMaxThreadPoolSize;
  }

  public void setCnMaxThreadPoolSize(int cnMaxThreadPoolSize) {
    this.cnMaxThreadPoolSize = cnMaxThreadPoolSize;
  }

  public int getDnRequestNum() {
    return dnRequestNum;
  }

  public void setDnRequestNum(int dnRequestNum) {
    this.dnRequestNum = dnRequestNum;
  }

  public int getDnConcurrentClientNum() {
    return dnConcurrentClientNum;
  }

  public void setDnConcurrentClientNum(int dnConcurrentClientNum) {
    this.dnConcurrentClientNum = dnConcurrentClientNum;
  }

  public List<TEndPoint> getWorkerDnList() {
    return workerDnList;
  }

  public void setWorkerDnList(List<TEndPoint> workerDnList) {
    this.workerDnList = workerDnList;
  }
}
