package com.faye.lucene;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.wltea.analyzer.lucene.IKAnalyzer;

public class TestLucene {

	public static void main(String[] args) throws Exception {
		// 1. 准备中文分词器
		IKAnalyzer analyzer = new IKAnalyzer();

		// 2. 索引
		List<String> productNames = new ArrayList<>(); // 2.1. 首先准备10条数据，这10条数据都是字符串，相当于产品表里的数据
		productNames.add("飞利浦led灯泡e27螺口暖白球泡灯家用照明超亮节能灯泡转色温灯泡");
		productNames.add("飞利浦led灯泡e14螺口蜡烛灯泡3W尖泡拉尾节能灯泡暖黄光源Lamp");
		productNames.add("雷士照明 LED灯泡 e27大螺口节能灯3W球泡灯 Lamp led节能灯泡");
		productNames.add("飞利浦 led灯泡 e27螺口家用3w暖白球泡灯节能灯5W灯泡LED单灯7w");
		productNames.add("飞利浦led小球泡e14螺口4.5w透明款led节能灯泡照明光源lamp单灯");
		productNames.add("飞利浦蒲公英护眼台灯工作学习阅读节能灯具30508带光源");
		productNames.add("欧普照明led灯泡蜡烛节能灯泡e14螺口球泡灯超亮照明单灯光源");
		productNames.add("欧普照明led灯泡节能灯泡超亮光源e14e27螺旋螺口小球泡暖黄家用");
		productNames.add("聚欧普照明led灯泡节能灯泡e27螺口球泡家用led照明单灯超亮光源");		
		Directory index = createIndex(analyzer, productNames); //2.2. 通过createIndex方法，把它加入到索引当中

		// 3. 查询器 : 根据关键字 护眼带光源，基于 "name" 字段进行查询。 这个 "name" 字段就是在创建索引步骤里每个Document的 "name" 字段，相当于表的字段名
		String keyword = "护眼带光源";
		Query query = new QueryParser("name", analyzer).parse(keyword);
		

		// 4. 搜索
		IndexReader reader = DirectoryReader.open(index); // 创建索引 reader
		IndexSearcher searcher = new IndexSearcher(reader); // 基于 reader 创建搜索器
		int numberPerPage = 1000; //指定每页要显示多少条数据
		System.out.printf("当前一共有%d条数据%n",productNames.size());
		System.out.printf("查询关键字是：\"%s\"%n",keyword);
//		ScoreDoc[] hits = searcher.search(query, numberPerPage).scoreDocs; //执行搜索
		
		int pageNow = 2;
        int pageSize = 3;
//        ScoreDoc[] hits = pageSearch1(query, searcher, pageNow, pageSize);
        ScoreDoc[] hits = pageSearch2(query, searcher, pageNow, pageSize);

		// 5. 显示查询结果
		showSearchResults(searcher, hits, query, analyzer);
		// 6. 关闭查询
		reader.close();
	}
	
	private static ScoreDoc[] pageSearch1(Query query, IndexSearcher searcher, int pageNow, int pageSize)
            throws IOException {
        TopDocs topDocs = searcher.search(query, pageNow*pageSize);
         System.out.println("查询到的总条数\t"+topDocs.totalHits);
         ScoreDoc [] alllScores = topDocs.scoreDocs;
 
         List<ScoreDoc> hitScores = new ArrayList<>();
         
         int start = (pageNow -1)*pageSize ;
         int end = pageSize*pageNow;
         for(int i=start;i<end;i++)
             hitScores.add(alllScores[i]);
         
         ScoreDoc[] hits = hitScores.toArray(new ScoreDoc[]{});
        return hits;
    }
	
	private static ScoreDoc[] pageSearch2(Query query, IndexSearcher searcher, int pageNow, int pageSize) throws IOException {
        int start = (pageNow - 1) * pageSize;
        if(0==start){
        	TopDocs topDocs = searcher.search(query, pageNow*pageSize);
        	return topDocs.scoreDocs;
        }
        
        TopDocs topDocs = searcher.search(query, start); // 查询数据， 结束页面自前的数据都会查询到，但是只取本页的数据
        
        ScoreDoc preScore= topDocs.scoreDocs[start-1]; //获取到上一页最后一条
        
        topDocs = searcher.searchAfter(preScore, query, pageSize); //查询最后一条后的数据的一页数据
        return topDocs.scoreDocs;
	}
	
	private static void showSearchResults(IndexSearcher searcher, ScoreDoc[] hits, Query query, IKAnalyzer analyzer) throws Exception {
		System.out.println("找到 " + hits.length + " 个命中----------------------------------------");
		System.out.println("序号\t匹配度得分\t结果");
		for (int i = 0; i < hits.length; ++i) { //每一个ScoreDoc[] hits 就是一个搜索结果，首先把他遍历出来
			ScoreDoc scoreDoc= hits[i];
			int docId = scoreDoc.doc; //然后获取当前结果的docid, 这个docid相当于就是这个数据在索引中的主键
			Document d = searcher.doc(docId); //再根据主键docid，通过搜索器从索引里把对应的Document取出来
			List<IndexableField> fields = d.getFields();
			System.out.print((i + 1));
			System.out.print("\t" + scoreDoc.score); //scoreDoc.score 表示当前命中的匹配度得分，越高表示匹配程度越高
			
			SimpleHTMLFormatter simpleHTMLFormatter = new SimpleHTMLFormatter("<span style='color:red'>", "</span>");
		    Highlighter highlighter = new Highlighter(simpleHTMLFormatter, new QueryScorer(query));
		 
			//接着就打印出这个Document里面的数据。 
			//虽然当前Document只有name一个字段，但是代码还是通过遍历所有字段的形式，打印出里面的值，这样当Docment有多个字段的时候，代码就不用修改了，兼容性更好点
			for (IndexableField f : fields) { 
				TokenStream tokenStream = analyzer.tokenStream(f.name(), new StringReader(d.get(f.name())));
                String fieldContent = highlighter.getBestFragment(tokenStream, d.get(f.name()));
                System.out.print("\t" + fieldContent);
//				System.out.print("\t" + d.get(f.name()));
			}
			System.out.println("<br>");
		}
	}

	private static Directory createIndex(IKAnalyzer analyzer, List<String> products) throws IOException {
		Directory index = new RAMDirectory(); // 创建内存索引，为什么Lucene会比数据库快？因为它是从内存里查，自然就比数据库里快多了呀
		IndexWriterConfig config = new IndexWriterConfig(analyzer); //根据中文分词器创建配置对象
		IndexWriter writer = new IndexWriter(index, config); //创建索引 writer

		for (String name : products) { // 遍历那10条数据，把他们挨个放进索引里
			addDoc(writer, name);
		}
		writer.close();
		return index;
	}
	
	//每条数据创建一个Document，并把这个Document放进索引里。 这个Document有一个字段，叫做"name"。 TestLucene.java 第49行创建查询器，就会指定查询这个字段
	private static void addDoc(IndexWriter w, String name) throws IOException {
		Document doc = new Document();
		doc.add(new TextField("name", name, Field.Store.YES));
		w.addDocument(doc);
	}
}
