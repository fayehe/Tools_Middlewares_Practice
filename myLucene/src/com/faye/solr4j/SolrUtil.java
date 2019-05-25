package com.faye.solr4j;
import java.io.IOException;
import java.util.List;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.beans.DocumentObjectBinder;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.util.NamedList;


public class SolrUtil { //工具类，用来把产品集合批量增加到Solr. 这里就用到了SolrJ第三方包里的api了。
    public static SolrClient client;
    private static String url;
    static {
        url = "http://localhost:8983/solr/fayecore";
        client = new HttpSolrClient.Builder(url).build();
    }

    public static <T> boolean batchSaveOrUpdate(List<T> entities) throws SolrServerException, IOException {
        DocumentObjectBinder binder = new DocumentObjectBinder();
		int total = entities.size();
		int count=0;
        for (T t : entities) {
            SolrInputDocument doc = binder.toSolrInputDocument(t);
            client.add(doc);
            System.out.printf("添加数据到索引中，总共要添加 %d 条记录，当前添加第%d条 %n", total, ++count);
		}
        client.commit();
        return true;
    }
    
    //分页查询
    public static QueryResponse query(String keywords,int startOfPage, int numberOfPage) throws SolrServerException, IOException {
        SolrQuery query = new SolrQuery();
        query.setStart(startOfPage);
        query.setRows(numberOfPage);
         
        query.setQuery(keywords);
        QueryResponse rsp = client.query(query);
        return rsp;
    }
    
    public static void queryHighlight(String keywords) throws SolrServerException, IOException {// 高亮显示
        SolrQuery q = new SolrQuery();
        q.setStart(0); //开始页数
        q.setRows(10); //每页显示条数
        q.setQuery(keywords); // 设置查询关键字
        q.setHighlight(true); // 开启高亮
        q.addHighlightField("name"); // 高亮字段
        q.setHighlightSimplePre("<span style='color:red'>"); // 高亮单词的前缀
        q.setHighlightSimplePost("</span>"); // 高亮单词的后缀
        q.setHighlightFragsize(100); //摘要最长100个字符
        QueryResponse query = client.query(q); //查询
 
        NamedList<Object> response = query.getResponse(); //获取高亮字段name相应结果
        NamedList<?> highlighting = (NamedList<?>) response.get("highlighting");
        for (int i = 0; i < highlighting.size(); i++) {
            System.out.println(highlighting.getName(i) + "：" + highlighting.getVal(i));
        }
         
        SolrDocumentList results = query.getResults(); //获取查询结果
        for (SolrDocument result : results) {
            System.out.println(result.toString());
        }
    }
    
    public static <T> boolean saveOrUpdate(T entity) throws SolrServerException, IOException { //提供一个对象的增加或者更新(都是同一个方法）
        DocumentObjectBinder binder = new DocumentObjectBinder();
        SolrInputDocument doc = binder.toSolrInputDocument(entity);
        client.add(doc);
        client.commit();
        return true;
    }
     
    public static boolean deleteById(String id) { //根据id删除这个索引
        try {
            client.deleteById(id);
            client.commit();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}