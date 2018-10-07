package com.coon.activiti.common.web.controller;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class TestController {
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;

    @RequestMapping(value = "startProcess")
    public String startProcess() {
        Map<String, String> resultMap = new HashMap<>();
        //根据bpmn文件部署流程
        Deployment deployment = repositoryService.createDeployment().addClasspathResource("bpmn\\test.bpmn").deploy();
        //获取流程定义
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).singleResult();
        //启动流程定义，返回流程实例
        ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinition.getId());
        String processId = processInstance.getId();
        System.out.println("流程创建成功，当前流程实例ID："+processId);

        return "流程启动成功，流程实例Id:" + processId;
    }

    @RequestMapping(value = "auditProcess")
    public String auditByTep1(Integer pass) {
        Task task = taskService.createTaskQuery().processInstanceId("5005").singleResult();
        Map<String, Object> variables = new HashMap<>();
        variables.put("pass", pass);
        taskService.complete(task.getId(), variables);
        return "当前签收流程节点：" + task.getName() + " 签收结果未：" + (pass == 1 ? "通过" : "不通过");
    }

}
