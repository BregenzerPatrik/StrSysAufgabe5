package org.example;

public class Main {
    public static void main(String[] args) {
        System.setProperty(org.slf4j.simple.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "off");
        org.slf4j.Logger log = LoggerFactory.getLogger(org.apache.kafka.clients.NetworkClient.class);
        ArrayList<String> topics = new ArrayList<>();
        topics.add("PathDataTopic");
        KafkaConsumer kafkaConsumer = new KafkaConsumer("consumer1", topics);
        Thread thread = new Thread(kafkaConsumer);
        thread.setDaemon(true);
        thread.start();

        FileReader fr = new FileReader();
        fr.readFile();
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}