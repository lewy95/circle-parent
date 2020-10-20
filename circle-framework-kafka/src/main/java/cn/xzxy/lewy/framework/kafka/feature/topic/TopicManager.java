package cn.xzxy.lewy.framework.kafka.feature.topic;

import cn.xzxy.lewy.framework.kafka.feature.common.CommonException;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author lewy95
 */
@Component
@Slf4j
public class TopicManager {

    static final CommonException EXP_TOPIC_NULL = new CommonException(500, "主题不能为空");

    @Autowired
    AdminClient adminClient;

    private ReentrantReadWriteLock reentrantReadWriteLock;
    private volatile List<String> existTopic;

    @PostConstruct
    public void init() {
        this.reentrantReadWriteLock = new ReentrantReadWriteLock();
        this.setExistTopic();
        if (CollectionUtils.isEmpty(this.existTopic)) {
            this.existTopic = Collections.emptyList();
        }

        log.info("获取所有已存在的主题:{}", this.existTopic);
    }

    public void createTopic(String name) {
        try {
            NewTopic build = TopicBuilder.name(name).partitions(2).replicas(2).compact().build();
            this.adminClient.createTopics(Arrays.asList(build));
            log.debug("创建主题{}", build);
        } catch (Exception var3) {
            log.error("创建主题失败，将使用kafkatemplate自动创建主题！！！", var3);
        }

    }

    public boolean isNewTopic(String name) {
        if (Collections.emptyList().equals(this.existTopic)) {
            return true;
        } else {
            boolean isNew = !this.isExistTopic(name);
            log.debug("线程{}发送主题{}，是否新主题{}", Thread.currentThread().getName(), name, isNew);
            if (isNew) {
                try {
                    if (this.reentrantReadWriteLock.writeLock().tryLock()) {
                        log.debug("线程{}获取写锁", Thread.currentThread().getName());
                        this.setExistTopic();
                    }
                } finally {
                    if (this.reentrantReadWriteLock.writeLock().isHeldByCurrentThread()) {
                        this.reentrantReadWriteLock.writeLock().unlock();
                        log.debug("线程{}释放写锁", Thread.currentThread().getName());
                    }

                }

                isNew = !this.isExistTopic(name);
                log.debug("线程{}发送主题{}，是否确认新主题{}", Thread.currentThread().getName(), name, isNew);
            }

            return isNew;
        }
    }

    public boolean isNewTopic(List<String> topicList) {
        if (Collections.emptyList().equals(this.existTopic)) {
            return true;
        } else {
            boolean isNew = !this.isExistTopic(topicList);
            log.debug("线程{}发送主题{}，是否新主题{}", Thread.currentThread().getName(), topicList, isNew);
            if (isNew) {
                try {
                    if (this.reentrantReadWriteLock.writeLock().tryLock()) {
                        log.debug("线程{}获取写锁", Thread.currentThread().getName());
                        this.setExistTopic();
                    }
                } finally {
                    if (this.reentrantReadWriteLock.writeLock().isHeldByCurrentThread()) {
                        this.reentrantReadWriteLock.writeLock().unlock();
                        log.debug("线程{}释放写锁", Thread.currentThread().getName());
                    }

                }

                isNew = !this.isExistTopic(topicList);
                log.debug("线程{}发送主题{}，是否确认新主题{}", Thread.currentThread().getName(), topicList, isNew);
            }

            return isNew;
        }
    }

    boolean isExistTopic(String topic) {
        return this.existTopic.contains(topic);
    }

    boolean isExistTopic(List<String> topicList) {
        return this.existTopic.containsAll(topicList);
    }

    private void setExistTopic() {
        try {
            ListTopicsResult listTopicsResult = this.adminClient.listTopics();
            Set<String> strings = listTopicsResult.names().get();
            this.existTopic = new ArrayList<>(strings);
        } catch (Exception var3) {
            log.error("获取kafka主题列表失败", var3);
        }

    }
}
