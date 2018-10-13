package com.freemarker.template.test;

import java.io.File;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import freemarker.cache.FileTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;

@Component
public class FreemarkerUtils {

	public static void readTemplate(Map<String, Object> templateObject) {
		Configuration cfg = new Configuration(Configuration.VERSION_2_3_27);

		TemplateLoader ftl1;
		try {
			// For Relative or Absolute Path Level Template Loading
			ftl1 = new FileTemplateLoader(new File("config"));
			// For Multiple Template Loading
			MultiTemplateLoader mtl = new MultiTemplateLoader(new TemplateLoader[] { ftl1 });
			cfg.setTemplateLoader(ftl1);

			// For ClassPath Level Template Loading
			cfg.setClassForTemplateLoading(FreemarkerUtils.class, "/templates");

			String templateOutput = FreeMarkerTemplateUtils.processTemplateIntoString(cfg.getTemplate("template.ftl"),
					templateObject);
			System.out.println(templateOutput);

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

}
