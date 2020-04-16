package com.jn.audit.mq;

import com.jn.audit.mq.event.TopicEvent;
import com.jn.langx.annotation.Singleton;
import com.jn.langx.event.EventPublisher;
import com.jn.langx.event.local.SimpleEventPublisher;
import com.jn.langx.util.Objects;
import com.jn.langx.util.collection.Collects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class MessageTopicDispatcher {
    private static final Logger logger = LoggerFactory.getLogger(MessageTopicDispatcher.class);
    private final Map<String, MessageTopic> topicMap = Collects.emptyHashMap();
    private EventPublisher<TopicEvent> topicEventPublisher;

    private MessageTopicDispatcher() {
    }

    public EventPublisher<TopicEvent> getTopicEventPublisher() {
        return topicEventPublisher;
    }

    public void setTopicEventPublisher(EventPublisher<TopicEvent> topicEventPublisher) {
        this.topicEventPublisher = topicEventPublisher;
    }

    public List<String> getTopicNames() {
        return Collects.newArrayList(topicMap.keySet());
    }

    public void registerTopic(MessageTopic messageTopic) {
        topicMap.put(messageTopic.getName(), messageTopic);
    }

    public void unregisterTopic(String name) {
        topicMap.remove(name);
    }

    public void publish(String topicName, Object message) {
        MessageTopic topic = topicMap.get(topicName);
        if (Objects.isNull(topic)) {
            logger.warn("Can't find the specified topic : {}", topicName);
        } else {
            topic.publish(message);
        }
    }

    public <M> void subscribe(String topicName, Consumer<M> consumer, String... dependencies) {
        MessageTopic topic = topicMap.get(topicName);
        if (Objects.isNull(topic)) {
            logger.warn("Can't find a topic : {}", topicName);
        } else {
            topic.subscribe(consumer, dependencies);
        }
    }

}
