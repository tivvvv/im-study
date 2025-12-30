package com.tiv.im.study.realtime.communicate.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 自定义消息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"type", "data"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Message {

    private Integer type;

    private Object data;

}
