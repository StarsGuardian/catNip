package test;
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
		controller.plantCatnip(0, 0, true);
		controller.consumeCatnip();
		controller.getNewFieldState();
		controller.updateMoney(10000);
		controller.getCatnip();
		controller.getSeason();
		controller.getLegacy();
		controller.getMoney();
		controller.buyLand();
	}
	
	/**
	 * This method tests the model class
	 */
	@Test
	void testModel() {
		catView view = new catView();
		catController controller = new catController(view);
		catModel model = new catModel(view);
		model.setSpring(true);
		model.setSummer(false);
		model.setFall(false);
		model.setWinter(false);
		model.checkBuyable();
		model.consume();
		model.getCatNipRemaining();
		model.getModel();
		model.getMoney();
		model.plantCatnip(3, 2, false);
		model.plantCatnip(0, 2, false);
		model.plantCatnip(2, 2, true);
		model.returnSeason();
		model.setMoney(100000);
		model.sold(1000);
		model.syncMoneyNip();
		model.buyLand();
		controller.buyLand();
		controller.checkBuyable();
		controller.paySpeed(100);
		controller.exitGame();
		controller.getAvailableLand();
		controller.getLegacy();
		controller.getNewFieldState();
		controller.getSeason();
		controller.resetGame();
		model.spendMoney(100);
	}
	@Test
	void testModel_2() {
		catView view = new catView();
		catController controller = new catController(view);
		catModel model = new catModel(view);
		model.setSummer(true);
		model.setSpring(false);
		model.setFall(false);
		model.setWinter(false);
		model.checkBuyable();
		model.consume();
		model.getCatNipRemaining();
		model.getModel();
		model.getMoney();
		model.plantCatnip(3, 2, false);
		model.plantCatnip(0, 2, false);
		model.plantCatnip(2, 2, true);
		model.returnSeason();
		model.setMoney(100000);
		model.sold(1000);
		model.syncMoneyNip();
		model.buyLand();
		controller.buyLand();
		controller.checkBuyable();
		controller.paySpeed(100);
		controller.exitGame();
		controller.getAvailableLand();
		controller.getLegacy();
		controller.getNewFieldState();
		controller.getSeason();
		controller.resetGame();
		model.spendMoney(100);
	}
	@Test
	void testModel_3() {
		catView view = new catView();
		catController controller = new catController(view);
		catModel model = new catModel(view);
		model.setSummer(false);
		model.setSpring(false);
		model.setFall(true);
		model.setWinter(false);
		model.checkBuyable();
		model.consume();
		model.getCatNipRemaining();
		model.getModel();
		model.getMoney();
		model.plantCatnip(3, 2, false);
		model.plantCatnip(0, 2, false);
		model.plantCatnip(2, 2, true);
		model.returnSeason();
		model.setMoney(100000);
		model.sold(1000);
		model.syncMoneyNip();
		model.buyLand();
		controller.buyLand();
		controller.checkBuyable();
		controller.paySpeed(100);
		controller.exitGame();
		controller.getAvailableLand();
		controller.getLegacy();
		controller.getNewFieldState();
		controller.getSeason();
		controller.resetGame();
		model.spendMoney(100);
		model.grown(3, 2);
	}
	@Test
	void testModel_4() {
		catView view = new catView();
		catController controller = new catController(view);
		catModel model = new catModel(view);
		view.speedup = true;
		model.plantCatnip(3, 2, false);
		model.plantCatnip(0, 2, false);
		model.plantCatnip(2, 2, true);
		model.setField();
		model.setHarvestCalled(true);
		model.setSummer(false);
		model.setSpring(false);
		model.setFall(false);
		model.setWinter(true);
		model.checkBuyable();
		model.consume();
		model.getCatNipRemaining();
		model.getModel();
		model.getMoney();
		model.returnSeason();
		model.setMoney(100000);
		model.sold(1000);
		model.syncMoneyNip();
		model.buyLand();
		controller.buyLand();
		controller.checkBuyable();
		controller.paySpeed(100);
		controller.exitGame();
		controller.getAvailableLand();
		controller.getLegacy();
		controller.getNewFieldState();
		controller.getSeason();
		controller.resetGame();
		model.spendMoney(100);
		model.harvest();
	}
	@Test
	void test_6() {
		catView view = new catView();
		catController controller = new catController(view);
		catModel model = new catModel(view);
		model.setSummer(false);
		model.setSpring(false);
		model.setFall(true);
		controller.plantCatnip(0, 0, false);
		controller.plantCatnip(1, 0, false);
	}
	
	@Test
	void test_7() {
		catView view = new catView();
		catController controller = new catController(view);
		catModel model = new catModel(view);
		model.setSummer(false);
		model.setSpring(false);
		model.setFall(true);
		controller.plantCatnip(0, 0, true);
		controller.plantCatnip(1, 0, false);
	}
}
