package com.tiv.im.study.realtime.communicate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AckData implements Serializable {

    private Long sessionId;

    private Long receiverId;

    private String msgId;

}
