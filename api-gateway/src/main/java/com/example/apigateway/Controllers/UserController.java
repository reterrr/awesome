package com.example.apigateway.Controllers;

import com.example.apigateway.Clients.UserClient;
import com.example.apigateway.Helpers.SecurityUtil;
import com.example.apigateway.Responses.Response;
import org.springframework.web.bind.annotation.*;

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
}
