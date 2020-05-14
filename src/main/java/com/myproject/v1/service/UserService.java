package com.myproject.v1.service;


import com.myproject.v1.dao.UserRepository;
import com.myproject.v1.model.User;
import com.myproject.v1.viewmodel.AuthUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findFirstByEmail(email);
        if (!user.isPresent()) throw new UsernameNotFoundException(email);
        User entityUser = user.get();
        int clientId = 0;
        String clientUUId = null;

        if(entityUser.getClient() != null)
        {
            clientId = entityUser.getClient().getId();
            clientUUId = entityUser.getClient().getClientId().toString();
        }

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();

        AuthUser authUser = new AuthUser(entityUser.getEmail(), entityUser.getPassword(), true,
                true,true,true,grantedAuthorities,
                clientId,clientUUId,entityUser.getId() );
        return authUser;
    }
}
