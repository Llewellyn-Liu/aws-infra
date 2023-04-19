packer {
  required_plugins {
    amazon = {
      version = ">= 1.2.1"
      source  = "github.com/hashicorp/amazon"
    }
  }
}

variable "aws_region" {
  type    = string
  default = "us-west-2"
}

variable "source_ami" {
  type    = string
  default = "ami-0f1a5f5ada0e7da53" # t2.micro
}

variable "ssh_username" {
  type    = string
  default = "ec2-user"
}

variable "subnet_id" {
  type    = string
  default = "subnet-07a311e76ff508782"
}

variable "vpc_id" {
  type    = string
  default = "vpc-008555a438b4099b9"
}

variable "sg_id" {
  type    = string
  default = "sg-0ece263c251d30a1f"
}

# https://www.packer.io/plugins/builders/amazon/ebs
source "amazon-ebs" "aws-lrl-6225-t" {
  region          = "${var.aws_region}"
  ami_name        = "csye6225_${formatdate("YYYY_MM_DD_hh_mm_ss", timestamp())}"
  ami_description = "AMI for CSYE 6225"
  ami_regions = [
    "us-east-2",
  ]

  aws_polling {
    delay_seconds = 120
    max_attempts  = 50
  }


  access_key = ""
  secret_key = ""

  instance_type     = "t2.micro"
  source_ami        = "${var.source_ami}"
  ssh_username      = "${var.ssh_username}"
  vpc_id            = "${var.vpc_id}"
  security_group_id = "${var.sg_id}"
  subnet_id         = "${var.subnet_id}"

 ami_users = ["desktop-dev", 
 "lrl-dev", 
 "lrl-readOnly",
 ]

  launch_block_device_mappings {
    delete_on_termination = true
    device_name           = "/dev/sda1"
    volume_size           = 10
    volume_type           = "gp2"
  }
}

build {
  sources = ["source.amazon-ebs.aws-lrl-6225-t"]

    provisioner "file" {
  source = "liu-station-spring-0.0.1-SNAPSHOT.jar"
  destination = "~/app/app.jar"
}

  provisioner "shell" {
    environment_vars = [
      # "DEBIAN_FRONTEND=noninteractive",
      # "CHECKPOINT_DISABLE=1"
    ]
    inline = [
      # "sudo apt-get update",
      # "sudo apt-get upgrade -y",
      # "sudo apt-get install nginx -y",
      # "sudo apt-get clean",
      "sudo touch test-inline",
      "sudo yum install java-17-amazon-corretto-headless",
      "mkdir tomcat",
      "cd tomcat",
      "wget https://dlcdn.apache.org/tomcat/tomcat-9/v9.0.72/bin/apache-tomcat-9.0.72.tar.gz",
      "tar -xvzf apache-tomcat-9.0.72.tar.gz",
      "cd ~/tomcat/apache-tomcat-9.0.72/bin",
      "chmod +x startup.sh",
      "chmod +x shutdown.sh",
      "java -jar ~/app/app.jar",
    ]
  }


}