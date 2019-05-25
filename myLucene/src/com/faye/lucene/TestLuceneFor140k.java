package com.faye.lucene;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.Scanner;
 
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.wltea.analyzer.lucene.IKAnalyzer;
 
public class TestLuceneFor140k {
//	在入门中 TestLucene.java 的基础上进行修改。 主要做了两个方面的修改：
//	1. 索引的增加，以前是10条数据，现在是14万条数据
//		注： 因为数据量比较大， 所以加入到索引的时间也比较久，请耐心等待。
//	2. Document以前只有name字段，现在有6个字段
//	3. 查询关键字从控制台输入，这样每次都可以输入不同的关键字进行查询。 
//		因为索引建立时间比较久，采用这种方式，可以建立一次索引，进行多次查询，否则的话，每次使用不同的关键字，都要耗时建立索引，测试效率会比较低
    public static void main(String[] args) throws Exception {
        IKAnalyzer analyzer = new IKAnalyzer(); // 1. 准备中文分词器
        Directory index = createIndex(analyzer); // 2. 创建索引
	      	//2.2 删除id=51173的数据
	        IndexWriterConfig config = new IndexWriterConfig(analyzer);
	        IndexWriter indexWriter = new IndexWriter(index, config);
	        indexWriter.deleteDocuments(new Term("id", "51173"));
//	        DeleteDocuments(Query query):根据Query条件来删除单个或多个Document
//	        DeleteDocuments(Query[] queries):根据Query条件来删除单个或多个Document
//	        DeleteDocuments(Term term):根据Term来删除单个或多个Document
//	        DeleteDocuments(Term[] terms):根据Term来删除单个或多个Document
//	        DeleteAll():删除所有的Document
	        Document doc = new Document(); // 2.3  更新索引
		        doc.add(new TextField("id", "51173", Field.Store.YES));
		        doc.add(new TextField("name", "神鞭，鞭没了，神还在", Field.Store.YES));
		        doc.add(new TextField("category", "道具", Field.Store.YES));
		        doc.add(new TextField("price", "998", Field.Store.YES));
		        doc.add(new TextField("place", "南海群岛", Field.Store.YES));
		        doc.add(new TextField("code", "888888", Field.Store.YES));
		        indexWriter.updateDocument(new Term("id", "51173"), doc );
	        indexWriter.commit();
	        indexWriter.close();
        Scanner s = new Scanner(System.in); // 3. 查询器
        while(true){
            System.out.print("请输入查询关键字：");
            String keyword = s.nextLine();
            System.out.println("当前关键字是："+keyword);
            Query query = new QueryParser( "name", analyzer).parse(keyword);
            // 4. 搜索
            IndexReader reader = DirectoryReader.open(index);
            IndexSearcher searcher=new IndexSearcher(reader);
            int numberPerPage = 10;
            ScoreDoc[] hits = searcher.search(query, numberPerPage).scoreDocs;
             
            // 5. 显示查询结果
            showSearchResults(searcher, hits,query,analyzer);
            // 6. 关闭查询
            reader.close();
        }
         
    }
 
    private static void showSearchResults(IndexSearcher searcher, ScoreDoc[] hits, Query query, IKAnalyzer analyzer) throws Exception {
        System.out.println("找到 " + hits.length + " 个命中.");
 
        SimpleHTMLFormatter simpleHTMLFormatter = new SimpleHTMLFormatter("<span style='color:red'>", "</span>");
        Highlighter highlighter = new Highlighter(simpleHTMLFormatter, new QueryScorer(query));
 
        System.out.println("找到 " + hits.length + " 个命中.");
        System.out.println("序号\t匹配度得分\t结果");
        for (int i = 0; i < hits.length; ++i) {
            ScoreDoc scoreDoc= hits[i];
            int docId = scoreDoc.doc;
            Document d = searcher.doc(docId);
            List<IndexableField> fields= d.getFields();
            System.out.print((i + 1) );
            System.out.print("\t" + scoreDoc.score);
            for (IndexableField f : fields) {
                if("name".equals(f.name())){
                    TokenStream tokenStream = analyzer.tokenStream(f.name(), new StringReader(d.get(f.name())));
                    String fieldContent = highlighter.getBestFragment(tokenStream, d.get(f.name()));
                    System.out.print("\t"+fieldContent);
                }
                else{
                    System.out.print("\t"+d.get(f.name()));
                }
            }
            System.out.println("<br>");
        }
    }
 
    private static Directory createIndex(IKAnalyzer analyzer) throws IOException {
        Directory index = new RAMDirectory();
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        IndexWriter writer = new IndexWriter(index, config);
        String fileName = "140k_products.txt";
        List<Product> products = ProductUtil.file2list(fileName);
        int total = products.size();
        int count = 0;
        int per = 0;
        int oldPer =0;
        for (Product p : products) {
            addDoc(writer, p);
            count++;
            per = count*100/total;
            if(per!=oldPer){
                oldPer = per;
                System.out.printf("索引中，总共要添加 %d 条记录，当前添加进度是： %d%% %n",total,per);
            }
        }
        writer.close();
        return index;
    }
 
    private static void addDoc(IndexWriter w, Product p) throws IOException {
        Document doc = new Document();
        doc.add(new TextField("id", String.valueOf(p.getId()), Field.Store.YES));
        doc.add(new TextField("name", p.getName(), Field.Store.YES));
        doc.add(new TextField("category", p.getCategory(), Field.Store.YES));
        doc.add(new TextField("price", String.valueOf(p.getPrice()), Field.Store.YES));
        doc.add(new TextField("place", p.getPlace(), Field.Store.YES));
        doc.add(new TextField("code", p.getCode(), Field.Store.YES));
        w.addDocument(doc);
    }
}