package com.carspottingapp.security;

import com.carspottingapp.model.CarSpotUser;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class CarSpotUserRegistrationDetails implements UserDetails {

    private String userName;
    private String userPassword;
    private Boolean isEnabled;
    private List<GrantedAuthority> authorityList;

    public CarSpotUserRegistrationDetails(CarSpotUser carSpotUser) {
        this.userName = carSpotUser.getEmail();
        this.userPassword = carSpotUser.getPassword();
        this.isEnabled = carSpotUser.getIsEnabled();
        this.authorityList = Arrays.stream(carSpotUser.getCarSpotUserRole().toString()
                .split(",")).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorityList;
    }

    @Override
    public String getPassword() {
        return userPassword;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }
}
