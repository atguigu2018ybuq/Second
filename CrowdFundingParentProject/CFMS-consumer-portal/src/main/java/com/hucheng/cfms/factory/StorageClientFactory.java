package com.hucheng.cfms.factory;

import java.net.URL;

import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class StorageClientFactory implements FactoryBean<StorageClient> {

	// 使用@Value注解从application.yml中读取配置项的值（记得加上${}）
	@Value("${hucheng.crowdfunding.tracker.config.location}")
	private String configLocation;

	@Override
	public StorageClient getObject() throws Exception {
		// 1.将configLocation中的classpath:去掉
		configLocation = configLocation.replace("classpath:", "");

		// 2.获取tracker.conf的绝对物理路径
		URL url = StorageClientFactory.class.getResource(configLocation);
		String absolutePath = url.getPath();
		System.out.println("absolutePath=" + absolutePath);

		// 3.初始化操作
		ClientGlobal.init(absolutePath);

		// 4.创建对象
		TrackerClient trackerClient = new TrackerClient();
		TrackerServer trackerServer = trackerClient.getConnection();
		StorageServer storageServer = null;
		StorageClient storageClient = new StorageClient(trackerServer, storageServer);

		// 5.将创建好的对象返回
		return storageClient;
	}

	@Override
	public Class<?> getObjectType() {
		return StorageClient.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

}
