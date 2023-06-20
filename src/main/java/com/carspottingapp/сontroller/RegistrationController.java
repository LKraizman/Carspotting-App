package com.carspottingapp.сontroller;

import com.carspottingapp.event.RegistrationCompleteEvent;
import com.carspottingapp.model.CarSpotUser;
import com.carspottingapp.model.token.VerificationToken;
import com.carspottingapp.repository.VerificationTokenRepository;
import com.carspottingapp.service.CarSpotUserService;
import com.carspottingapp.service.request.RegistrationRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/register")
public class RegistrationController {
    private final CarSpotUserService carSpotUserService;
    private final ApplicationEventPublisher publisher;
    private final VerificationTokenRepository verificationTokenRepository;
    @PostMapping
    public String registerCarSpotUser(@RequestBody RegistrationRequest registrationRequest, final HttpServletRequest request){
        CarSpotUser carSpotUser = carSpotUserService.registerUser(registrationRequest);

        publisher.publishEvent(new RegistrationCompleteEvent(carSpotUser, applicationUrl(request)));

        return "Success! Please, check your email to confirm registration";
    }

    @GetMapping("/verifyEmail")
    public String verifyEmail(@RequestParam("token")String token){
        VerificationToken verifiedToken = verificationTokenRepository.findByToken(token);
        if(verifiedToken.getUser().getIsEnabled()){
            return "This account is already verified. Try login";
        }
        String verificationResult = carSpotUserService.validateToken(token);
        if(verificationResult.equalsIgnoreCase("valid")){
            return "You account successfully verified. Now you can login to your account.";
        }
        return "Invalid verification token";
    }

    public String applicationUrl(HttpServletRequest request) {
        return "http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
    }

}
