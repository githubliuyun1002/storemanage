package com.xiabuxiabu.storemanage.publicutils;




import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import java.util.ArrayList;
import java.util.List;

public class CompareProperties {

    public static boolean contrastObj(Object obj1, Object obj2) {
        boolean isEquals = true;
        if (obj1 instanceof WidthCheck && obj2 instanceof WidthCheck ) {
            WidthCheck pojo1 = (WidthCheck) obj1;
            WidthCheck pojo2 = (WidthCheck) obj2;
            List textList = new ArrayList<String>();
            try {
                Class clazz = pojo1.getClass();
                Field[] fields = pojo1.getClass().getDeclaredFields();
                for (Field field : fields) {
                    PropertyDescriptor pd = new PropertyDescriptor(field.getName(), clazz);
                    Method getMethod = pd.getReadMethod();
                    Object o1 = getMethod.invoke(pojo1);
                    Object o2 = getMethod.invoke(pojo2);
                    if (!o1.toString().equals(o2.toString())) {
                        isEquals = false;
                        textList.add(getMethod.getName() + ":" + "false");
                    } else {
                        textList.add(getMethod.getName() + ":" + "true");
                    }
                }
            } catch (Exception e) {

            }
            for (Object object : textList) {
                System.out.println(object);
            }
        }
        return isEquals;
    }


}
