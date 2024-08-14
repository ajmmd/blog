package com.flybearblog.blogserve.common.pojo.po;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Document(indexName = "mall_goods")
public class GoodsSearchPO implements Serializable {

    /**
     * 数据ID
     */
    @Id
    private Long id;

    /**
     * 标题
     */
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String title;

    /**
     * 摘要
     */
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String brief;

    /**
     * 封面图
     */
    @Field(type = FieldType.Keyword)
    private String coverUrl;

    /**
     * 售价
     */
    private BigDecimal salePrice;

    /**
     * 关键词列表
     */
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String keywords;

    /**
     * 上架状态，0=下架，1=上架
     */
    @Field(type = FieldType.Integer)
    private Integer isPutOn;

    /**
     * 销量
     */
    @Field(type = FieldType.Integer)
    private Integer salesCount;

    /**
     * 评论数量
     */
    @Field(type = FieldType.Integer)
    private Integer commentCount;

}
