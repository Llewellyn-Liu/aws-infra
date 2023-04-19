#!/bin/bash
mkdir ~/img1
sudo systemctl start mariadb
echo export AWS_DEFAULT_REGION="us-west-2" >> /home/ec2-user/.bash_profile
echo export S3_NAME=${aws_s3_bucket.s3-b-1.bucket} >> /home/ec2-user/.bash_profile

echo export DB_DRIVE="com.mysql.cj.jdbc.Driver" >> /home/ec2-user/.bashrc
echo export DB_URL="jdbc:mysql://terraform-20230411180652436600000002.cyigxjqcgi0g.us-west-2.rds.amazonaws.com/TestDev" >> /home/ec2-user/.bashrc
echo export DB_USERNAME="csye6225" >> /home/ec2-user/.bashrc
echo export DB_PASSWORD="liurunlin" >> /home/ec2-user/.bashrc


source ~/.bashrc

su - ec2-user -c "mkdir img"
su - ec2-user -c "java -jar app.jar &"

sudo /opt/aws/amazon-cloudwatch-agent/bin/amazon-cloudwatch-agent-ctl -a fetch-config -m ec2 -c file:/home/ec2-user/cloudwatch/config -s
