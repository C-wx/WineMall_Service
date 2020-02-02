package winemall.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Explain: 商品属性传输对象
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PropertyDto {
    private String variety;
    private String type;
    private String years;
    private String capacity;
    private String degree;
    private String introduction;

}
