package ru.melodrom.amor

import grails.compiler.GrailsCompileStatic
import grails.transaction.Transactional
import groovy.util.logging.Slf4j
import ru.melodrom.amor.security.AppUser

@GrailsCompileStatic
@Slf4j
class WalletService {

    @Transactional
    Wallet create(String address, Boolean test = false) {
        assert address?.trim()

        Wallet wallet = new Wallet(
                address: address,
                test: test
        ).save(flush: true, failOnError: true)

        log.info("Created ${wallet}")

        return wallet
    }

    @Transactional
    Wallet getByUser(AppUser user) {
        assert user

        return Wallet.findByUser(user)
    }

    @Transactional
    Wallet getByAddress(String address) {
        assert address

        return Wallet.findByAddress(address)
    }

    @Transactional
    Wallet addWalletToUser(AppUser user, Wallet wallet = null) {
        assert user

        if (!wallet) {
            wallet = Wallet.findByUser(null)
            if (!wallet) {
                log.error("No wallet for user: ${user.email}")
                return null
            }
        }
        if (wallet) {
            wallet.user = user
            log.info("Add wallet: ${wallet.address} to user: ${user.email}")
        }

        return wallet
    }

}
