package com.navid.nifty.flow.template.ftl;

import com.navid.nifty.flow.ScreenGenerator;
import de.lessvoid.nifty.Nifty;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by alberto on 8/31/15.
 */
public abstract class FtlTemplateGenerator implements ScreenGenerator {

    private final Nifty nifty;
    private final String templateFileName;
    private final Configuration configuration;

    public FtlTemplateGenerator(Nifty nifty, String templateFileName) throws IOException {
        this.nifty = nifty;
        this.templateFileName = templateFileName;
        this.configuration = new FreemarkerConfiguration().createConfiguration();
    }

    @Override
    public void buildScreen(String screenUniqueId) {
        //remove old version if it exists
        if (nifty.getScreen(screenUniqueId) != null) {
            nifty.removeScreen(screenUniqueId);
        }

        Map root = new HashMap();
        root.put("screenUniqueId", screenUniqueId);
        root.putAll(injectProperties());
        try {
            Template temp = configuration.getTemplate(templateFileName);

            StringWriter sw = new StringWriter();

            temp.process(root, sw);
            nifty.registerScreenController();
            nifty.addXml( new ByteArrayInputStream(sw.toString().getBytes(StandardCharsets.UTF_8)));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }

    }

    protected abstract Map injectProperties();

}
