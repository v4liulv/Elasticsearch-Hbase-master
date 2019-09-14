package com.sinobest.eshbasetest.ces;

import com.sinobest.eshbasetest.domain.Doc;
import com.sinobest.eshbasetest.domain.PageUtil;
import com.sinobest.eshbasetest.util.Esutil;
import com.sinobest.eshbasetest.util.HbaseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SuppressWarnings("MVCPathVariableInspection")
@Controller
public class EsController {
	private static final Logger LOG = LoggerFactory.getLogger(EsController.class);
	private HbaseUtils hbaseUtils = new HbaseUtils();
	@Autowired
	private Index index;
	public static String path = "D:/index";

	@RequestMapping(value = "/create.do", method = RequestMethod.GET)
	public String createIndex() throws Exception {
		index.createIndex();
		return "create";
	}

	@RequestMapping(value = "/search.do",  method = RequestMethod.GET)
	public String serachArticle(Model model,
			@RequestParam(value="keyWords",required = false) String keyWords,
			@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
			@RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize){
		Map<String,Object> map = null;
		int count = 0;
		try {
			map = Esutil.search(keyWords,"tfjt","doc",(pageNum-1)*pageSize, pageSize);
			if(map != null)
			count = Integer.parseInt(((Long) map.get("count")).toString());
		} catch (Exception e) {
			LOG.error("查询索引错误!",e);
			e.printStackTrace();
		}
		PageUtil<Map<String, Object>> page = new PageUtil<>(String.valueOf(pageNum),String.valueOf(pageSize),count);
		List<Map<String, Object>> articleList = new ArrayList<>();
		if(map != null)
		articleList = (List<Map<String, Object>>) map.get("dataList");
		page.setList(articleList);
		model.addAttribute("total",count);
		model.addAttribute("pageNum",pageNum);
		model.addAttribute("page",page);
		model.addAttribute("kw",keyWords);
		return "search_result";
	}

	/**
	 * 查看文章详细信息
	 * @return detail.jsp
	 */
	//@RequestMapping(name = "/detailDocById/{id}.do", method = RequestMethod.POST)
	//public String detailArticleById(@PathVariable(value="id") String id, Model modelMap) throws IOException {

	@RequestMapping(name = "/detailDocById.do", method = RequestMethod.GET)
	public String detailArticleById(String id, Model modelMap) throws IOException {
		//这里用的查询是直接从hbase中查询一条字符串出来做拆分封装，这里要求protobuffer
		if(HbaseUtils.conn == null || HbaseUtils.conn.isClosed() || HbaseUtils.admin ==null || HbaseUtils.admin.isAborted())
			hbaseUtils = new HbaseUtils();
		Doc doc = hbaseUtils.getDoc(hbaseUtils.TABLE_NAME, id);
		if(doc == null){
			throw new RuntimeException("HBase查询表:{" + hbaseUtils.TABLE_NAME + "}的rowkey:{" + id + "}数据为空");
		}
		//doc.setAuthor(new String(doc.getAuthor().getBytes("gbk"),"UTF-8"));
		doc.setAuthor(doc.getAuthor());
		//doc.setTitle(new String(doc.getTitle().getBytes("gbk"),"UTF-8"));
		doc.setTitle(doc.getTitle());
		//doc.setContent(new String(doc.getContent().getBytes("gbk"),"UTF-8"));
		doc.setContent(doc.getContent());
		//doc.setDescribe(new String(doc.getDescribe().getBytes("gbk"),"UTF-8"));
		doc.setDescribe(doc.getDescribe());
		modelMap.addAttribute("Doc",doc);

		return "detail";
	}
	
	
	
}
