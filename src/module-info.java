/**
 * Diabetes Knight This game is a 2D platformer game where the player is a
 * knight who has diabetes and must collect insulin to survive.<br />
 * The player can also collect sugar to gain power and attack enemies.<br />
 * But be careful, too much sugar will cause the player to die!<br />
 * <hr />
 * This game is made for the 2110215 Programming Methodology class at
 * Chulalongkorn University.<br />
 * <hr />
 * <h2>How to play:</h2>
 * <ul>
 * <li>Use 'A' and 'D' to move left and right</li>
 * <li>Use 'Space' to jump</li>
 * <li>Use 'Left click' to attack</li>
 * <li>Use '1 - 10' to select item in inventory or use mouse wheel to scroll
 * through items</li>
 * <li>Use 'E' to consume items</li>
 * <li>Use 'F' to use ultimate</li>
 * <li>Use 'Esc' to pause the game</li>
 * </ul>
 */
module diabetesKnight {
	requires javafx.controls;
	requires javafx.graphics;
	requires javafx.media;
	requires java.desktop;
	requires javafx.base;

	opens application to javafx.graphics, javafx.fxml, javafx.media;
}
