package com.zerotoheroes;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import org.json.JSONObject;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SubscriptionRepository {

    private SessionFactory sessionFactory;

    @Inject
    public SubscriptionRepository(SecretManager secretManager) {
        JSONObject rds = secretManager.getRdsConnectionInfo();
        Configuration configuration = new Configuration();

        configuration.addAnnotatedClass(SubscriptionStatus.class);
        configuration.setProperty("connection.driver_class", "com.mysql.cj.jdbc.Driver");
        configuration.setProperty("hibernate.connection.url", "jdbc:mysql://" + rds.getString("host") + ":" + rds.getInt("port") + "/replay_summary");
        configuration.setProperty("hibernate.connection.username", rds.getString("username"));
        configuration.setProperty("hibernate.connection.password", rds.getString("password"));

        configuration.setProperty("show_sql", "true");
        configuration.setProperty("hibernate.hbm2ddl.auto", "update");
        configuration.setProperty("hibernate.connection.CharSet", "utf8");
        configuration.setProperty("hibernate.connection.characterEncoding", "utf8");
        configuration.setProperty("hibernate.connection.useUnicode", "true");
        configuration.setProperty("connection.pool_size", "5");
        configuration.setProperty("hibernate.connection.pool_size", "5");

        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties());
        sessionFactory = configuration.buildSessionFactory(builder.build());
    }

    public SubscriptionStatus getSubscriptionStatus(String userid, String username) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Query q = session.createQuery("FROM SubscriptionStatus WHERE userid = :userid AND username = :username")
                .setParameter("userid", userid)
                .setParameter("username", username);
        SubscriptionStatus result = (SubscriptionStatus) q.uniqueResult();
        session.getTransaction().commit();
        session.close();
        return result;
    }
}
