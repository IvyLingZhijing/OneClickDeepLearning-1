package acceler.ocdl.service.impl;

import acceler.ocdl.service.TemplateService;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DefaultTemplateService implements TemplateService {


    private final MongoDatabase mongoDatabase;
    private final MongoCollection<Document> collection;


    public DefaultTemplateService() {
        MongoClient mongoClient = new MongoClient("54.175.170.87", 27017);
        mongoDatabase = mongoClient.getDatabase("Oneclick");
        collection = mongoDatabase.getCollection("templates");
    }

    @Override
    public List<String> getTemplatesList(String type) {

        List<String> templatesList = getFile("target/template/"+type);


        return templatesList;
    }


    /*
     * 函数名：getFile
     * 作用：使用递归，输出指定文件夹内的所有文件
     * 参数：path：文件夹路径
     */
    private static List<String> getFile(String path) {
        // 获得指定文件对象
        List<String> nameList = new ArrayList<String>();
        File file = new File(path);
        // 获得该文件夹内的所有文件
        File[] array = file.listFiles();

        for (int i = 0; i < array.length; i++) {
            if (array[i].isFile())//如果是文件
            {
                // 只输出文件名字
                //System.out.println( array[i].getName());

                nameList.add(array[i].getName());
            }
        }
        return nameList;
    }

    @Override
    public Map<String, String> getTemplates(List<String> ids) {

        Map<String, String> templates = new HashMap<>();

        FindIterable<Document> findIterable = collection.find();
        MongoCursor<Document> mongoCursor = findIterable.iterator();
        while (mongoCursor.hasNext()) {

            Document next = mongoCursor.next();

            if (ids.contains(next.get("ID").toString())) {
                templates.put(next.get("code").toString(), next.get("descrp").toString());
            }
        }

        return templates;
    }

    @Override
    public List<String> getTemplates2(String name,String type) {
        List<String> result = new ArrayList<>();
        String code = "";
        try { // 防止文件建立或读取失败，用catch捕捉错误并打印，也可以throw

            /* 读入TXT文件 */
            String pathname = "target/template/"+type+"/"+ name; // 绝对路径或相对路径都可以，这里是绝对路径，写入文件时演示相对路径
            File filename = new File(pathname); // 要读取以上路径的input。txt文件
            InputStreamReader reader = new InputStreamReader(
                    new FileInputStream(filename)); // 建立一个输入流对象reader
            BufferedReader br = new BufferedReader(reader); // 建立一个对象，它把文件内容转成计算机能读懂的语言
            String line = "";
            while (line != null && !line.equals("null")) {
                code += "\n";
                line = br.readLine(); // 一次读入一行数据
                code += line;
            }
        } catch (Exception e) {
        }
        result.add(code);
        System.out.println(code);
        result.add("I'm the description");


        return result;
    }
}