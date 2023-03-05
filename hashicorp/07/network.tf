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
  vpc_id      = "vpc-008555a438b4099b9"

  ingress {
    description = "DB Connection"
    from_port   = 3306
    to_port     = 3306
    protocol    = "tcp"
    security_groups = ["sg-09ed4c74c2a3d2c0d",
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
  bucket = "this-is-lrl-bucket-01"

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
}


resource "aws_instance" "ebs-ec2-instance-runnable" {
  ami                  = "ami-0eec5f390baa86868"
  instance_type        = "t2.micro"
  iam_instance_profile = "EC2-CSYE6225"
  key_name = "key-037e2befe87e5e099"

  tags = {
    Name = "ebs-ec2-ins-runnable"
  }
  user_data = <<EOF
#!/bin/bash
mkdir img
sudo systemctl start mariadb
export AWS_ACCESS_KEY_ID=AKIAIOSFODNN7EXAMPLE
export AWS_SECRET_ACCESS_KEY=wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY
export AWS_DEFAULT_REGION=us-west-2
export S3_NAME=${aws_s3_bucket.s3-b-1.bucket}
EOF
}
