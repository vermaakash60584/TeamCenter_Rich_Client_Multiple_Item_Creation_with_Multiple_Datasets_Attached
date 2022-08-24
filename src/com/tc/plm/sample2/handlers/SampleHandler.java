package com.tc.plm.sample2.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
// import org.eclipse.ui.IWorkbenchWindow;
// import org.eclipse.ui.handlers.HandlerUtil;

import com.teamcenter.rac.aif.AbstractAIFUIApplication;
import com.teamcenter.rac.aifrcp.AIFUtility;
import com.teamcenter.rac.kernel.TCComponentFolder;
import com.teamcenter.rac.kernel.TCComponentUser;
import com.teamcenter.rac.kernel.TCException;
import com.teamcenter.rac.kernel.TCSession;
import com.teamcenter.services.loose.core.DataManagementService;
import com.teamcenter.services.loose.core._2006_03.DataManagement.CreateItemsOutput;
import com.teamcenter.services.loose.core._2006_03.DataManagement.CreateItemsResponse;
import com.teamcenter.services.loose.core._2006_03.DataManagement.ItemProperties;
import com.teamcenter.services.loose.core._2008_06.DataManagement.DatasetProperties2;
import com.teamcenter.soa.client.model.ModelObject;


public class SampleHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
	//	IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		AbstractAIFUIApplication app = AIFUtility.getCurrentApplication();
		TCSession session = (TCSession) app.getSession();
		TCComponentUser user  = session.getUser();
//		try {
//			String id = user.getUserId();
//			MessageDialog.openInformation(window.getShell(), "Message", "Hello "+ id);
//		} catch (TCException e) {
//			// handle exception
//			e.printStackTrace();
//		}
		// connecting soa with current session
		DataManagementService service = DataManagementService.getService(session.getSoaConnection());
		ItemProperties prop[] = new ItemProperties[3];
		for (int i =0; i<prop.length; i++) {
			prop[i] = new ItemProperties();
			
			double random = Math.random()*10000; // Generates random IDs
			int rand = (int)random;
			String ran = rand+"";
			
			prop[i].clientId = "TC001";
			prop[i].itemId = ran;
			prop[i].name = "Test ITEM -> "+(i+1);
			prop[i].type = "Item";
			prop[i].revId = "A";
			prop[i].description = "bulk item creation";
		}
	
//		int i = 0;
//		ItemProperties prop1 = new ItemProperties();
//		prop1.clientId = "TC001";
//		prop1.itemId = "12345";
//		prop1.name = "test ITEM"+i;
//		prop1.type = "Item";
//		prop1.revId = "A";
//		prop1.description = "bulk item creation";
//		prop[0] = prop1;
		try {
			TCComponentFolder folder =  user.getHomeFolder();
				CreateItemsResponse response = service.createItems(prop, folder, "contents");
				CreateItemsOutput output[] = response.output;
				
				for(int i =0; i<output.length; i++) {
					ModelObject itemrev = output[i].itemRev;
					DatasetProperties2 dataset[] = new DatasetProperties2[2];
					
					for(int j = 0; j<dataset.length; j++) {
						dataset[j] = new DatasetProperties2();
						
						double randomClient = Math.random()*10000; // Generates random IDs
						int randClient = (int)randomClient;
						String ranClient = randClient+"";
						
						dataset[j].clientId = ranClient;
						if (j==1) {
							dataset[j].name = "sample Text";
							dataset[j].type = "Text";
						}
						else {
							dataset[j].name = "sample PDF";
							dataset[j].type = "PDF";
						}
						dataset[j].description = "sample dataset";
						dataset[j].datasetId = "";
						dataset[j].datasetRev = "";
						dataset[j].container = itemrev;
						dataset[j].relationType = "IMAN_specification";
						
					}
					service.createDatasets2(dataset);
					
				}
				
		} catch (TCException e) {
			// Auto-generated catch block
			e.printStackTrace();
		}
		
				
		
		return null;
	}
}
