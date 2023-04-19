#!/bin/bash
sudo yum -y install java-17-amazon-corretto-headless
sudo yum install -y mariadb-server
sudo yum install -y amazon-cloudwatch-agent

echo export DB_DRIVE="" >> ~/.bashrc
echo export DB_URL="" >> ~/.bashrc
echo export DB_USERNAME="" >> ~/.bashrc
echo export DB_PASSWORD="" >> ~/.bashrc
systemctl start mariadb

source ~/.bashrc

mkdir cloudwatch
touch ~/cloudwatch/config
touch ~/cloudwatch/cloudwatch.log


# sudo java -jar ~/app.jar