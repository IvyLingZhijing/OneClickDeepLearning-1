package com.ocdl.proxy.controller;

import com.ocdl.proxy.dto.ProjectDto;
import com.ocdl.proxy.service.JenkinsService;
import com.ocdl.proxy.service.KafkaTopicService;
import com.ocdl.proxy.util.CmdHelper;
import com.ocdl.proxy.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.concurrent.Callable;


@Controller
@RequestMapping(path = "/laucher")
public final class SetUpController {

    @Autowired
    JenkinsService jenkinsService;

    @Autowired
    KafkaTopicService kafkaTopicService;

    @ResponseBody
    @RequestMapping(params = "action=setuplaucher", method = RequestMethod.POST)
    public final Response setUpLaucher(ProjectDto projectDto){

        Response.Builder builder = Response.getBuilder();

        try{

            System.out.println("This is the Laucher........................");
            Path path= Paths.get("/home/ec2-user/OneClickDLTemp/ocdl/proxy/src/main/resources");

            ArrayList<String> args = new ArrayList<String>();
            args.add(projectDto.getId().toString());
            CmdHelper.runCommand("laucher.sh", args, path.toString());
            String gitUrl = "http://ec2-54-89-140-122.compute-1.amazonaws.com/git/" + projectDto.getId();


            String topic = projectDto.getId() + " " + projectDto.getName();
//            kafkaTopicService.createTopic(topic);
            args.clear();
            args.add(topic);
            CmdHelper.runCommand("add_kafka_topic.sh", args, path.toString());

            String outputFileName = "jkmsg.txt";
            String xml = jenkinsService.generateXML(projectDto.getId().toString(), gitUrl, topic, outputFileName);
            jenkinsService.createJob(projectDto.getId().toString(), xml);

            builder.setCode(Response.Code.SUCCESS);

        } catch (Exception e) {
            builder.setCode(Response.Code.ERROR)
                    .setMessage(e.getMessage());

        }
        return builder.build();
    }
}
