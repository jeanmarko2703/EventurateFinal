package pe.edu.upc.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.edu.upc.entity.Users;
import pe.edu.upc.serviceinterface.IUserService;


@Service
public class JpaUserDetailsService implements UserDetailsService {

	@Autowired
	private IUserService uS;

	@Transactional(readOnly = true)
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		Users user = uS.findByEmailUser(username);

		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

		authorities.add(new SimpleGrantedAuthority(user.getRole().getNameRole()));

		return new User(user.getEmailUser(), user.getPasswordUser(), true, true, true, true, authorities);

	}

}