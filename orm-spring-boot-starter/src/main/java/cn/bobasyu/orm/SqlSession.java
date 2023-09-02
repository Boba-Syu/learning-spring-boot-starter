package cn.bobasyu.orm;

import java.io.Closeable;
import java.util.List;

/**
 * 对数据库操作的查询接口
 */
public interface SqlSession extends Closeable {
    <T> T selectOne(String statement);

    <T> T selectOne(String statement, Object parameter);

    <T> List<T> selectList(String statement);

    <T> List<T> selectList(String statement, Object parameter);

    void close();
}
