package com.books.chapters.restfulapi.patterns.parttwo.bpm;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.books.chapters.restfulapi.patterns.parttwo.adapter.amqp.producer.RpcClient;
import com.books.chapters.restfulapi.patterns.parttwo.adapter.entity.BusinessEntity;
import com.books.chapters.restfulapi.patterns.parttwo.adapter.entity.ServiceResponse;


@Component
public class AllocateInventoryActivity implements JavaDelegate {
	private static final Logger LOG = LoggerFactory.getLogger(AllocateInventoryActivity.class);

	public static final String SERVICE_ACTION = "allocate";

	@Autowired
	RpcClient rpcClient;

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		LOG.info("execute {} - {}", ProcessConstants.SERVICE_NAME_INVENTORY, SERVICE_ACTION);
		BusinessEntity sc = (BusinessEntity) execution.getVariable(ProcessConstants.VAR_SC);
		ServiceResponse response = rpcClient.invokeService(
				ProcessUtil.buildServiceRequest(sc, ProcessConstants.SERVICE_NAME_INVENTORY, SERVICE_ACTION));
		ProcessUtil.processResponse(execution, response);
		execution.setVariable(ProcessConstants.VAR_INVENTORY_ALLOCATED, true);
	}

}
