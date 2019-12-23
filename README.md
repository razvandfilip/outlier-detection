# outlier-detection

## Overview

First clone the project, then build, start and run test with:
```
sudo docker build -t outlier-detection . && sudo docker run -it -d --name outlier-container outlier-detection && sudo docker exec -it outlier-container/app/run.sh
```

The run.sh script will output detected outliers.

## App

The SpringBoot app is built with gradle in a temporary image during the build of the docker image.

The test data generator, the message consumer and the outlier retrieval endpoint are all built into the same SpringBoot app.
Zookeeper, Kafka and MongoDB are installed on the same container.
These components should normally be kept separate using different containers.

The outlier detection uses the median absolute deviation calculated a window of values.
Values are selected as outliers if they differ from the window median with more than 5 times the median absolute deviation.
This approach was selected because no other information was available regarding the distribution of reading values.

Random data is generated at the application startup for 1 minute, using 5 different publisher ids.
One out of 10 values are generated as outliers and this event is logged.
The test script searches the log for generated outliers and greps them to the response of the /outliers?publisherId service to
check if the application correctly identifies them. 