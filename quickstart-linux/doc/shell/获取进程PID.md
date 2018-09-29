pid=`ps ax | grep -i 'com.alibaba.rocketmq.broker.BrokerStartup' |grep java |grep msg_broker_master_19 | grep -v grep | awk '{print $1}'`
pid=`ps ax |grep java | grep -v grep | awk '{print $1}'`
if [ -z "$pid" ] ; then
   echo "No mqbroker running."
   exit -1;
fi


ps -ef|grep ${PROCESS_NAME} | grep ${PROCESS_PARM} | grep java | grep -v grep | awk '{print $2}' |while read pid
do
    echo $pid
done
