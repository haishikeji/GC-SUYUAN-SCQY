package com.px.modules.aa.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class UserDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "手机号")
   private String phone;  // 手机号

    @ApiModelProperty(value = "密码")
   private String password; // 密码

}
