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

provider "aws" {
  region = "us-west-2"
  access_key = "AKIATKDBMDVFOLYDIBXT"
  secret_key = "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"
}

