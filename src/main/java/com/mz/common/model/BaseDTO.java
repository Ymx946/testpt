/**
 * Copyright 2013 Sinovatech
 */
package com.mz.common.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Objects;

@Data
@JsonIgnoreProperties(value = {"pageNo", "pageSize", "qkeyword", "orderBy"})
public class BaseDTO implements Serializable {
    private static Logger logger = LoggerFactory.getLogger(BaseDTO.class);

    public final static String[] INJECTION_KEY = new String[]{"--", "drop", "and", "where", "not", "use", "insert", "delete",
            "select", "count", "group", "union", "set", "truncate", "alter", "grant", "execute", "table", "database",
            "case", "when", "call", "declare", "source", "sql", "sleep", "substring", "version", "else", "from", "like"};

    private Integer pageNo = 1;//默认页码
    private Integer pageSize = 10;//默认每页条数

    /**
     * 关键字查询
     */
    private String qkeyword;

    /**
     * 排序
     */
    private String orderBy = " id DESC ";

    /**
     * 判断sql中是否包含关键字
     */
    private boolean isSqlInjection(String sql) {
        if (StringUtils.isBlank(sql)) {
            return false;
        }
        sql = sql.toLowerCase();
        for (String key : INJECTION_KEY) {
            boolean flag = sql.contains(key);
            if (flag) {
                logger.error("存在sql注入==={}==={}", key, sql);
                return true;
            }
        }
        return false;
    }

    private long getOffset() {
        long current = this.getPageNo();
        return current <= 1L ? 0L : (current - 1L) * this.getPageSize();
    }


    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getPageNo(), this.getPageSize(), this.getOffset(), this.getQkeyword(), this.getOrderBy());
    }
}
