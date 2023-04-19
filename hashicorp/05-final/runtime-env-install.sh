#!/bin/bash
sudo yum -y install java-17-amazon-corretto-headless
sudo yum install -y mariadb-server

echo export DB_DRIVE="com.mysql.cj.jdbc.Driver" >> ~/.bashrc
echo export DB_URL="" >> ~/.bashrc
echo export DB_USERNAME="" >> ~/.bashrc
echo export DB_PASSWORD="" >> ~/.bashrc
systemctl start mariadb

source ~/.bashrc



# sudo java -jar ~/app.jar