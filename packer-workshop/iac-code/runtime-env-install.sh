#!/bin/bash
sudo yum -y install java-17-amazon-corretto-headless
sudo yum install -y mariadb-server

echo export DB_DRIVE="com.mysql.cj.jdbc.Driver" >> ~/.bashrc
echo export DB_URL="" >> ~/.bashrc
echo export DB_USERNAME="" >> ~/.bashrc
echo export DB_PASSWORD="" >> ~/.bashrc
systemctl start mariadb

source ~/.bashrc


#下行代码移动到创建实例时生成，以确保ami环境遇到异常
# sudo java -jar ~/app.jar