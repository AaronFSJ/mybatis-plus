/*
 * Copyright (c) 2011-2020, hubin (jobob@qq.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.baomidou.mybatisplus.extension.injector.methods;

import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.extension.injector.LogicAbstractMethod;

/**
 * <p>
 * 根据 ID 更新有值字段
 * </p>
 *
 * @author hubin
 * @since 2018-04-06
 */
public class LogicUpdateById extends LogicAbstractMethod {

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        String sql;
        SqlMethod sqlMethod = SqlMethod.UPDATE_BY_ID;
        if (tableInfo.isLogicDelete()) {

            sql = String.format(sqlMethod.getSql(), tableInfo.getTableName(), sqlSet(true, false, tableInfo, "et."),
                tableInfo.getKeyColumn(), new StringBuilder("et.").append(tableInfo.getKeyProperty()).toString(),
                new StringBuilder("<if test=\"et instanceof java.util.Map\">")
                    .append("<if test=\"et.MP_OPTLOCK_VERSION_ORIGINAL!=null\">")
                    .append(" AND ${et.MP_OPTLOCK_VERSION_COLUMN}=#{et.MP_OPTLOCK_VERSION_ORIGINAL}")
                    .append("</if></if>").append(getLogicDeleteSql(true,tableInfo)));
        } else {
            sqlMethod = SqlMethod.UPDATE_BY_ID;
            sql = String.format(sqlMethod.getSql(), tableInfo.getTableName(),
                sqlSet(false, false, tableInfo, "et."),
                tableInfo.getKeyColumn(), "et." + tableInfo.getKeyProperty(),
                new StringBuilder("<if test=\"et instanceof java.util.Map\">")
                    .append("<if test=\"et.MP_OPTLOCK_VERSION_ORIGINAL!=null\">")
                    .append(" AND ${et.MP_OPTLOCK_VERSION_COLUMN}=#{et.MP_OPTLOCK_VERSION_ORIGINAL}")
                    .append("</if></if>"));
        }
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);
        return addUpdateMappedStatement(mapperClass, modelClass, sqlMethod.getMethod(), sqlSource);
    }
}
