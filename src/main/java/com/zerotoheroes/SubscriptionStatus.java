package com.zerotoheroes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Entity
@Table(name = "subscriptions", indexes = {
        @Index(columnList = "username", name = "ix_username"),
        @Index(columnList = "userId", name = "ix_userId"),
        @Index(columnList = "subscriptionStatus", name = "ix_subscriptionStatus"),
        @Index(columnList = "expirationDate", name = "ix_expirationDate")
})
@AllArgsConstructor()
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public final class SubscriptionStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;
    private String username;
    private String subscriptionStatus;

    @Temporal(TemporalType.TIMESTAMP)
    private Date expirationDate;

    private SubscriptionStatus() {
        // Needed by JPA
    }
}
