package com.eguller.hntoplinks.services;


import com.eguller.hntoplinks.Application;
import com.eguller.hntoplinks.models.HnStory;
import com.eguller.hntoplinks.models.Story;
import com.eguller.hntoplinks.repository.StoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest(classes = {Application.class})
@ActiveProfiles({"local"})
public class StoryServiceTest {
    @Autowired
    private StoryService storyService;

    @Autowired
    StoryRepository storyRepository;

    @Test
    public void test_saveStory(){
        var hnStory = new Story(null, 1L, 59, "Test", "https://www.hntoplinks.com", "hntoplinks.com", "eguller", 99, LocalDateTime.now());
        storyService.saveStories(List.of(hnStory));

        var savedEntity = storyRepository.findByHnid(1L);
        Assertions.assertNotNull(savedEntity);

        var hnStoryUpdated = new Story(null, 1L, 59, "Test", "https://www.hntoplinks.com", "hntoplinks.com", "eguller", 100, LocalDateTime.now());
        storyService.saveStories(List.of(hnStoryUpdated));
        var updatedEntity = storyRepository.findByHnid(1L);

        Assertions.assertEquals(savedEntity.get().getId(), updatedEntity.get().getId());
    }
}