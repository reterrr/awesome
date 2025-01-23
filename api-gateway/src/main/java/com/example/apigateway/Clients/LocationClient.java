package com.example.apigateway.Clients;

import com.example.apigateway.Client;

@Client(host = "${location.server.host}",
        port = "${location.server.port}")
public class LocationClient {
}
