package com.eguller.hntoplinks.models;

import com.eguller.hntoplinks.services.EmailService;
import com.eguller.hntoplinks.services.StoryCacheService;
import com.eguller.hntoplinks.services.TemplateService;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
public abstract class TopLinksEmail {
  protected Subscription      subscription;
  protected StoryCacheService storyCacheService;
  protected TemplateService   templateService;

  public Email createEmail() {
    var subject = createSubject();
    var topStories = getTopStories();
    var content = templateService.generateTopEmail(subject, subscription, topStories);

    var email = Email.builder()
      .subject("[hntoplinks] - " + subject)
      .to(subscription.getEmail())
      .html(content).build();

    return email;
  }

  protected abstract String createSubject();

  protected abstract List<Story> getTopStories();
}