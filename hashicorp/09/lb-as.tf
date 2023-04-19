# resource "aws_placement_group" "autoscaling-placement-group" {
#   name     = "autoscaling-placement-group"
#   strategy = "cluster"
# }

# resource "aws_launch_configuration" "asg_launch_config" {
#   name_prefix                 = "asg_launch_config-"
#   image_id                    = var.default-ami
#   instance_type               = "t2.micro"
#   key_name                    = var.default-key-pair-name
#   associate_public_ip_address = true
#   iam_instance_profile        = aws_iam_instance_profile.role-instance-profile.name
#   security_groups = [var.instance-sg
#   , ]

#   user_data = <<EOF
# #!/bin/bash
# mkdir ~/img1
# sudo systemctl start mariadb
# echo export AWS_DEFAULT_REGION="us-west-2" >> /home/ec2-user/.bash_profile
# echo export S3_NAME=${aws_s3_bucket.s3-b-1.bucket} >> /home/ec2-user/.bash_profile

# echo export DB_DRIVE="${var.env-db-drive}" >> /home/ec2-user/.bashrc
# echo export DB_URL="jdbc:mysql://${aws_db_instance.default.address}/TestDev" >> /home/ec2-user/.bashrc
# echo export DB_USERNAME="${var.env-db-username}" >> /home/ec2-user/.bashrc
# echo export DB_PASSWORD="${var.env-db-password}" >> /home/ec2-user/.bashrc


# source ~/.bashrc

# su - ec2-user -c "mkdir img"
# su - ec2-user -c "java -jar app.jar &"

# sudo /opt/aws/amazon-cloudwatch-agent/bin/amazon-cloudwatch-agent-ctl -a fetch-config -m ec2 -c file:/home/ec2-user/cloudwatch/config -s
# EOF

# }

resource "aws_kms_key" "ebs-key" {

  description = "default keypair for encrypted ebs"
  
}

resource "aws_kms_key_policy" "ebs-key-policy" {
  key_id = aws_kms_key.ebs-key.id
  policy = jsonencode({
    "Id": "key-consolepolicy-3",
    "Version": "2012-10-17",
    "Statement": [
        {
            "Sid": "Enable IAM User Permissions",
            "Effect": "Allow",
            "Principal": {
                "AWS": "arn:aws:iam::507902230172:root"
            },
            "Action": "kms:*",
            "Resource": "*"
        },
        {
            "Sid": "Allow access for Key Administrators",
            "Effect": "Allow",
            "Principal": {
                "AWS": [
                    "arn:aws:iam::507902230172:user/op-lrl",
                    "arn:aws:iam::507902230172:role/EC2-CSYE6225"
                ]
            },
            "Action": [
                "kms:Create*",
                "kms:Describe*",
                "kms:Enable*",
                "kms:List*",
                "kms:Put*",
                "kms:Update*",
                "kms:Revoke*",
                "kms:Disable*",
                "kms:Get*",
                "kms:Delete*",
                "kms:TagResource",
                "kms:UntagResource",
                "kms:ScheduleKeyDeletion",
                "kms:CancelKeyDeletion"
            ],
            "Resource": "*"
        },
        {
            "Sid": "Allow use of the key",
            "Effect": "Allow",
            "Principal": {
                "AWS": [
                    "arn:aws:iam::507902230172:user/op-lrl",
                    "arn:aws:iam::507902230172:role/EC2-CSYE6225"
                ]
            },
            "Action": [
                "kms:Encrypt",
                "kms:Decrypt",
                "kms:ReEncrypt*",
                "kms:DescribeKey",
                "kms:GetPublicKey"
            ],
            "Resource": "*"
        },
        {
            "Sid": "Allow attachment of persistent resources",
            "Effect": "Allow",
            "Principal": {
                "AWS": [
                    "arn:aws:iam::507902230172:user/op-lrl",
                    "arn:aws:iam::507902230172:role/EC2-CSYE6225"
                ]
            },
            "Action": [
                "kms:CreateGrant",
                "kms:ListGrants",
                "kms:RevokeGrant"
            ],
            "Resource": "*",
            "Condition": {
                "Bool": {
                    "kms:GrantIsForAWSResource": "true"
                }
            }
        }
    ]
})
}


resource "aws_kms_key" "rds-key" {

  description = "default keypair for encrypted ebs"
  
}

resource "aws_kms_key_policy" "rds-key-policy" {
  key_id = aws_kms_key.rds-key.id
  policy = jsonencode({
    "Id": "key-consolepolicy-3",
    "Version": "2012-10-17",
    "Statement": [
        {
            "Sid": "Enable IAM User Permissions",
            "Effect": "Allow",
            "Principal": {
                "AWS": "arn:aws:iam::507902230172:root"
            },
            "Action": "kms:*",
            "Resource": "*"
        },
        {
            "Sid": "Allow access for Key Administrators",
            "Effect": "Allow",
            "Principal": {
                "AWS": [
                    "arn:aws:iam::507902230172:user/op-lrl",
                    "arn:aws:iam::507902230172:role/EC2-CSYE6225"
                ]
            },
            "Action": [
                "kms:Create*",
                "kms:Describe*",
                "kms:Enable*",
                "kms:List*",
                "kms:Put*",
                "kms:Update*",
                "kms:Revoke*",
                "kms:Disable*",
                "kms:Get*",
                "kms:Delete*",
                "kms:TagResource",
                "kms:UntagResource",
                "kms:ScheduleKeyDeletion",
                "kms:CancelKeyDeletion"
            ],
            "Resource": "*"
        },
        {
            "Sid": "Allow use of the key",
            "Effect": "Allow",
            "Principal": {
                "AWS": [
                    "arn:aws:iam::507902230172:user/op-lrl",
                    "arn:aws:iam::507902230172:role/EC2-CSYE6225"
                ]
            },
            "Action": [
                "kms:Encrypt",
                "kms:Decrypt",
                "kms:ReEncrypt*",
                "kms:DescribeKey",
                "kms:GetPublicKey"
            ],
            "Resource": "*"
        },
        {
            "Sid": "Allow attachment of persistent resources",
            "Effect": "Allow",
            "Principal": {
                "AWS": [
                    "arn:aws:iam::507902230172:user/op-lrl",
                    "arn:aws:iam::507902230172:role/EC2-CSYE6225"
                ]
            },
            "Action": [
                "kms:CreateGrant",
                "kms:ListGrants",
                "kms:RevokeGrant"
            ],
            "Resource": "*",
            "Condition": {
                "Bool": {
                    "kms:GrantIsForAWSResource": "true"
                }
            }
        }
    ]
})
}


## The launch template must be created with a kms key pair - 09
resource "aws_launch_template" "lb-autoscaling-template" {
  name = "lb-autoscaling-template-01"

  block_device_mappings {
    device_name = "/dev/sdh"

    ebs {
      volume_size = 8
      volume_type = "gp2"

      encrypted = true
      kms_key_id = aws_kms_key.ebs-key.arn
    }
  }
  image_id      = var.default-ami
  instance_type = "t2.micro"
  key_name      = var.default-key-pair-name
  vpc_security_group_ids = [aws_security_group.aws_app_env.id
  , ]

  iam_instance_profile {
    name = "EC2-CSYE6225"
  }

  ##Attributes from other tutorial
  monitoring {
    enabled = true
  }

  user_data = base64encode(
    <<EOF
#!/bin/bash
mkdir ~/img4
sudo systemctl start mariadb
echo export AWS_DEFAULT_REGION="us-west-2" >> /home/ec2-user/.bash_profile
echo export S3_NAME=${aws_s3_bucket.s3-b-1.bucket} >> /home/ec2-user/.bash_profile

echo export DB_DRIVE="${var.env-db-drive}" >> /home/ec2-user/.bashrc
echo export DB_URL="jdbc:mysql://${aws_db_instance.default.address}/TestDev" >> /home/ec2-user/.bashrc
echo export DB_USERNAME="${var.env-db-username}" >> /home/ec2-user/.bashrc
echo export DB_PASSWORD="${var.env-db-password}" >> /home/ec2-user/.bashrc


source ~/.bashrc

su - ec2-user -c "mkdir img"
su - ec2-user -c "java -jar app.jar &"

sudo /opt/aws/amazon-cloudwatch-agent/bin/amazon-cloudwatch-agent-ctl -a fetch-config -m ec2 -c file:/home/ec2-user/cloudwatch/config -s
EOF
  )
}


resource "aws_autoscaling_group" "autoscaling-group" {
  name                      = "autoscaling-group-08"
  max_size                  = 3
  min_size                  = 1
  health_check_grace_period = 300
  health_check_type         = "ELB"
  desired_capacity          = 1
  force_delete              = true

  vpc_zone_identifier = [aws_subnet.assign-03-subnet-pub-1.id,
    aws_subnet.assign-03-subnet-pub-2.id
  , ]
  default_cooldown = 60

  launch_template {
    id = aws_launch_template.lb-autoscaling-template.id
  }

  ##Test tags form the example, ignore it, coz idk how to use.
  tag {
    key                 = "foo"
    value               = "bar"
    propagate_at_launch = true
  }

  timeouts {
    delete = "15m"
  }

  tag {
    key                 = "lorem"
    value               = "ipsum"
    propagate_at_launch = false
  }

  target_group_arns = [aws_lb_target_group.lb-target-group.arn
  , ]
}


##Scale up
resource "aws_autoscaling_policy" "autoscaling-scale-up" {
  autoscaling_group_name = aws_autoscaling_group.autoscaling-group.name
  name                   = "autoscaling-scale-up"
  scaling_adjustment     = 1
  adjustment_type        = "ChangeInCapacity"
  cooldown               = 300

}

resource "aws_cloudwatch_metric_alarm" "alarm-cpu-top-limit" {
  alarm_name          = "app_cpu_top_limit_alarm"
  comparison_operator = "GreaterThanOrEqualToThreshold"
  evaluation_periods  = 2
  metric_name         = "CPUUtilization"
  namespace           = "AWS/EC2"
  period              = 120
  statistic           = "Average"
  threshold           = 5

  dimensions = {
    AutoScalingGroupName = aws_autoscaling_group.autoscaling-group.name
  }

  alarm_description = "When cpu usage beyond 5%"
  alarm_actions = [aws_autoscaling_policy.autoscaling-scale-up.arn
  , ]
}

##Scale down
resource "aws_autoscaling_policy" "autoscaling-scale-down" {
  autoscaling_group_name = aws_autoscaling_group.autoscaling-group.name
  name                   = "autoscaling-scale-down"
  scaling_adjustment     = -1
  adjustment_type        = "ChangeInCapacity"
  cooldown               = 300

}

resource "aws_cloudwatch_metric_alarm" "alarm-cpu-bottom-limit" {
  alarm_name          = "app_cpu_bottom_limit_alarm"
  comparison_operator = "LessThanThreshold"
  evaluation_periods  = 2
  metric_name         = "CPUUtilization"
  namespace           = "AWS/EC2"
  period              = 120
  statistic           = "Average"
  threshold           = 3

  dimensions = {
    AutoScalingGroupName = aws_autoscaling_group.autoscaling-group.name
  }

  alarm_description = "When cpu usage beyond 5%"
  alarm_actions = [aws_autoscaling_policy.autoscaling-scale-down.arn
  , ]
}


##Load balancer
resource "aws_lb" "lb" {
  name               = "ins-lb-09"
  internal           = false
  load_balancer_type = "application"
  security_groups = [aws_security_group.aws-load-balancer-sg.id
  , ]
  subnets = [aws_subnet.assign-03-subnet-pub-1.id,
    aws_subnet.assign-03-subnet-pub-2.id
  , ]

  enable_deletion_protection = false

  # access_logs {
  #   bucket  = aws_s3_bucket.s3-b-1.id
  #   prefix  = "test-lb-log"
  #   enabled = true
  # }

  tags = {
    Environment = "production"
  }


}

resource "aws_lb_target_group" "lb-target-group" {
  name        = "lb-target-group"
  port        = 8080
  protocol    = "HTTP"
  vpc_id      = aws_vpc.main.id
  target_type = "instance"

  health_check {
    path = "/healthz"
  }
}


resource "aws_lb_listener" "lb-listener" {
  load_balancer_arn = aws_lb.lb.arn
  port              = "443"
  protocol          = "HTTPS"

  ssl_policy        = "ELBSecurityPolicy-TLS13-1-2-2021-06"
  certificate_arn   = var.ssl-arn

  default_action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.lb-target-group.arn
  }
}

variable "ssl-arn" {
  description = "SSL certificate for https request"
}

resource "aws_lb_listener_certificate" "example" {
  listener_arn    = aws_lb_listener.lb-listener.arn
  certificate_arn = var.ssl-arn
}


resource "aws_instance" "aws-ami-instance-lb-1" {
  ami           = var.default-ami
  instance_type = "t2.micro"

  disable_api_termination = false


  key_name             = "aws-dev-lrl-us-west-2"
  iam_instance_profile = aws_iam_instance_profile.role-instance-profile.name

  security_groups = [aws_security_group.aws_app_env.id,
  ]

  subnet_id = aws_subnet.assign-03-subnet-pub-2.id

  associate_public_ip_address = true

  ebs_block_device {
    device_name = "/dev/sdh"
    volume_size = 8
    volume_type = "gp2"
  }
  tags = {
    Name = "aws-ami-instance-ebs-test"
  }

  user_data = <<EOF
#!/bin/bash
mkdir ~/img3
sudo systemctl start mariadb
echo export AWS_DEFAULT_REGION="us-west-2" >> /home/ec2-user/.bash_profile
echo export S3_NAME=${aws_s3_bucket.s3-b-1.bucket} >> /home/ec2-user/.bash_profile

echo export DB_DRIVE="${var.env-db-drive}" >> /home/ec2-user/.bashrc
echo export DB_URL="jdbc:mysql://${aws_db_instance.default.address}/TestDev" >> /home/ec2-user/.bashrc
echo export DB_USERNAME="${var.env-db-username}" >> /home/ec2-user/.bashrc
echo export DB_PASSWORD="${var.env-db-password}" >> /home/ec2-user/.bashrc


source ~/.bashrc

su - ec2-user -c "mkdir img"
su - ec2-user -c "java -jar app.jar &"

sudo /opt/aws/amazon-cloudwatch-agent/bin/amazon-cloudwatch-agent-ctl -a fetch-config -m ec2 -c file:/home/ec2-user/cloudwatch/config -s
EOF
}
