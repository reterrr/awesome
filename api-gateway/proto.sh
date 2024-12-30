#!/bin/bash
SRC_DIR=.

out=$(find . -name Protos 2>/dev/null)

protoc -I $SRC_DIR --java_out=$out/.. $out/api_user_service.proto