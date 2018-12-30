package sec.project.config;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import sec.project.domain.Account;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) {
        Connection connection = null;
        Account account = new Account();
        try {
            connection = DriverManager.getConnection("jdbc:h2:file:./database", "sa", "");
            //fixme A1:2017-Injection
            ResultSet resultSet = connection.createStatement().executeQuery("SELECT * FROM Account WHERE username = '" + username + "'");
            if (resultSet.next()) {
                account.setUsername(resultSet.getString("username"));
                account.setPassword(resultSet.getString("password"));
            } else {
                throw new UsernameNotFoundException("No such user: " + username);
            }
            resultSet.close();
            connection.close();
        } catch (SQLException ignored) {
            System.out.println(ignored);
        }


        return new org.springframework.security.core.userdetails.User(
                account.getUsername(),
                account.getPassword(),
                true,
                true,
                true,
                true,
                Arrays.asList(new SimpleGrantedAuthority("USER")));
    }
}
