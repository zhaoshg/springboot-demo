package com.hlgf.sms.utils;

import freemarker.ext.beans.BeansWrapper;
import freemarker.template.TemplateModel;
import freemarker.template.Version;
import org.springframework.ui.ModelMap;

public class FreemarkerUtil {

    public static void initStatics(final ModelMap model) {
        // you can also create the Version like: new Version("2.3.27");
        BeansWrapper wrapper = new BeansWrapper(new Version(2, 3, 28));
        TemplateModel statics = wrapper.getStaticModels();
        model.addAttribute("statics", statics);
    }


    public static int bitAnd(int a, int b) {
        return a & b;
    }

}
