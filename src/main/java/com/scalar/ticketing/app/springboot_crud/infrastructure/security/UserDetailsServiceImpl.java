package com.scalar.ticketing.app.springboot_crud.infrastructure.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.scalar.ticketing.app.springboot_crud.infrastructure.entity.UserEntity;
import com.scalar.ticketing.app.springboot_crud.infrastructure.repository.SpringDataUserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final SpringDataUserRepository userRepo;

    public UserDetailsServiceImpl(SpringDataUserRepository userRepo) {
        this.userRepo = userRepo;
    }

    // @Override
    // public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    //     UserEntity user = userRepo.findByEmail(email)
    //             .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

    //     return org.springframework.security.core.userdetails.User.builder()
    //             .username(user.getEmail())
    //             .password(user.getPassword()) // ya debe estar bcrypt
    //             .roles(user.getRole().name())
    //             .build();
    // }
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity user = userRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        System.out.println("Authorities asignadas => " + user.getRole().getAuthorities()); // <-- debe mostrar authorities

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .authorities(user.getRole().getAuthorities())  // ✅ CLAVE
                .build();
    }
}
