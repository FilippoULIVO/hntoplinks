package com.eguller.hntoplinks.services;


import com.eguller.hntoplinks.Application;
import com.eguller.hntoplinks.controllers.ApplicationController;
import com.eguller.hntoplinks.models.Email;
import com.eguller.hntoplinks.models.SubscriptionPage;
import com.eguller.hntoplinks.repository.SubscriptionRepository;
import com.eguller.hntoplinks.util.SubscriptionUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.ui.ExtendedModelMap;

@SpringBootTest(classes = {Application.class})
@ActiveProfiles({"local"})
public class SubscriptionServiceTest {
  @Autowired
  private ApplicationController applicationController;

  @Autowired
  private SubscriptionRepository subscriptionRepository;

  @Autowired
  private MockEmailStore mockEmailStore;

  @Test
  public void test_subscribe() {
    var emailAddress = "test_subscribe1@hntoplinks.com";
    SubscriptionUtil.subscribeDailyNew(this.applicationController, emailAddress);

    var subscriptionEntity = subscriptionRepository.findByEmail(emailAddress);
    Assertions.assertEquals(emailAddress, subscriptionEntity.get().getEmail());
    Assertions.assertTrue(subscriptionEntity.get().isDaily());
    Assertions.assertFalse(subscriptionEntity.get().isWeekly());
    Assertions.assertFalse(subscriptionEntity.get().isMonthly());
    Assertions.assertFalse(subscriptionEntity.get().isAnnually());

    Email email = mockEmailStore.getLastMail(emailAddress).orElseThrow();
    Assertions.assertEquals(1, email.getTo().size());
    Assertions.assertTrue(email.getTo().contains(emailAddress));
    Assertions.assertEquals("[hntoplinks] - Welcome to hntoplinks.com", email.getSubject());
  }

  @Test
  public void test_unsubscribe() {
    var emailAddress = "test_unsubscribe1@hntoplinks.com";
    var model = SubscriptionUtil.subscribeDailyNew(this.applicationController, emailAddress);
    var subscriptionEntity = subscriptionRepository.findByEmail(emailAddress);
    Assertions.assertEquals(emailAddress, subscriptionEntity.get().getEmail());
    applicationController.unsubscribe_Get(new ExtendedModelMap(), subscriptionEntity.get().getSubsUUID());
    subscriptionEntity = subscriptionRepository.findByEmail(emailAddress);
    Assertions.assertFalse(subscriptionEntity.isPresent());
  }

  @Test
  public void test_updateSubscription() {
    var emailAddress = "test_updatesubscription1@hntoplinks.com";
    var model = SubscriptionUtil.subscribeDailyNew(this.applicationController, emailAddress);
    var subscriptionEntity = subscriptionRepository.findByEmail(emailAddress);
    Assertions.assertEquals(emailAddress, subscriptionEntity.get().getEmail());
    Assertions.assertTrue(subscriptionEntity.get().isDaily());
    Assertions.assertFalse(subscriptionEntity.get().isWeekly());
    SubscriptionUtil.subscribe(this.applicationController, emailAddress, ((SubscriptionPage) model.getAttribute("page")).getSubscription().getSubsUUID(), true, true, false, false);
    Assertions.assertTrue(subscriptionEntity.get().isDaily());
    Assertions.assertFalse(subscriptionEntity.get().isWeekly());
    subscriptionEntity = subscriptionRepository.findByEmail(emailAddress);
    Assertions.assertTrue(subscriptionEntity.get().isDaily());
    Assertions.assertTrue(subscriptionEntity.get().isWeekly());
  }



}