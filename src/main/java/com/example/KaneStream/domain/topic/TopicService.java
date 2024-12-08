package com.example.KaneStream.domain.topic;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TopicService {
    private final TopicRepository topicRepository;

    public List<Topic> getAllTopics() {
        return topicRepository.findAll();
    }

    public Topic getTopicById(UUID id) {
        return topicRepository.getReferenceById(id);
    }

    public void updateTopic(Topic topic) {
        topicRepository.save(topic);
    }
}
