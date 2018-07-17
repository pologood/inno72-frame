package com.inno72.log.util;

import com.inno72.log.vo.SysLog;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface SysLogRepository extends ElasticsearchRepository<SysLog, String> {
}
