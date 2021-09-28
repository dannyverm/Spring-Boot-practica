package com.jpa.app.database.models.services;

import java.util.ArrayList;
import java.util.List;

import com.jpa.app.database.models.dao.IUsuarioDao;
import com.jpa.app.database.models.entity.Role;
import com.jpa.app.database.models.entity.Usuario;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("jpaUserDetailsServise")
public class JpaUserDetailsService implements UserDetailsService {

    private Logger logger= LoggerFactory.getLogger(JpaUserDetailsService.class);

    @Autowired
    private IUsuarioDao usuarioDao;
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario= usuarioDao.findByUsername(username);
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

        if(usuario==null){
            logger.error("Error Login: no existe el usuario '"+username+"'");
            throw new UsernameNotFoundException("Username "+username+" no existe en el sistema!");
        }
        
        for(Role role: usuario.getRole()){
            authorities.add(new SimpleGrantedAuthority(role.getAuthority()));
        }
        return new User(usuario.getUsername(), usuario.getPassword(), usuario.getEnabled(), true, true, true, authorities);
    }
    
}
