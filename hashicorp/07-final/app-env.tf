variable "default-ami" {
  type        = string
  description = "default ami from Env-Var"
}

variable "default-key-pair-name" {
  type        = string
  description = "default key from Env-Var"
}

variable "instance-sg" {
  type        = string
  description = "The Security Group for the instance to be related"
}

variable "vpc-id" {
  type        = string
  description = "Default vpc"
}



resource "random_id" "s3-id-gen" {
  byte_length = 8
}

# resource "aws_vpc" "storage-main" {
#   cidr_block       = "10.12.0.0/16"
#   instance_tenancy = "default"

#   tags = {
#     Name = "storage-vpc-assign-05"
#   }
# }

resource "aws_security_group" "aws_rds_env" {
  name        = "aws_rds_env_asmt_05"
  description = "SG for RDS environment"
  vpc_id      = var.vpc-id
  ingress {
    description = "DB Connection"
    from_port   = 3306
    to_port     = 3306
    protocol    = "tcp"
    security_groups = [var.instance-sg,
    ]
  }

  egress {
    from_port        = 0
    to_port          = 0
    protocol         = "-1"
    cidr_blocks      = ["0.0.0.0/0"]
    ipv6_cidr_blocks = ["::/0"]
  }

  tags = {
    Name = "aws_rds_env_name"
  }
}

variable "s3b1-env" {
  type        = string
  description = "environment val used in aws_s3_bucket.s3-b-1"
  default     = "Dev"
}


resource "aws_s3_bucket" "s3-b-1" {
  bucket = "my-webapp-s3-lrl-01"

  force_destroy = true

  tags = {
    Environment = var.s3b1-env
    Name        = "s3bucket-${random_id.s3-id-gen.hex}-${var.s3b1-env}"
  }
}

resource "aws_s3_bucket_lifecycle_configuration" "bucket-s3b1-config" {

  bucket = aws_s3_bucket.s3-b-1.id

  rule {
    id = "archival"

    expiration {
      days = 60
    }
    filter {
      and {
        prefix = "/"

        tags = {
          rule      = "archival"
          autoclean = "true"
        }
      }
    }

    status = "Enabled"

    transition {
      days          = 30
      storage_class = "STANDARD_IA"
    }
  }
}

resource "aws_s3_bucket_server_side_encryption_configuration" "s3b1-encrypt" {
  bucket = aws_s3_bucket.s3-b-1.id
  rule {
    apply_server_side_encryption_by_default {
      sse_algorithm = "AES256"
    }
  }
}

##DB Section
##The aws_db_instance will be built in the default VPC if not specified by db_subnet_group_name

variable "rds-subnet-id-1" {
  type = string
  description = "Default subnet id for rds"
}

variable "rds-subnet-id-2" {
  type = string
  description = "Default subnet id for rds"
}

resource "aws_db_parameter_group" "default" {
  name   = "rds-pg"
  family = "mysql5.7"

  parameter {
    name  = "character_set_server"
    value = "utf8"
  }

  parameter {
    name  = "character_set_client"
    value = "utf8"
  }
}

resource "aws_db_subnet_group" "default" {
  name       = "res-subnet-group"
  subnet_ids = [var.rds-subnet-id-1,
  var.rds-subnet-id-2
  ,]

  tags = {
    Name = "My DB subnet group"
  }
}

resource "aws_db_instance" "default" {
  allocated_storage    = 10
  db_name              = "csye6225"
  engine               = "mysql"
  engine_version       = "5.7"
  instance_class       = "db.t3.micro"
  username             = "csye6225"
  password             = "liurunlin"
  parameter_group_name = aws_db_parameter_group.default.id
  skip_final_snapshot  = true
  db_subnet_group_name = aws_db_subnet_group.default.name

  vpc_security_group_ids = [aws_security_group.aws_rds_env.id
  ,]
}

##INSTANCE SECTION

##IAM-ROLES AND POLICIES
resource "aws_iam_policy" "policy" {
  name        = "s3-policy-test"
  description = "My test policy"

  policy = <<EOT
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Action": [
                "s3:*"
            ],
            "Effect": "Allow",
            "Resource": [
                "arn:aws:s3:::${aws_s3_bucket.s3-b-1.bucket}",
                "arn:aws:s3:::${aws_s3_bucket.s3-b-1.bucket}/*"
            ]
        }
    ]
}
EOT
}

resource "aws_iam_role" "instance" {
  name               = "EC2-CSYE6225"

  assume_role_policy = jsonencode({
    "Version": "2012-10-17",
    "Statement": [
        {
            "Effect": "Allow",
            "Action": [
                "sts:AssumeRole"
            ],
            "Principal": {
                "Service": [
                    "ec2.amazonaws.com"
                ]
            }
        }
    ]
})

 managed_policy_arns = [aws_iam_policy.policy.arn,"arn:aws:iam::aws:policy/AmazonS3FullAccess"
 ,"arn:aws:iam::aws:policy/CloudWatchAgentAdminPolicy"
 ,]
}

resource "aws_iam_instance_profile" "role-instance-profile" {
  name = "EC2-CSYE6225"
  role = aws_iam_role.instance.name
}


variable "subnet-id" {
  type        = string
  description = "default subnet"
}

resource "aws_network_interface" "instance-ani" {
  subnet_id = var.subnet-id
  security_groups = [var.instance-sg
  , ]
  tags = {
    "Name" = "ins-ani-a04"
  }
}

resource "aws_eip" "my-eip" {
  vpc               = true
  network_interface = aws_network_interface.instance-ani.id
  tags = {
    "Name" = "ami-ins-eip"
  }
}

##LOAD THE ENV VAR MANUALLY
variable "env-db-drive" {
  type = string
  description = "DB_DRIVE to be uploaded. 1/4"
}

variable "env-db-username" {
  type = string
  description = "DB_USERNAME to be uploaded. 3/4"
}

variable "env-db-password" {
  type = string
  description = "DB_USERNAME to be uploaded"
}
resource "aws_instance" "aws-ami-instance-0" {
  ami           = var.default-ami
  instance_type = "t2.micro"

  disable_api_termination = false


  key_name = "aws-dev-lrl-us-west-2"
  iam_instance_profile = aws_iam_instance_profile.role-instance-profile.name

  security_groups = [var.instance-sg,
  ]

  subnet_id = var.subnet-id

  associate_public_ip_address = true

  ebs_block_device {
    device_name = "/dev/sdh"
    volume_size = 8
    volume_type = "gp2"
  }
  tags = {
    Name = "aws-ami-instance-ebs"
  }

  user_data = <<EOF
#!/bin/bash
mkdir ~/img1
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
