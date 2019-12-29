#!/bin/bash

function install()
{
  echo "install $username@$host"

  scp $HOME/deploy/pkg/gateway/aifgw-backend-1.0.tar.gz $username@$host:~

	scp $HOME/deploy/shell/gateway/deploy.sh $username@$host:~
	ssh -n $username@$host "sh deploy.sh;rm -f deploy.sh"
}

cat hostlist|while read line
   do
	  host=`echo "$line"|awk '{print $1}'`
	  username=`echo "$line"|awk '{print $2}'`
	  password=`echo "$line"|awk '{print $3}'`
	  install
   done
