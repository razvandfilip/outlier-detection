#!/usr/bin/env bash
sleep 70s
for PUBLISHER_ID in 1 2 3 4 5
do
  URL="http://localhost:8080/outliers?publisherId=$PUBLISHER_ID&windowSize=100"
  RESULT=`wget -qO- ${URL}`
  grep "Publisher id $PUBLISHER_ID produced outlier value" /app/logs/outlier-detection/outlier-detection.log | grep -o '[^ ]\+$' | while read -r line ; do
    FOUND="`echo "$RESULT" | grep -o "$line" | wc -l`"
    if [ $FOUND == 1 ]
    then
      echo "Outlier $line found in /outliers response"
    else
      echo "Outlier $line not found in /outliers response"
    fi
  done
done