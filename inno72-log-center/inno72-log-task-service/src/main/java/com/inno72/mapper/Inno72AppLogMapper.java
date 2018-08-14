package com.inno72.mapper;

import java.util.List;

import com.inno72.common.Mapper;
import com.inno72.model.Inno72AppLog;

public interface Inno72AppLogMapper extends Mapper<Inno72AppLog> {
	List<Inno72AppLog> selectAllByStatus(int status);
}