package ru.melodrom.amor.security

import grails.compiler.GrailsCompileStatic
import grails.plugin.springsecurity.userdetails.GormUserDetailsService
import grails.plugin.springsecurity.userdetails.NoStackUsernameNotFoundException
import grails.transaction.Transactional
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException

import static ru.melodrom.amor.utils.LogbackArguments.email

@GrailsCompileStatic
class UserDetailsService extends GormUserDetailsService {

    UserService userService

    @Transactional(readOnly = true, noRollbackFor = [IllegalArgumentException, UsernameNotFoundException])
    UserDetails loadUserByUsername(String username, boolean loadRoles) throws UsernameNotFoundException {
        AppUser user = userService.findByEmail(username)
        if (!user) {
            log.warn("User not found: {}", email(username))
            throw new NoStackUsernameNotFoundException()
        }

        // just for paranoic like me
        user = user.refresh()

        Collection<GrantedAuthority> authorities = loadAuthorities(user, username, loadRoles)
        UserDetails userDetails = createUserDetails(user, authorities)

        // TODO REMOVE
        log.debug("UserDetails: ${userDetails}, username: ${userDetails.username}, password: ${userDetails.password}")

        return userDetails
    }

}
