package rotmg.objects.particles;

public class SparkParticle extends Particle {

	public int lifetime;

	public int timeLeft;

	public int initialSize;

	public double dx;

	public double dy;

	public SparkParticle(int param1, int param2, int param3, double param4, double param5, double param6) {
		super(param2, param4, param1);
		this.initialSize = param1;
		this.lifetime = this.timeLeft = param3;
		this.dx = param5;
		this.dy = param6;
	}

	@Override
	public boolean update(int param1, int param2) {
		this.timeLeft = this.timeLeft - param2;
		if (this.timeLeft <= 0) {
			return false;
		}
		this.x = this.x + ((this.dx * param2) / 1000);
		this.y = this.y + ((this.dy * param2) / 1000);
		this.setSize((this.timeLeft / this.lifetime) * this.initialSize);
		return true;
	}

}
