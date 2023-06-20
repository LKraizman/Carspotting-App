package com.carspottingapp.service;

import com.carspottingapp.repository.CarSpotUserRepository;
import com.carspottingapp.security.CarSpotUserRegistrationDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CarSpotUserRegistrationDetailsService implements UserDetailsService {

    private final CarSpotUserRepository carSpotUserRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return carSpotUserRepository.findByEmail(email)
                .map(CarSpotUserRegistrationDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("User with this email not found"));
    }
}
