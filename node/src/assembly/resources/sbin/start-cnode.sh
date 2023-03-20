#!/bin/bash
#
# Licensed to the Apache Software Foundation (ASF) under one
# or more contributor license agreements.  See the NOTICE file
# distributed with this work for additional information
# regarding copyright ownership.  The ASF licenses this file
# to you under the Apache License, Version 2.0 (the
# "License"); you may not use this file except in compliance
# with the License.  You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing,
# software distributed under the License is distributed on an
# "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
# KIND, either express or implied.  See the License for the
# specific language governing permissions and limitations
# under the License.
#

echo ----------------------------
echo Starting CNode
echo ----------------------------

# find java in JAVA_HOME
if [ -n "$JAVA_HOME" ]; then
    for java in "$JAVA_HOME"/bin/amd64/java "$JAVA_HOME"/bin/java; do
        if [ -x "$java" ]; then
            JAVA="$java"
            break
        fi
    done
else
    JAVA=java
fi

if [ -z $JAVA ] ; then
    echo Unable to find java executable. Check JAVA_HOME and PATH environment variables.  > /dev/stderr
    exit 1;
fi

# Environmental variables
# shellcheck disable=SC2155
export NODE_HOME="$(dirname "$0")/.."
export NODE_CONF=${NODE_HOME}/conf
export NODE_LOG_DIR=${NODE_HOME}/logs
export NODE_LOG_CONFIG="${NODE_CONF}/logback-cnode.xml"

# ClassPath
CLASSPATH=""
for f in "${NODE_HOME}"/lib/*.jar; do
  CLASSPATH=${CLASSPATH}":"$f
done
classname=com.timecho.awesome.cnode.CNode

launch_service() {
    class="$1"
    node_params="-DNODE_HOME=${NODE_HOME}"
  	node_params="$node_params -DNODE_CONF=${NODE_CONF}"
  	node_params="$node_params -DNODE_LOG_DIR=${NODE_LOG_DIR}"
  	node_params="$node_params -Dlogback.configurationFile=${NODE_LOG_CONFIG}"

    "$JAVA" "$node_params" -cp "$CLASSPATH" "$class"

  	return $?

}

# Start up the service
launch_service "$classname"

exit $?
