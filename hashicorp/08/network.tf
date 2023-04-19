resource "aws_vpc" "main" {
  cidr_block       = "10.11.0.0/16"
  instance_tenancy = "default"

  tags = {
    Name = "assign_03_vpc"
  }
}

resource "aws_internet_gateway" "gw" {
  vpc_id = aws_vpc.main.id

  tags = {
    Name = "my_tf_gw_config"
  }
}

resource "aws_subnet" "assign-03-subnet-1" {
  vpc_id            = aws_vpc.main.id
  cidr_block        = "10.11.11.0/24"
  availability_zone = "us-west-2b"

  tags = {
    Name = "assign_03_subnet_1"
  }
}

resource "aws_subnet" "assign-03-subnet-2" {
  vpc_id            = aws_vpc.main.id
  cidr_block        = "10.11.12.0/24"
  availability_zone = "us-west-2a"

  tags = {
    Name = "assign_03_subnet_2"
  }
}

resource "aws_subnet" "assign-03-subnet-3" {
  vpc_id            = aws_vpc.main.id
  cidr_block        = "10.11.13.0/24"
  availability_zone = "us-west-2c"

  tags = {
    Name = "assign_03_subnet_3"
  }
}

resource "aws_subnet" "assign-03-subnet-pub-1" {
  vpc_id            = aws_vpc.main.id
  cidr_block        = "10.11.0.0/24"
  availability_zone = "us-west-2b"

  tags = {
    Name = "assign_03_subnet-pub_1"
  }
}

resource "aws_subnet" "assign-03-subnet-pub-2" {
  vpc_id            = aws_vpc.main.id
  cidr_block        = "10.11.1.0/24"
  availability_zone = "us-west-2a"

  tags = {
    Name = "assign_03_subnet_pub_2"
  }
}

resource "aws_subnet" "assign-03-subnet-pub-3" {
  vpc_id            = aws_vpc.main.id
  cidr_block        = "10.11.2.0/24"
  availability_zone = "us-west-2c"

  tags = {
    Name = "assign_03_subnet_pub_3"
  }
}

resource "aws_route_table" "public-access" {
  vpc_id = aws_vpc.main.id

  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.gw.id
  }

  tags = {
    Name = "public_access"
  }
}

resource "aws_route_table_association" "public-01" {
  subnet_id      = aws_subnet.assign-03-subnet-pub-1.id
  route_table_id = aws_route_table.public-access.id
}

resource "aws_route_table_association" "public-02" {
  subnet_id      = aws_subnet.assign-03-subnet-pub-2.id
  route_table_id = aws_route_table.public-access.id
}

resource "aws_route_table_association" "public-03" {
  subnet_id      = aws_subnet.assign-03-subnet-pub-3.id
  route_table_id = aws_route_table.public-access.id
}


##Create security group for the load balancer, which is needed when setting the app sg
##Thus the process is moved here 

resource "aws_security_group" "aws-load-balancer-sg" {
  name        = "aws_load_balancer_security_group"
  description = "SG fro load balancer"
  vpc_id      = aws_vpc.main.id

  ingress {
    description = "ports for http and https"
    from_port   = 80
    to_port     = 80
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    description = "ports for http and https"
    from_port   = 443
    to_port     = 443
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port        = 0
    to_port          = 0
    protocol         = "-1"
    cidr_blocks      = ["0.0.0.0/0"]
    ipv6_cidr_blocks = ["::/0"]
  }

  tags = {
    Name = "aws_load_balancer_security_group"
  }


}

##Creating app sg (comment: A8)

resource "aws_security_group" "aws_app_env" {
  name        = "aws_app_env_asmt_04"
  description = "SG for application environment"
  vpc_id      = aws_vpc.main.id

  ingress {
    description = "SSH Connection"
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = ["38.103.136.11/32"]
  }

  ingress {
    description = "TLS from VPC"
    from_port   = 443
    to_port     = 443
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    description = "Application Port 1"
    from_port   = 80
    to_port     = 80
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }
  ingress {
    description = "Application Port 2"
    from_port   = 8080
    to_port     = 8080
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }


  ingress {
    description = "ingress from load balancer"
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    security_groups = [
      aws_security_group.aws-load-balancer-sg.id,
    ]
  }

  ingress {
    description = "ingress from load balancer"
    from_port   = 8080
    to_port     = 8080
    protocol    = "tcp"
    security_groups = [
      aws_security_group.aws-load-balancer-sg.id,
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
    Name = "aws_app_env"
  }
}
