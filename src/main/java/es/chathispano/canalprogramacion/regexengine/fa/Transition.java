package es.chathispano.canalprogramacion.regexengine.fa;

/**
 * Interfaz común para las transiciones de un estado.
 * 
 * Una transición puede ser del tipo:
 * 
 * <ul>
 * <li>Transición vacía {@link TransitionEmpty} para autómatas finitos no
 * deterministas.</li>
 * <li>Transición de exclusión de un conjunto de caracteres
 * {@link TransitionExclude}</li>
 * <li>Transición de exclusión de rango de caracteres.
 * {@link TransitionExcludeRange}</li>
 * <li>Transicion de rango de caracateres. {@link TransitionExcludeRange}</li>
 * 
 * </ul>
 */
public interface Transition {

	/**
	 * Comprueba si la transición se cumple para un carácter de entrada.
	 * 
	 * @param character
	 *            carácter a comprobar.
	 * @return true si la transcióon cumple para esa entrada; false al
	 *         contrario.
	 */
	boolean match(char character);

	/**
	 * Establece el estado al que se dirige la transición.
	 * 
	 * @param pState
	 *            Estado {@link State}
	 */
	void setNextState(State pState);

	/**
	 * Obtiene el estado al que lleva esta transición.
	 * 
	 * @return Estado destino. {@link State}
	 */
	State getNextState();

	Object clone() throws CloneNotSupportedException;

}
