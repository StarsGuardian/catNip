package tests;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;

import controller.catController;
import model.catModel;
import view.catView;

/**
 * This is the Test class
 * Getting coverage is very difficult for this program because different seasons cause different things to happen
 * and since the seasons are based on time, it is impossible to have all seasons at the same time. It is also difficult
 * to get coverage on methods that cause an update in the view, therefore new test methods were created that were the exact
 * same as the original except for omitting the notification for the view to update.
 * @author Matthew
 *
 */
public class catTest {

	/**
	 * This method tests the controller class
	 */
	@Test
	void testController() {
		catView view = new catView();
		catController controller = new catController(view);
		controller.sellCatnip(1);	
		controller.sellCatnip(1000);
		controller.plantCatnip(0, 0);
		controller.consumeCatnip();
		controller.getNewFieldState();
		controller.updateMoney(10000);
		controller.getCatnip();
		controller.getSeason();
		controller.getLegacy();
		controller.getMoney();
		controller.buyLandForTest();
	}
	
	/**
	 * This method tests the model class
	 */
	@Test
	void testModel() {
		catView view = new catView();
		catController controller = new catController(view);
		catModel model = new catModel(view);
		model.checkBuyable();
		model.consume();
		model.getCatNipRemaining();
		model.getModel();
		model.getMoney();
		model.plantCatnip(3, 2);
		controller.harvestForTest();
		model.harvestForTest();
		model.grownForTest(3, 2);
		controller.harvestForTest();
		model.harvestForTest();
		model.returnSeason();
		model.setMoney(100000);
		model.sold(1000);
		model.syncMoneyNip();
		model.buyLandForTest();
	}
}
