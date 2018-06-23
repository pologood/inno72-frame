package com.inno72.spring;

import java.util.Map;

import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.inno72.common.Result;

public class JsonView extends MappingJackson2JsonView {
	public JsonView() {
		super();
	}

	@Override
	protected Object filterModel(Map<String, Object> model) {

		for (Map.Entry<String, Object> entry : model.entrySet()) {

			if (entry.getValue().getClass().getName().equals(Result.class.getName())) {
				return entry.getValue();
			}
		}

		return super.filterModel(model);
	}

}
