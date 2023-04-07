terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 4.16"
    }
    random = {
      source  = "hashicorp/random"
      version = "3.4.3"
    }
  }

  required_version = ">= 1.2.0"
}

variable "default-region" {
  type        = string
  description = "default region from Env-Var"
}

variable "auth-key" {
  type        = string
  description = "auth key from Env-Var"
}

variable "auth-secret-key" {
  type        = string
  description = "auth secret key from Env-Var"
}

provider "aws" {
  region     = var.default-region
  access_key = var.auth-key
  secret_key = var.auth-secret-key
}