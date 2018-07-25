package com.inno72.mapper;

import com.inno72.common.Mapper;
import com.inno72.model.Inno72AppLog;

import java.util.List;

public interface Inno72AppLogMapper extends Mapper<Inno72AppLog> {
	List<Inno72AppLog> selectAllByStatus(int status);
}