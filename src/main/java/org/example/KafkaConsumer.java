package org.example;

import com.google.gson.Gson;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

public class KafkaConsumer implements Runnable {
    private final org.apache.kafka.clients.consumer.KafkaConsumer<String, String> consumer;
    private final List<String> topics;
    private final AtomicBoolean shutdown;
    private final CountDownLatch shutdownLatch;
    private final String ConsumerGroupId = "singleGroup";

    private final static String BOOTSTRAP_SERVERS = "localhost:9092";

    public KafkaConsumer(String id, List<String> topics) {
        System.out.println("----Erzeuge Consumer----");

        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        props.put(ConsumerConfig.CLIENT_ID_CONFIG, id);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, ConsumerGroupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        this.consumer = new org.apache.kafka.clients.consumer.KafkaConsumer<>(props);
        this.topics = topics;
        this.shutdown = new AtomicBoolean(false);
        this.shutdownLatch = new CountDownLatch(1);
    }


    public void run() {
        try {
            System.out.println("---Starte Consumer---");
            consumer.subscribe(topics);
            while (!shutdown.get()) {
                System.out.println("checking for message");
                Duration duration = Duration.ofMillis(500);
                ConsumerRecords<String, String> records = consumer.poll(duration);

                for (ConsumerRecord<String, String> record : records) {
                    System.out.println(record.value());
                    Gson gson = new Gson();
                    PathDataStore.add(gson.fromJson(record.value(), PathData.class));
                }

            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            System.out.println("---Beende Consumer---");
            consumer.close();
            shutdownLatch.countDown();
        }
    }

    public void shutdown() throws InterruptedException {
        shutdown.set(true);
        shutdownLatch.await();
    }
}
