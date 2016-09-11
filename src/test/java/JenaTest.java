import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map.Entry;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.impl.PropertyImpl;
import org.junit.Test;

public class JenaTest {
	
	class Name extends PropertyImpl
	{

		public Name(String uri) {
			super(uri);
			// TODO Auto-generated constructor stub
		}
	}
	
	
	@Test
	public void test() throws Exception {
		  // some definitions
        String personURI    = "http://somewhere/MohamedLee";
        String name = "Mohamed Lee";        
        String age = "34";
        String ssn = "456423456";
        String maritalStatus = "single";
        String email = "mohamed.lee@gmx.de";
        String address = "Main Street 1";
                
        Person person = new Person();
        person.setName(name);
        person.setAge(age);
        person.setAddress(address);
        person.setEmail(email);
        person.setMaritalStatus(maritalStatus);
        person.setSsn(ssn);
        
        HashMap<String, String> fieldValuePairs = new HashMap<String, String>();
        fieldValuePairs = getFieldValuePairs(person);      
        
        Model model = ModelFactory.createDefaultModel();
             
        Resource p = model.createResource(personURI);
        
        for (Entry<String, String> entry : fieldValuePairs.entrySet())
        {            
            System.out.println(entry.getKey() + " = " + entry.getValue());
            PropertyImpl prop = new PropertyImpl("file:" + entry.getKey());
            p.addProperty(prop, entry.getValue().toString());
        }
        /*Resource MohamedLee 
          = model.createResource(personURI)
                 .addProperty(VCARD.ADR, address)
                 .addProperty(VCARD.EMAIL, email)
                 .addProperty(VCARD.NAME, name);*/
        
        // now write the model in XML form to a file
        model.write(System.out);
	}

	public HashMap<String, String> getFieldValuePairs(Object obj) throws Exception {
	    Class<?> objClass = obj.getClass();
        HashMap<String, String> fieldValuePairs = new HashMap<String, String>();
	    Field[] fields = objClass.getFields();
	    for(Field field : fields) {
	        String fieldName = field.getName();
	        Object fieldValue = field.get(obj);
	        fieldValuePairs.put(fieldName, fieldValue.toString());	        	        
	    }
		return fieldValuePairs;
	}
	
	
}
