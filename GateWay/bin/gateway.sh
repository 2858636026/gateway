#/bin/bash
. /data/br/conf/conf_env.sh

# 变量定义规则，如果没有可删除或将变量值设置为""，例如：
# jar包名称，也为app名
APP_TYPE="jar"       #jar|class|storm 目前支持三种方式启动
APP_NAME="GATEWAY"  #如果是jar方式启动，必须与jar文件名相同
CLASS_NAME=""        #如果是class方式启动，需要设置类名
STORM_NAME=""        #如果是storm方式启动，需要设置拓扑名
STORM_CLASS=""       #如果是storm方式启动，需要设置拓扑的类名
STORM_OPTS=""        #如果是storm方式启动，需要设置的其他参数
# java参数
#TEST_MODE="false"
#JAVA_OPTS="-Dtest.mode=$TEST_MODE -Xms2g -Xmx2g"
JAVA_OPTS="-Xms512m -Xmx512m -DJAR_NAME=${APP_NAME}"
# gc参数
GC_OPTS="-XX:+PrintGC -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -Xloggc:${BR_LOG_DIR}/${APP_NAME}/GC_${APP_NAME}.log"
# 私有配置文件
APP_CONF="-Djar_name=${APP_NAME} -Dconf_dir=${BR_CONF}/gateway-conf.xml"

#etl处理数据线程数(>=4)
#HANDLER_THREAD_COUNT=12
# 其他参数
OTHER_PARMS=""

# 调用服务方法（启动、停止、检查进程等）
doApp $1
