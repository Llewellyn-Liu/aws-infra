packer {
  required_plugins {
    amazon = {
      version = ">= 1.2.1"
      source  = "github.com/hashicorp/amazon"
    }
  }
}

variable "default-region" {
  type    = string
  description = "Default region"
}

variable "default-ami" {
  type    = string
  description = "Default AMI to generate instances"   # t2.micro
}

variable "ssh_username" {
  type    = string
  default = "ec2-user"
}

variable "vpc-id" {
  type        = string
  description = "Default entry for VPC id"
}

variable "subnet-id" {
  type    = string
  description = "Default subnet id"
}


variable "sg-id" {
  type        = string
  description = "Default Security Group"
}

variable "auth-key" {
  type        = string
  description = "Access key to connect aws service"
}

variable "auth-secret-key" {
  type        = string
  description = "Secret key to connect aws service"
}

variable "ami-user-default" {
  type        = string
  description = "My Root user id"
}
# https://www.packer.io/plugins/builders/amazon/ebs
source "amazon-ebs" "aws-lrl-6225-t" {
  region          = "${var.default-region}"
  ami_name        = "csye6225_${formatdate("YYYY_MM_DD_hh_mm_ss", timestamp())}"
  ami_description = "AMI for CSYE 6225"
  ami_regions = [
    var.default-region,
  ]

  aws_polling {
    delay_seconds = 120
    max_attempts  = 50
  }

  access_key = var.auth-key
  secret_key = var.auth-secret-key

  # ssh_keypair_name = "aws-ami"
  # ssh_private_key_file = "aws-ami-pri-con.pem"

  instance_type = "t2.micro"
  source_ami    = "${var.default-ami}"
  ssh_username  = "${var.ssh_username}"

  vpc_id            = var.vpc-id

  ami_users = [var.ami-user-default]

  launch_block_device_mappings {
    delete_on_termination = true
    device_name           = "/dev/sdh"
    volume_size           = 8
    volume_type           = "gp2"
  }
}

build {
  sources = ["source.amazon-ebs.aws-lrl-6225-t"]

  provisioner "shell" {
    script = "runtime-env-install.sh"
  }

  provisioner "file" {
    source      = "liu-station-spring-0.0.7-SNAPSHOT.jar"
    destination = "~/app.jar"
  }

  provisioner "file" {
    source      = "cloudwatchconfig.json"
    destination = "~/cloudwatch/config"
  }




}