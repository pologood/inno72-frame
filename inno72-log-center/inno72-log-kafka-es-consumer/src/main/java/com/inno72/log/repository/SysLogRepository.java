package com.inno72.log.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface SysLogRepository extends ElasticsearchRepository<SysLog, String> {
}
