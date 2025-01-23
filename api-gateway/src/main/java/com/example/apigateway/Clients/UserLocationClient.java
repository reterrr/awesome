package com.example.apigateway.Clients;

import com.example.apigateway.Client;

@Client(host = "${user-location.server.host}",
        port = "${user-location.server.port}")
public class UserLocationClient {

}
