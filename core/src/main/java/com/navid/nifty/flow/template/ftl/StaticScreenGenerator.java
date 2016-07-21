package com.navid.nifty.flow.template.ftl;

import com.navid.nifty.flow.ScreenGenerator;
import de.lessvoid.nifty.Nifty;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.*;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by alberto on 6/6/15.
 */
public class StaticScreenGenerator implements ScreenGenerator {

    private final Nifty nifty;
    private final String templateFileName;
    private final Configuration configuration;

    StaticScreenGenerator(Nifty nifty, String templateFileName/*, Configuration configuration*/) throws IOException {
        this.nifty = nifty;
        this.templateFileName = templateFileName;
        this.configuration = new FreemarkerConfiguration().createConfiguration();
    }

    @Override
    public void buildScreen(String screenUniqueId, String controllerClassName) {
        if (nifty.getScreen(screenUniqueId) == null) {
            Map root = new HashMap();
            root.put("screenUniqueId", screenUniqueId);
            root.put("controllerClassName", controllerClassName);
            try {
                Template temp = configuration.getTemplate(templateFileName);

                StringWriter sw = new StringWriter();

                temp.process(root, sw);
                nifty.registerScreenController();
                nifty.addXml( new ByteArrayInputStream(sw.toString().getBytes("UTF-8")));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TemplateException e) {
                e.printStackTrace();
            }


        }
    }
}
