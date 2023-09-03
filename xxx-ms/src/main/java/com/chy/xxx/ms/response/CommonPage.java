package com.chy.xxx.ms.response;

import com.github.pagehelper.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * 通用分页结果封装
 *
 * @author chy
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("通用分页结果")
public class CommonPage<T> implements Serializable {

    private static final long serialVersionUID = 2275963650709288768L;

    /**
     * 满足条件的总记录数
     */
    @ApiModelProperty("满足条件的总记录数")
    private Long total;

    /**
     * 满足条件的总页数
     */
    @ApiModelProperty("满足条件的总页数")
    private Integer pages;

    /**
     * 当前页数据
     */
    @ApiModelProperty("当前页数据")
    private List<T> list;

    /**
     * 将page helper的分页结果转化为通用分页结果
     *
     * @param pageResult page helper分页结果
     * @return CommonPage
     */
    public static <T> CommonPage<T> restPage(Page<T> pageResult) {
        CommonPage<T> commonPage = new CommonPage<>();
        commonPage.setTotal(pageResult.getTotal());
        commonPage.setPages(pageResult.getPages());
        commonPage.setList(pageResult.getResult());
        return commonPage;
    }

    /**
     * 将page helper的分页结果转化为通用分页结果，使用指定list
     *
     * @param pageResult page helper分页结果
     * @return CommonPage
     */
    public static <T> CommonPage<T> restPage(Page<?> pageResult, List<T> list) {
        CommonPage<T> commonPage = new CommonPage<>();
        commonPage.setTotal(pageResult.getTotal());
        commonPage.setPages(pageResult.getPages());
        commonPage.setList(list);
        return commonPage;
    }

    /**
     * 构建空的分页结果
     *
     * @return CommonPage
     */
    public static <T> CommonPage<T> empty() {
        CommonPage<T> commonPage = new CommonPage<>();
        commonPage.setTotal(0L);
        commonPage.setPages(0);
        commonPage.setList(Collections.emptyList());
        return commonPage;
    }

}
