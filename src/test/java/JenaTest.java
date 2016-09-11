import static org.junit.Assert.fail;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.apache.jena.rdf.model.InfModel;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.reasoner.ValidityReport;
import org.apache.jena.util.FileManager;
import org.apache.jena.vocabulary.RDF;
import org.junit.Test;

public class JenaTest {
	
	public static String rdfSchemaFileLocation = "file:/home/erebos/sanbox/rdf-mapping/src/test/java/rdf_schema_sample.txt";
	public static String rdfDataFileLocation = "file:/home/erebos/sanbox/rdf-mapping/src/test/java/rdf_data_sample.txt";

	@Test
	public void test() throws Exception {
		// some definitions
		String personURI = "http://somewhere/MohamedLee";
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
		String NS = "http://somewhere/MohamedLee/";
		for (Entry<String, String> entry : fieldValuePairs.entrySet()) {
			//System.out.println(entry.getKey() + " = " + entry.getValue());						
			Property prop = model.createResource(NS+entry.getKey(), RDF.Property ).as( Property.class );
			p.addProperty(prop, entry.getValue().toString());
		}
		// now write the model in XML form to a file		
		model.write( System.out, "RDF/XML-ABBREV" );
	}

	public HashMap<String, String> getFieldValuePairs(Object obj) throws Exception {
		Class<?> objClass = obj.getClass();
		HashMap<String, String> fieldValuePairs = new HashMap<String, String>();
		Field[] fields = objClass.getFields();
		for (Field field : fields) {
			String fieldName = field.getName();
			Object fieldValue = field.get(obj);
			fieldValuePairs.put(fieldName, fieldValue.toString());
		}
		return fieldValuePairs;
	}
	
	@Test
	public void testValidate()
	{
		try {
			boolean valid = validate();
		} catch (Exception e) {
			fail("Test failed" + e.toString());
		}
	}

	public boolean validate() throws Exception{
		Model schema = FileManager.get().loadModel(rdfSchemaFileLocation);		
		Model data = FileManager.get().loadModel(rdfDataFileLocation);
		InfModel infmodel = ModelFactory.createRDFSModel(schema, data);		
		ValidityReport validity = infmodel.validate();
		if (validity.isValid()) {
			System.out.println("Validation status: OK");
			return true;
		} else {
			System.out.println("Validation status: Conflicts");
			for (Iterator i = validity.getReports(); i.hasNext();) {
				System.out.println(" - " + i.next());
			}
			return false;
		}		
	}

}
