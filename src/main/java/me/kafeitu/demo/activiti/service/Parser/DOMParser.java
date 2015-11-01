package me.kafeitu.demo.activiti.service.Parser;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;

import org.w3c.dom.Element;

import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class DOMParser {
	
	public ArrayList<String> getNodeContent(InputStream f,ArrayList<String> NodeNames,String formName){
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		ArrayList<String> content = new ArrayList<String>();

		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document document;
			document = db.parse(f);
			NodeList list;
			for(int i = 0; i<NodeNames.size();i++){
				list = document.getElementsByTagName(NodeNames.get(i));
				for(int j = 0; j<list.getLength();j++)
				{
					Element element = (Element)list.item(j);
					String tagContent = element.getAttribute(formName);
					if(tagContent.equals(null) || tagContent.equals(""))
					{
						break;
					}
					content.add(tagContent);
				}
				
			}
			
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		return content;
	
		
	}
	
//	public static void main(String args[]){
//		File f = new File("src/main/resources/diagrams/leave-formkey/leave-formkey.bpmn"); 
//		DOMParser test = new DOMParser();
//		ArrayList res = test.getNodeContent(f, "userTask", "activiti:formKey");
//		ArrayList res1 = test.getNodeContent(f, "startEvent", "activiti:formKey");
//
//		System.out.print(res.toString());
//		System.out.print(res1.toString());
//		
//	}
	

}
