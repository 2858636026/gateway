#!/bin/bash
#放置公共变量、系统环境变量
export BR_DIR="/data/br"             #标准实施目录，无需修改
export BR_CONF="$BR_DIR/conf"           #配置文件目录，无需修改
export BR_JAR="$BR_DIR/jar"             #jar文件目录，无需修改
export BR_LOG_DIR="$BR_DIR/logs"        #日志目录，无需修改

#ZK主机，用逗号分隔
export BR_ZK_HOST=Prd-Comm-ZK01,Prd-Comm-ZK02,Prd-Comm-ZK03,Prd-Comm-ZK04,Prd-Comm-ZK05

source /etc/profile               #引用系统环境变量
source ${BR_DIR}/shell/common.sh
