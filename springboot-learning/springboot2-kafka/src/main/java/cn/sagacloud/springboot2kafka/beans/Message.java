package cn.sagacloud.springboot2kafka.beans;

import lombok.Data;

import java.util.Date;

@Data
public class Message {
    Long id;
    String msg;
    Date sendTime;
}
