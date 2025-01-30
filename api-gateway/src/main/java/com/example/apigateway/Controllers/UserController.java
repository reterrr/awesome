package com.example.apigateway.Controllers;

import com.example.apigateway.Clients.LocationClient;
import com.example.apigateway.Clients.UserClient;
import com.example.apigateway.Clients.UserLocationClient;
import com.example.apigateway.Helpers.SecurityUtil;
import com.example.apigateway.Responses.Response;
import com.example.apigateway.RestLocation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @DeleteMapping("/")
    public Response<Void> deleteUser() {
        var response = UserClient.delete(Integer.parseInt(SecurityUtil.getCurrentUserId()));

        var code = response.getCode();
        var message = response.getMessage();

        return new Response<>(
                code, message
        );
    }

    @GetMapping("locations")
    public List<Long> getUserLocations() {
        var ids = UserLocationClient
                .getRelatedLocations(
                        Integer
                                .parseInt(
                                        SecurityUtil
                                                .getCurrentUserId()
                                )
                )
                .getLocationsList();

        System.out.println(Integer
                .parseInt(
                        SecurityUtil
                                .getCurrentUserId()
                ));

        return ids;
    }
}
