package winemall.dto;

import lombok.Data;

/**
 * @Explain: 数据传输实体
 */
@Data
public class UserDto {
    private String account;
    private String pwd;
    private String verifyCode;
}
