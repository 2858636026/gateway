#!/bin/sh
#################################
#  Version: 20180708
#  Author: GuoYi
#  Company: Bonree
#  Bonree Bash Shell
#################################

# 1.比较得瑟的提示框
function alert(){
  str=$1
  RES="\e[0m"
  COLOR_ALERT="\e[31;47m"
  COLOR_BG="\e[31;47m"
  getlen $str
  echo -e "${COLOR_BG}##########\c"
  for i in $(seq 1 $len)
  do
    echo -e "#\c"
  done
  echo -e "##########${RES}"
  echo -e "${COLOR_BG}#${RES}${COLOR_ALERT}         ${1}         ${RES}${COLOR_BG}#${RES}"
  echo -e "${COLOR_BG}##########\c"
  for i in $(seq 1 $len)
  do
    echo -e "#\c"
  done
  echo -e "##########${RES}"
}

#获取字符串长度，中文等于2字节，得瑟的提示框用。
function getlen(){
  str=$1|sed 's/ /1/g'
  len=0
  for i in `seq ${#str}`
  do
  s=`printf "%d" \'${str:$i-1:1}`
  if [ $s -gt 255 ];then
    len=$[len+2]
  else
    len=$[len+1]
  fi
  done
}

# 2.启停服务公共模块
# 检查jps进程
function checkJps(){
  str=$1
  sleep 2
  res=`jps -lm | grep "$str" | wc -l`
  if [ $res -gt 0 ];then
    echo "${str} x $res is running..."
  else
    echo "${str} is stoped..."
  fi
}
# 杀掉某个jps进程
function killJps(){
  str=$1
  jps -lm | grep "$str" | awk {'print$1'} | nohup xargs kill -9 > /dev/null 2>&1 &
  #checkJps $str
}

# 检查strom拓扑
function checkStorm(){
  str=$1
  sleep 2
  res=`jps -lm | grep "$str" | wc -l`
  if [ $res -gt 0 ];then
    echo "${str} x $res is running..."
  else
    echo "${str} is stoped..."
  fi
}

# 执行storm拓扑相关
function doStorm(){
  case $1 in
    status)
      checkStorm ${APP_NAME}
    ;;
    start)
      startStorm ${APP_NAME}
    ;;
    stop)
      stopStorm ${APP_NAME}
    ;;
    restart)
      restartStorm
    ;;
  esac
}

# 执行jar相关
function doApp(){
 case $1 in
    status)
      checkJps ${APP_NAME}
    ;;
    start)
      startApp ${APP_NAME}
    ;;
    stop)
      stopApp ${APP_NAME}
    ;;
    restart)
      restartApp
    ;;
    *)
      restartApp
    ;;
  esac
}

# 启动一个jar
function startApp(){
  case $APP_TYPE in
    storm)
      nohup storm jar ${BR_JAR}/${APP_NAME}.jar ${STORM_CLASS} ${APP_CONF} ${BR_ZK_HOST} ${STORM_NAME} ${STORM_OPTS} > /dev/null 2>&1
      #checkStorm
      echo "${STORM_NAME} is running..."
    ;;
    class)
      nohup java -Dzk=${BR_ZK_HOST} ${JAVA_OPTS} ${GC_OPTS} ${APP_CONF} -Dlog_dir=${BR_LOG_DIR}/${APP_NAME} -classpath ${BR_JAR}/${APP_NAME}.jar:${BR_JAR}/lib/* ${CLASS_NAME} ${OTHER_PARMS} > /dev/null 2>&1 &
      checkJps ${CLASS_NAME}
    ;;
    *)  #默认为jar方式启动
      nohup java -Dzk=${BR_ZK_HOST} ${JAVA_OPTS} ${GC_OPTS} ${APP_CONF} -Dconf.path=${BR_CONF}/ -Dlog_dir=${BR_LOG_DIR}/${APP_NAME} -jar ${BR_JAR}/${APP_NAME}.jar ${OTHER_PARMS} > /dev/null 2>&1 &
      echo "nohup java -Dzk=${BR_ZK_HOST} ${JAVA_OPTS} ${GC_OPTS} ${APP_CONF} -Dconf.path=${BR_CONF}/ -Dlog_dir=${BR_LOG_DIR}/${APP_NAME} -jar ${BR_JAR}/${APP_NAME}.jar ${OTHER_PARMS} > /dev/null 2>&1 &"
      checkJps ${APP_NAME}
    ;;
  esac
}
# 终结一个jar
function stopApp(){
    case ${APP_TYPE} in
    storm)
      nohup storm kill ${STORM_NAME} -w 1 > /dev/null 2>&1
      #checkStorm
      echo "${STORM_NAME} is stopped..."
    ;;
    class)
        killJps ${CLASS_NAME}
        checkJps ${CLASS_NAME}
    ;;
    *)  #默认为jar方式启动
        killJps ${APP_NAME}
        checkJps ${APP_NAME}
    ;;
    esac
}
# 重启一个jar
function restartApp(){
  stopApp ${APP_NAME}
  startApp ${APP_NAME}
}

# 3. 进度条
progress='#'
function prog()
{
        percent=$1
        progress_do_num=$(($percent/2))
        for ((progress_num=`echo $progress | wc -m`;$progress_num<$progress_do_num;progress_num+=1))
        do
                progress=#$progress
        done
        printf "Progress :[%-50s]%d%%\r" $progress $percent
}

