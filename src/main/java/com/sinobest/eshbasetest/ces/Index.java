package com.sinobest.eshbasetest.ces;

import com.sinobest.eshbasetest.domain.Doc;
import com.sinobest.eshbasetest.util.Esutil;
import com.sinobest.eshbasetest.util.HbaseUtils;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Created by root on 2016/3/7 0007.
 */
@Service
public class Index {
    private static final Logger LOG = LoggerFactory.getLogger(Index.class);

    /**
     * APP PATH
     */
    private static String APP_PATH;

    static {
        try {
            APP_PATH = URLDecoder.decode(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("")).getPath() + "/", "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void createIndex() throws Exception {
        List<Doc> arrayList = new ArrayList<>();
        LOG.info("APP_PATH: {}", APP_PATH);
        File file = new File(APP_PATH + "doc1.txt");
        List<String> list = FileUtils.readLines(file, "UTF8");
        for (String line : list) {
            Doc Doc = new Doc();
            String[] split = line.split("\003");
            System.out.print(split[0]);
            int parseInt = Integer.parseInt(split[0].trim());
            Doc.setId(parseInt);
            Doc.setTitle(split[1]);
            Doc.setAuthor(split[2]);
            Doc.setDescribe(split[3]);
            Doc.setContent(split[4]);
            Doc.setCjsj(new SimpleDateFormat().format(new Date()));
            arrayList.add(Doc);
        }
        HbaseUtils hbaseUtils = new HbaseUtils();
        for (Doc Doc : arrayList) {
            try {
                //把数据插入hbase
                hbaseUtils.put(hbaseUtils.TABLE_NAME, Doc.getId() + "", hbaseUtils.COLUMNFAMILY_1, hbaseUtils.COLUMNFAMILY_1_TITLE, Doc.getTitle());
                hbaseUtils.put(hbaseUtils.TABLE_NAME, Doc.getId() + "", hbaseUtils.COLUMNFAMILY_1, hbaseUtils.COLUMNFAMILY_1_AUTHOR, Doc.getAuthor());
                hbaseUtils.put(hbaseUtils.TABLE_NAME, Doc.getId() + "", hbaseUtils.COLUMNFAMILY_1, hbaseUtils.COLUMNFAMILY_1_DESCRIBE, Doc.getDescribe());
                hbaseUtils.put(hbaseUtils.TABLE_NAME, Doc.getId() + "", hbaseUtils.COLUMNFAMILY_1, hbaseUtils.COLUMNFAMILY_1_CONTENT, Doc.getContent());
                hbaseUtils.put(hbaseUtils.TABLE_NAME, Doc.getId() + "", hbaseUtils.COLUMNFAMILY_1, hbaseUtils.COLUMNFAMILY_1_CJSJ, Doc.getCjsj());
                //把数据插入es
                Esutil.addIndex("tfjt", "doc", Doc);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void insert(String dataPath, String indexPath) throws Exception {
        List<Doc> arrayList = new ArrayList<>();
        File file = new File(APP_PATH + "article.txt");
        List<String> list = FileUtils.readLines(file);
        for (String line : list) {
            Doc Doc = new Doc();
            String[] split = line.split("\t");
            int parseInt = Integer.parseInt(split[0].trim());
            Doc.setId(parseInt);
            Doc.setTitle(split[1]);
            Doc.setAuthor(split[2]);
            Doc.setDescribe(split[3]);
            Doc.setContent(split[4]);
            arrayList.add(Doc);
        }
        HbaseUtils hbaseUtils = new HbaseUtils();
        for (Doc Doc : arrayList) {
            try {
                //把数据插入hbase
                hbaseUtils.put(hbaseUtils.TABLE_NAME, Doc.getId() + "", hbaseUtils.COLUMNFAMILY_1, hbaseUtils.COLUMNFAMILY_1_TITLE, Doc.getTitle());
                hbaseUtils.put(hbaseUtils.TABLE_NAME, Doc.getId() + "", hbaseUtils.COLUMNFAMILY_1, hbaseUtils.COLUMNFAMILY_1_AUTHOR, Doc.getAuthor());
                hbaseUtils.put(hbaseUtils.TABLE_NAME, Doc.getId() + "", hbaseUtils.COLUMNFAMILY_1, hbaseUtils.COLUMNFAMILY_1_DESCRIBE, Doc.getDescribe());
                hbaseUtils.put(hbaseUtils.TABLE_NAME, Doc.getId() + "", hbaseUtils.COLUMNFAMILY_1, hbaseUtils.COLUMNFAMILY_1_CONTENT, Doc.getContent());
                //把数据插入es
                Esutil.addIndex("tfjt", "doc", Doc);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
