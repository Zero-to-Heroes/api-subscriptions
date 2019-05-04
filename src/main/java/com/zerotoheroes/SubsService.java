package com.zerotoheroes;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SubsService {

    private SubscriptionRepository repository;

    @Inject
    public SubsService(SubscriptionRepository repository) {
        this.repository = repository;
    }

    public SubscriptionStatus getSubscriptionStatus(String userid, String username) {
        return repository.getSubscriptionStatus(userid, username);
    }
}
