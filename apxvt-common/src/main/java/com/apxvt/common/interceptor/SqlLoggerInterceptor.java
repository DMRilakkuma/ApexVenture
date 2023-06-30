package com.apxvt.common.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.TypeHandlerRegistry;

import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.util.*;

@Slf4j
@Intercepts({
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class,
                ResultHandler.class, CacheKey.class, BoundSql.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class,
                ResultHandler.class})
})
public class SqlLoggerInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        Object parameter = invocation.getArgs().length > 1 ? invocation.getArgs()[1] : null;
        String sqlId = mappedStatement.getId();
        BoundSql boundSql = mappedStatement.getBoundSql(parameter);
        Configuration configuration = mappedStatement.getConfiguration();
        Object returnValue = null;
        String sql = generateSql(configuration, boundSql);

        long end;
        long start;
        try {
            start = System.currentTimeMillis();
            returnValue = invocation.proceed();
            end = System.currentTimeMillis();
        } catch (InvocationTargetException | IllegalAccessException e) {
            logSqlError(sqlId, sql, e);
            return null;
        }
        logSql(sqlId, sql, returnValue, end - start, mappedStatement);

        return returnValue;
    }

    private void logSqlError(String sqlId, String sql, Exception e) {
        log.error("\n" +
                "\033[31m----------------------------------SqlLogs ----------------------------------------\n" +
                "==> 调用方法 : " + sqlId + "\n" +
                "==> sql : " + sql + "\n" +
                "==> sql异常 : " + e.getClass().getSimpleName() + "\n" +
                "----------------------------------SqlLogs ----------------------------------------\033[m\n\n", e);
    }

    private void logSql(String sqlId, String sql, Object returnValue, long time, MappedStatement mappedStatement) {
        long count = 0;
        String resultType = "";
        if (returnValue instanceof Number) {
            Number number = (Number) returnValue;
            if (number instanceof Long) {
                count = number.longValue();
            } else if (number instanceof Integer) {
                count = number.intValue();
            } else if (number instanceof Short) {
                count = number.shortValue();
            } else if (number instanceof Byte) {
                count = number.byteValue();
            }
            if (SqlCommandType.INSERT.equals(mappedStatement.getSqlCommandType())) {
                resultType = "==> 插入了 \033[33m" + count + "\033[m 条数据\n";
            } else if (SqlCommandType.UPDATE.equals(mappedStatement.getSqlCommandType())) {
                resultType = "==> 更新了 \033[33m" + count + "\033[m 条数据\n";
            } else if (SqlCommandType.DELETE.equals(mappedStatement.getSqlCommandType())) {
                resultType = "==> 删除了 \033[33m" + count + "\033[m 条数据\n";
            }
        } else {
            List resultList = (ArrayList) returnValue;
            if (resultList.size() == 1 && resultList.get(0) instanceof Number) {
                Number number = (Number) resultList.get(0);
                if (number instanceof Long) {
                    count = number.longValue();
                } else if (number instanceof Integer) {
                    count = number.intValue();
                } else if (number instanceof Short) {
                    count = number.shortValue();
                } else if (number instanceof Byte) {
                    count = number.byteValue();
                }
                resultType = "==> 查询到一条数值为 \033[33m" + count + "\033[m\n";
            } else {
                count = resultList.size();
                resultType = "==> 查询到 \033[33m" + count + "\033[m 条数据\n";
            }
        }
        System.out.print("\n");
        log.trace("\n" +
                "\033[32m----------------------------------SqlLogs ----------------------------------------\033[m\n" +
                "==> 调用方法 : " + sqlId + "\n" +
                "==> sql : \033[33m" + sql + "\033[m\n" +
                resultType +
                "==> 耗时 : " + time + "ms\n" +
                "\033[32m----------------------------------SqlLogs ----------------------------------------\033[m\n");
    }

    private static String getParameterValue(Object obj) {
        if (obj instanceof String) {
            return "'" + obj.toString() + "'";
        } else if (obj instanceof Date) {
            DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.CHINA);
            return "'" + formatter.format(obj) + "'";
        } else {
            return obj != null ? obj.toString() : "";
        }
    }

    public static String generateSql(Configuration configuration, BoundSql boundSql) {
        Object parameterObject = boundSql.getParameterObject();
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        String sql = boundSql.getSql().replaceAll("[\\s]+", " ");
        if (parameterMappings.size() > 0 && parameterObject != null) {
            TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
            if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                sql = sql.replaceFirst("\\?", getParameterValue(parameterObject));
            } else {
                MetaObject metaObject = configuration.newMetaObject(parameterObject);
                for (ParameterMapping parameterMapping : parameterMappings) {
                    String propertyName = parameterMapping.getProperty();
                    if (metaObject.hasGetter(propertyName)) {
                        Object obj = metaObject.getValue(propertyName);
                        sql = sql.replaceFirst("\\?", getParameterValue(obj));
                    } else if (boundSql.hasAdditionalParameter(propertyName)) {
                        Object obj = boundSql.getAdditionalParameter(propertyName);
                        sql = sql.replaceFirst("\\?", getParameterValue(obj));
                    }
                }
            }
        }
        return sql;
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        // Handle properties if needed
    }
}
