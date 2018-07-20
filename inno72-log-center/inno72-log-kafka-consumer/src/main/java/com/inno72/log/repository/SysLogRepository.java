package com.inno72.log.repository;

import com.inno72.log.repository.SysLog;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface SysLogRepository extends ElasticsearchRepository<SysLog, String> {
}
