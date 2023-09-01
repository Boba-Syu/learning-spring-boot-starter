package cn.bobasyu.orm;

/**
 * 开启SqlSession的工厂类接口
 */
public interface SqlSessionFactory {

    SqlSession openSession();

}
