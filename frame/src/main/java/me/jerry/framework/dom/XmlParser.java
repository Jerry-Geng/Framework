package me.jerry.framework.dom;

import org.dom4j.Branch;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import me.jerry.framework.utils.ReflactUtils;

public class XmlParser {

	public final static String REG_FLOAT = "\\d+(\\.\\d*)?";

	public static <T> T parser(InputStream is, Class clazz) {
		try {
			Document document = new SAXReader().read(is);
			return parser(document, clazz);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static <T> T parser(Document document, Class clazz) {
		return (T) parser(document.getRootElement(), clazz);
	}

	@SuppressWarnings("unchecked")
	public static <T> T parser(Element element, Class<T> clazz) {
		// check if the class match the element
		if(clazz == int.class || clazz == Integer.class) {
			Integer value = null;
			if(element != null) {
				String attrValue = element.getText().trim();
				if(attrValue.matches("\\d+")) {
					value = Integer.valueOf(attrValue);
				} else {
					// the value is not an integer value
					throw new XmlException("element " + element.getName() + " is not an integer value.");
				}
			}
			return (T) value;
		} else if(clazz == short.class || clazz == Short.class) {
			Short value = null;
			if(element != null) {
				String attrValue = element.getText().trim();
				if(attrValue.matches("\\d+")) {
					value = Short.valueOf(attrValue);
				} else {
					// the value is not an short value
					throw new XmlException("element " + element.getName() + " is not a short value.");
				}
			}
			return (T) value;
		} else if(clazz == long.class || clazz == Long.class) {
			Long value = null;
			if(element != null) {
				String attrValue = element.getText().trim();
				if(attrValue.matches("\\d+")) {
					value = Long.valueOf(attrValue);
				} else {
					// the value is not an long value
					throw new XmlException("element " + element.getName() + " is not a long value.");
				}
			}
			return (T) value;
		} else if(clazz == float.class || clazz == Float.class) {
			Float value = null;
			if(element != null) {
				String attrValue = element.getText().trim();
				if(attrValue.matches(REG_FLOAT)) {
					value = Float.valueOf(attrValue);
				} else {
					// the value is not an float value
					throw new XmlException("element " + element.getName() + " is not a float value.");
				}
			}
			return (T) value;
		} else if(clazz == double.class || clazz == Double.class) {
			Double value = null;
			if(element != null) {
				String attrValue = element.getText().trim();
				if(attrValue.matches(REG_FLOAT)) {
					value = Double.valueOf(attrValue);
				} else {
					// the value is not an double value
					throw new XmlException("element " + element.getName() + " is not a double value.");
				}
			}
			return (T) value;
		} else if(clazz == boolean.class || clazz == Boolean.class) {
			Boolean value = null;
			if(element != null) {
				String attrValue = element.getText().trim();
				if("true".equals(attrValue) || "false".equals(attrValue)) {
					value = Boolean.valueOf(attrValue);
				} else {
					// the value is not an boolean value
					throw new XmlException("element " + element.getName() + " is not a boolean value.");
				}
			}
			return (T) value;
		}  else if(clazz == String.class) {
			String value = null;
			if(element != null) {
				value = element.getText();
			}
			return (T) value;
		}
		// Class Type, check
		String mainTypeName = null;
		if(clazz.getAnnotation(XmlBean.class) != null) {
			mainTypeName = ((XmlBean)clazz.getAnnotation(XmlBean.class)).elementName();
		}
		if(mainTypeName == null || mainTypeName.isEmpty()) {
			mainTypeName = clazz.getName();
		}
		if(!mainTypeName.equals(element.getName())) {
			throw new XmlException("element name " + element.getName() + " not match the class type name " + mainTypeName );
		}
		// match, create the bean.
		T bean = null;
		try {
			bean = (T) clazz.newInstance();
		} catch (InstantiationException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		}
		Field[] fields = ReflactUtils.getFieldsWithSuper(clazz, Object.class);
		for(Field field : fields) {
			if(field.getAnnotation(XmlField.class) == null) continue;
			field.setAccessible(true);
			String attrName = ((XmlField)field.getAnnotation(XmlField.class)).attributeName();
			if(attrName == null || attrName.isEmpty()) {
				attrName = field.getName();
			}
			String typeName = null;
			Class fieldClazz = field.getType();

			if(field.getAnnotation(XmlField.class).isContent() && field.getType().equals(String.class)) {
				try {
					field.set(bean, element.getText().trim());
					continue;
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}

			if(List.class.isAssignableFrom(field.getType()) || field.getType().isArray()) {
				// array or list
				List<Element> temp = element.elements("list");
				Element list = null;
				if(temp == null || temp.isEmpty()) {
					continue;
				}
				if(temp.size() == 1 && temp.get(0).attribute("nameInParent") == null) {
					list = temp.get(0);
				} else {
					for(Element ele : temp) {
						if(ele.attribute("nameInParent") != null && ele.attributeValue("nameInParent").equals(attrName)) {
							list = ele;
							break;
						}
					}
				}
				if(list != null) {
					List<Element> elementList = list.elements();
					Class type = ((XmlField)field.getAnnotation(XmlField.class)).listType();
					if(List.class.isAssignableFrom(field.getType())) {
						List value = new ArrayList<>();
						if(elementList != null && elementList.size() > 0) {
							for(Element listItem : elementList) {
								value.add(parser(listItem, type));
							}
						}
						try {
							field.set(bean, value);
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						} catch (IllegalArgumentException e) {
							e.printStackTrace();
						}
					} else {
						int len = 0;
						if(elementList != null && elementList.size() > 0) {
							len = elementList.size();
						}
						Object value = Array.newInstance(type, len);
						if(elementList != null && elementList.size() > 0) {
							for (int i = 0; i < elementList.size(); i++) {
								Array.set(value, i, parser(elementList.get(i), type));
							}
						}
						try {
							field.set(bean, value);
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						} catch (IllegalArgumentException e) {
							e.printStackTrace();
						}
					}
				}
			} else if(field.getType() == int.class || field.getType() == Integer.class) {
				// integer
				Integer value = null;
				if(element.attribute(attrName) != null) {
					String attrValue = element.attributeValue(attrName).trim();
					if(attrValue.matches("\\d+")) {
						value = Integer.valueOf(attrValue);
					} else {
						// the value is not an integer value
						throw new XmlException("element " + element.getName() + " is not an integer value.");
					}
				} else if(element.element(attrName) != null) {
					value = (int)parser(element.element(attrName), fieldClazz);
				}
				if(value != null) {
					try {
						field.set(bean, value);
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					}
				}
			} else if(field.getType() == short.class || field.getType() == Short.class) {
				// short
				Short value = null;
				if(element.attribute(attrName) != null) {
					String attrValue = element.attributeValue(attrName).trim();
					if(attrValue.matches("\\d+")) {
						value = Short.valueOf(attrValue);
					} else {
						// the value is not an short value
						throw new XmlException("element " + element.getName() + " is not a short value.");
					}
				} else if(element.element(attrName) != null) {
					value = (short)parser(element.element(attrName), fieldClazz);
				}
				if(value != null) {
					try {
						field.set(bean, value);
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					}
				}
			} else if(field.getType() == long.class || field.getType() == Long.class) {
				// long
				Long value = null;
				if(element.attribute(attrName) != null) {
					String attrValue = element.attributeValue(attrName).trim();
					if(attrValue.matches("\\d+")) {
						value = Long.valueOf(attrValue);
					} else {
						// the value is not an long value
						throw new XmlException("element " + element.getName() + " is not a long value.");
					}
				} else if(element.element(attrName) != null) {
					value = (long)parser(element.element(attrName), fieldClazz);
				}
				if(value != null) {
					try {
						field.set(bean, value);
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					}
				}
			} else if(field.getType() == float.class || field.getType() == Float.class) {
				// float
				Float value = null;
				if(element.attribute(attrName) != null) {
					String attrValue = element.attributeValue(attrName).trim();
					if(attrValue.matches(REG_FLOAT)) {
						value = Float.valueOf(attrValue);
					} else {
						// the value is not an float value
						throw new XmlException("element " + element.getName() + " is not a float value.");
					}
				} else if(element.element(attrName) != null) {
					value = (float)parser(element.element(attrName), fieldClazz);
				}
				if(value != null) {
					try {
						field.set(bean, value);
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					}
				}
			} else if(field.getType() == double.class || field.getType() == Double.class) {
				// double
				Double value = null;
				if(element.attribute(attrName) != null) {
					String attrValue = element.attributeValue(attrName).trim();
					if(attrValue.matches(REG_FLOAT)) {
						value = Double.valueOf(attrValue);
					} else {
						// the value is not an double value
						throw new XmlException("element " + element.getName() + " is not a double value.");
					}
				} else if(element.element(attrName) != null) {
					value = (double)parser(element.element(attrName), fieldClazz);
				}
				if(value != null) {
					try {
						field.set(bean, value);
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					}
				}
			} else if(field.getType() == boolean.class || field.getType() == Boolean.class) {
				// double
				Boolean value = null;
				if(element.attribute(attrName) != null) {
					String attrValue = element.attributeValue(attrName).trim();
					if("true".equals(attrValue) || "false".equals(attrValue)) {
						value = Boolean.valueOf(attrValue);
					} else {
						// the value is not an boolean value
						throw new XmlException("element " + element.getName() + " is not a boolean value.");
					}
				} else if(element.element(attrName) != null) {
					value = (boolean)parser(element.element(attrName), fieldClazz);
				}
				if(value != null) {
					try {
						field.set(bean, value);
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					}
				}
			} else if(field.getType() == String.class) {
				String value = null;
				// string
				if(element.attribute(attrName) != null) {
					value = element.attributeValue(attrName);
				} else if(element.element(attrName) != null) {
					value = (String)parser(element.element(attrName), fieldClazz);
				}
				if(value != null) {
					try {
						field.set(bean, value);
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					}
				}
			} else if(fieldClazz.getAnnotation(XmlBean.class) != null) {
				try {
					typeName = ((XmlBean) fieldClazz.getAnnotation(XmlBean.class)).elementName();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				}
				if(typeName == null || typeName.isEmpty()) {
					typeName = fieldClazz.getName();
				}
				List<Element> temp = element.elements(typeName);
				Element target = null;
				if(temp == null || temp.size() == 0) {
					continue;
				}
				if(temp.size() == 1 && temp.get(0).attribute("nameInParent") == null) {
					target = temp.get(0);
				} else {
					for(Element ele : temp) {
						if(ele.attribute("nameInParent") != null && ele.attributeValue("nameInParent").equals(attrName)) {
							target = ele;
							break;
						}
					}
				}
				if(target != null) {
					Object value = parser(target, fieldClazz);
					if(value != null) {
						try {
							field.set(bean, value);
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						} catch (IllegalArgumentException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		return bean;
	}

	public static <T> void saveXml(OutputStream os, T data ) {
		Document document = DocumentFactory.getInstance().createDocument();
		newElement(document, data);
		XMLWriter xmlWriter = null;
		try {
			xmlWriter = new XMLWriter(os, OutputFormat.createPrettyPrint());
			xmlWriter.write(document);
			xmlWriter.flush();
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(xmlWriter != null) {
					xmlWriter.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static <T> Element newElement(Branch parent, T data) {
		return newElement(parent, data, null);
	}

	public static <T> Element newElement(Branch parent, T data, String name) {
		Element child = null;
		if(data.getClass() == int.class || data.getClass() == Integer.class) {
			child = parent.addElement("integer");
			child.setText(String.valueOf(data));
			return child;
		} else if(data.getClass() == short.class || data.getClass() == Short.class) {
			child = parent.addElement("short");
			child.setText(String.valueOf(data));
			return child;
		} else if(data.getClass() == long.class || data.getClass() == Long.class) {
			child = parent.addElement("long");
			child.setText(String.valueOf(data));
			return child;
		} else if(data.getClass() == float.class || data.getClass() == Float.class) {
			child = parent.addElement("float");
			child.setText(String.valueOf(data));
			return child;
		} else if(data.getClass() == double.class || data.getClass() == Double.class) {
			child = parent.addElement("double");
			child.setText(String.valueOf(data));
			return child;
		} else if(data.getClass() == boolean.class || data.getClass() == Boolean.class) {
			child = parent.addElement("boolean");
			child.setText(String.valueOf(data));
			return child;
		} else if(data.getClass() == String.class) {
			child = parent.addElement("string");
			child.setText(String.valueOf(data));
			return child;
		} else if(List.class.isAssignableFrom(data.getClass()) || data.getClass().isArray()) {
			child = parent.addElement("list");
			if(name != null && !name.isEmpty()) {
				child.addAttribute("nameInParent", name);
			}
			if(List.class.isAssignableFrom(data.getClass())) {
				for(int i = 0; i < ((List)data).size(); i ++) {
					Object item = ((List)data).get(i);
					newElement(child, item, null);
				}
			} else {
				for(int i = 0; i < Array.getLength(data); i ++) {
					Object item = Array.get(data, i);
					newElement(child, item, null);
				}
			}
			return child;
		} else {
			String typeName = null;
			if(data.getClass().getAnnotation(XmlBean.class) == null) {
				return null;
			}
			try {
				typeName = ((XmlBean) data.getClass().getAnnotation(XmlBean.class)).elementName();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}
			if(typeName == null || typeName.isEmpty()) {
				typeName = data.getClass().getName();
			}
			child = parent.addElement(typeName);
			if(name != null && !name.isEmpty()) {
				child.addAttribute("nameInParent", name);
			}
			Field[] fields = ReflactUtils.getFieldsWithSuper(data.getClass(), Object.class);
			for(Field field : fields) {
				field.setAccessible(true);
				Class fieldType = field.getType();
				if(field.getAnnotation(XmlField.class) == null) continue;
				try {
					if(field.get(data) == null) continue;
				} catch (IllegalAccessException e1) {
				} catch (IllegalArgumentException e1) {
				}
				String attrName = ((XmlField)field.getAnnotation(XmlField.class)).attributeName();
				if(attrName == null || attrName.isEmpty()) {
					attrName = field.getName();
				}
				try {
					if(fieldType == int.class || fieldType == Integer.class) {
						child.addAttribute(attrName, String.valueOf(field.get(data)));
					} else if(fieldType == short.class || fieldType == Short.class) {
						child.addAttribute(attrName, String.valueOf(field.get(data)));
					} else if(fieldType == long.class || fieldType == Long.class) {
						child.addAttribute(attrName, String.valueOf(field.get(data)));
					} else if(fieldType == float.class || fieldType == Float.class) {
						child.addAttribute(attrName, String.valueOf(field.get(data)));
					} else if(fieldType == double.class || fieldType == Double.class) {
						child.addAttribute(attrName, String.valueOf(field.get(data)));
					} else if(fieldType == boolean.class || fieldType == Boolean.class) {
						child.addAttribute(attrName, String.valueOf(field.get(data)));
					} else if(fieldType == String.class) {
						child.addAttribute(attrName, String.valueOf(field.get(data)));
					} else if(List.class.isAssignableFrom(data.getClass()) || data.getClass().isArray()) {
						newElement(child, field.get(data), attrName);
					} else {
						newElement(child, field.get(data), attrName);
					}
				} catch (Exception e) {
				}
			}
			return child;
		}
	}
}
