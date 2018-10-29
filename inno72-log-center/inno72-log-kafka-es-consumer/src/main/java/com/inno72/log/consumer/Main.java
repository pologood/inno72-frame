package com.inno72.log.consumer;


import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

import javax.annotation.Resource;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContextAware;

import com.inno72.log.repository.SysLogRepository;

/**
 * 为啥要做一个这玩意儿呢 ？？？
 * 事情是这样子的
 * 系统日志太TMD多了，然后spring-kafka的单线程表示压力很大。
 * 然后就改成了批处理。那么问题来了，批处理后提交的offset不翼而飞！！！而且不走zookeeper去记录offset。FUCK
 * 搞的作者很郁闷，源码一大堆。找着实在费劲
 * 运维盼盼哥哥就不乐意了。总报警消息堆积。。。 （本人：diss 。！！！其实没有堆积么！只不过spring自己给管了。）
 * 实测关闭zookeeper也没有影响，启动也不会去创建consumer。。。自然没有offset，不报堆积才有鬼。
 * 运维哥提供的查看堆积的一个地址，需要他打开一下后台的脚本 http://dev.72solo.com:28000/v3/kafka/local/consumer/myGroup
 * 测试环境的地址在这  ./zkCli -server 192.168.33.243:2181 本地可以去看一下 根目录下面的客户端。。。ls /consumer
 *
 * 用原生的可以解决上面小哥哥的烦恼。那就搞吧。。。
 * 好的吧，然后就有我这么一大堆的代码。坑不坑的基本上都有了详细的注释；
 * 需要说明的是 偏移量是同步提交的。重新分配是重新写的。
 * 其实还有一些提升空间的，但是现在处理能力秒级可以几千条。。。应该是满足了业务需求。
 * 本着本本分分的原则就不继续搞了。万一搞出别的坑就不好解释了！！！
 */
public class Main{
	private static Logger logger = LoggerFactory.getLogger(Main.class);
	/**
	 * 用于接收kafka 消息的线程池
	 * 看下面的kafkaConsumerExecutorNumber注释 
	 */
	private ExecutorService kafkaConsumerExecutorService;
	/**
	 * 创建的kefka的处理线程数
	 * 原则上对应 该topic的分区数   log-sys topic 含有4个分区。
	 *  ***********************  设置多了也没用 ***************************
	 *  设置少了一个线程可能会对应多个patition。出现再分配时候会很坑。
	 *
	 *  坑，分了分区后会产生提交失败的情况。
	 */
	private int kafkaConsumerExecutorNumber = 1;


	/**
	 * 具体处理kafka消息的线程池。配置好下面的workerExecutorNumber就没问题
	 * 配置不好就是个死
	 */
	private ExecutorService workerExecutorService;
	/**
	 * 每个kafkaConsumerExecutorNumber可以有workerExecutorNumber个线程去处理具体的消息
	 * 看着整吧！多了es会死。会死。会死。不过2个到6个可以接受，kafkaConsumerExecutorNumber * workerExecutorNumber的关系。
	 * 多了还占用系统内存。
	 * 
	 *------------------------------- 慎重 -------------------------------
	 */
	private int workerExecutorNumber = 5;

	/**
	 * 可以阻塞往线程池中提交新的任务，直到有可用的线程
	 */
	private Semaphore semaphore;

	private SysLogRepository sysLogRepository;

	private ConsumerInit consumerInit;

	Main(SysLogRepository sysLogRepository, ConsumerInit consumerInit){
		//看上面的注释
		kafkaConsumerExecutorService = Executors.newFixedThreadPool(kafkaConsumerExecutorNumber);
		workerExecutorService = Executors.newFixedThreadPool(workerExecutorNumber);
		semaphore = new Semaphore(workerExecutorNumber);
		this.sysLogRepository = sysLogRepository;
		this.consumerInit = consumerInit;
	}

	public void start(){

		logger.info("开始运行");

		List<KafkaConsumer<String,String>> consumers = consumerInit.getConsumerClient();

		if (consumers.size() == 0) {
			throw new RuntimeException("客户端配置错误!");
		}

		consumers.forEach((KafkaConsumer<String, String> consumer) -> {

			kafkaConsumerExecutorService.submit(new ConsumerThrad(consumer, workerExecutorService, semaphore, sysLogRepository));

		});

	}

}