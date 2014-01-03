package helpers;

/**
 * Represents a stop of the tour. (which Spot, Method and resting time)
 */

public class Stop {
	private Spot spot;
	private Method method;
	private double restingTime;

	public Spot getSpot() {
		return spot;
	}

	public Method getMethod() {
		return method;
	}

	public double getRestingTime() {
		return restingTime;
	}

	public void setRestingTime(double restingTime) {
		this.restingTime = restingTime;
	}

	public Stop(Spot spot, Method method, double restingTime) {
		this.spot = spot;
		this.method = method;
		this.restingTime = restingTime;
	}
}
