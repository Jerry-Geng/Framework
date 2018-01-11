package me.jerry.framework.dom;

import java.lang.reflect.Field;
import java.util.ArrayList;

@XmlBean(elementName="xml_bean_example")
public class XmlBeanExample {
	@XmlField
	public String example;
	@XmlField
	public int age;
	@XmlField(listType = String.class)
	public ArrayList<String> names;
	@XmlField
	public double weight;

	public String toString(){
		StringBuilder sb = new StringBuilder();
		Field[] fields = getClass().getDeclaredFields();
		sb.append(getClass().getSimpleName()).append("{ ");
		for(Field field : fields) {
			field.setAccessible(true);
			try {
				String value = null;
				if(field.get(this) != null) {
					if(field.getType() == byte[].class) {
						StringBuilder builder = new StringBuilder();
						builder.append("[");
						for(byte b : (byte[])field.get(this)){
							builder.append(b).append(",");
						}
						value = builder.replace(builder.length() - 1, builder.length(), "]").toString();
					} else {
						value = field.get(this).toString();
					}
				}
				sb.append(field.getName()).append(" = ").append(value).append("; ");
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		sb.append(" }");
		return sb.toString();
	}
}
