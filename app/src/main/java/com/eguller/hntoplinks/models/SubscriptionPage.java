package com.eguller.hntoplinks.models;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Singular;
import lombok.experimental.SuperBuilder;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class SubscriptionPage extends Page {

  @Builder.Default
  private PageTab activeTab = PageTab.subscribe;

  private SubscriptionForm subscriptionForm;

  private       boolean      captchaEnabled;
  @Singular
  private final List<String> errors;

  @Singular
  private final List<String> messages;

  public boolean hasErrors() {
    return !CollectionUtils.isEmpty(errors);
  }

  public boolean hasMessages() {
    return !CollectionUtils.isEmpty(messages);
  }

  @Builder
  @Data
  public static class SubscriptionForm {
    private String subsUUID;

    @Builder.Default
    private String email = "";

    private boolean daily;

    @Builder.Default
    private boolean weekly = true;

    private boolean monthly;

    private boolean yearly;

    private String gRecaptchaResponse;

    @Builder.Default
    private String timeZone = "UTC";


    public boolean hasSubscription() {
      return (daily || weekly || monthly || yearly);
    }
  }
}
