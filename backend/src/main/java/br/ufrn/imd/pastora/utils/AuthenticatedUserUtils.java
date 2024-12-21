package br.ufrn.imd.pastora.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import br.ufrn.imd.pastora.config.security.UserPrincipal;
import br.ufrn.imd.pastora.exceptions.UserNotAuthenticatedException;

@Component
public class AuthenticatedUserUtils {
  public Object getAuthenticatedUser() throws UserNotAuthenticatedException {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      if (authentication != null && authentication.isAuthenticated()) {
          return authentication.getPrincipal();
      }
      throw new UserNotAuthenticatedException("User not authenticated!");
  }

  public String getAuthenticatedUserId() throws UserNotAuthenticatedException{
      Object user = getAuthenticatedUser();

      if(user instanceof UserPrincipal){
          return ((UserPrincipal) user).getId();
      }
      
      return null;
  }

  public String getAuthenticatedUserEmail() throws UserNotAuthenticatedException {
      Object user = getAuthenticatedUser();

      if (user instanceof UserPrincipal) { // Substitua por sua implementação
        return ((UserPrincipal) user).getEmail();
      }
      if (user instanceof UserDetails) {
          return ((UserDetails) user).getUsername(); // Geralmente o username é o email
      }

      return null;
  }  
}
