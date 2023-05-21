package interfaces;

/**
 * Damageable Represents an object that can receive damage
 */
public interface Damageable {
	/**
	 * Receive damage
	 * 
	 * @param damage Amount of damage to receive
	 */
	public void receiveDamage(int damage);

	/**
	 * Get the health of the object
	 * 
	 * @return Health of the object
	 */
	public int getHealth();
}
